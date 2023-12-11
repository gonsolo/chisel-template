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
    multiplySpectrum.input.valid := true.B
    multiplySpectrum.input.bits.a := reflectance
    for (i <- 0 until CONSTANTS.SPECTRUM_SAMPLES) {
      multiplySpectrum.input.bits.b.values(i) := invPi
    }
    when (multiplySpectrum.output.valid) {
      output.bits.out := multiplySpectrum.output.bits.out
      resultValid := true.B
      when (output.ready && resultValid) {
        busy := false.B
        resultValid := false.B
        multiplySpectrum.output.ready := true.B
      }
    }
  }.otherwise {
    multiplySpectrum.output.ready := false.B
    when (input.valid) {
      val bundle = input.deq()
      reflectance := bundle.reflectance
      busy := true.B
    }
  }
}

