package bsdf

import chisel3._
import chisel3.util.Decoupled
import java.lang.Float.floatToIntBits
import scala.math.Pi

class DiffuseInputBundle extends Bundle {
  val reflectance = new Spectrum
}

class DiffuseOutputBundle extends Bundle {
  val out = new Spectrum
}

class Diffuse() extends Module {
  val input = IO(Flipped(Decoupled(new DiffuseInputBundle)))
  val output = IO(Decoupled(new DiffuseOutputBundle))
  val multiplySpectrum = Module(new MultiplySpectrum)
  val invPi = RegInit(floatToIntBits(1.0f / Pi.toFloat).S)

  val busy = RegInit(false.B)
  val resultValid = RegInit(false.B)
  val reflectance = Reg(new Spectrum)

  input.ready := ! busy
  output.valid := resultValid
  output.bits := DontCare

  for (i <- 0 until CONSTANTS.SPECTRUM_SAMPLES) {
    multiplySpectrum.input.bits.a.values(i) := DontCare
    multiplySpectrum.input.bits.b.values(i) := DontCare
  }
  multiplySpectrum.output.ready := false.B
  multiplySpectrum.input.valid := false.B

  when(busy) {
    multiplySpectrum.input.bits.a := reflectance
    for (i <- 0 until CONSTANTS.SPECTRUM_SAMPLES) {
      multiplySpectrum.input.bits.b.values(i) := invPi
    }
    multiplySpectrum.input.valid := true.B
    when (multiplySpectrum.output.valid) {
      resultValid := true.B
      when (output.ready && resultValid) {
        output.bits.out := multiplySpectrum.output.bits.out
        resultValid := false.B
        multiplySpectrum.output.ready := true.B
        busy := false.B
      }
    }
  }.otherwise {
    multiplySpectrum.output.ready := false.B
    when (input.valid) {
      reflectance := input.deq().reflectance
      busy := true.B
    }
  }
}

