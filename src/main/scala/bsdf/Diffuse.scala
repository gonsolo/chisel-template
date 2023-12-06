package bsdf

import chisel3._
import chisel3.util.Decoupled
import java.lang.Float.floatToIntBits
import scala.math.Pi

class DiffuseInputBundle extends Bundle {
  val reflectance = Input(new Spectrum)
}

class DiffuseOutputBundle extends Bundle {
      val out = Output(new Spectrum)
}

class Diffuse() extends Module {
  val input = IO(Flipped(Decoupled(new DiffuseInputBundle)))
  val output = IO(Decoupled(new DiffuseOutputBundle))
  val multiply = Module(new MultiplySpectrum)
  val invPi = RegInit(floatToIntBits(1.0f / Pi.toFloat).S)

  val busy = RegInit(false.B)
  val resultValid = RegInit(false.B)
  val reflectance = Reg(new Spectrum)

  input.ready := ! busy
  output.valid := resultValid
  output.bits := DontCare

  for (i <- 0 until CONSTANTS.SPECTRUM_SAMPLES) {
    multiply.io.a.values(i) := DontCare
    multiply.io.b.values(i) := invPi
  }

  when(busy) {
    output.bits.out := multiply.io.out
    resultValid := true.B
    when(output.ready && resultValid) {
      busy := false.B
      resultValid := false.B
    }
  }.otherwise {
    when(input.valid) {
      val bundle = input.deq()
      reflectance := bundle.reflectance
      multiply.io.a := reflectance
      busy := true.B
    }
  }
}

