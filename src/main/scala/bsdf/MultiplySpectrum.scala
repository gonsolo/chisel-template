package bsdf

import chisel3._
import chisel3.util.Decoupled

class MultiplySpectrumInputBundle extends Bundle {
      val a = new Spectrum
      val b = new Spectrum
}

class MultiplySpectrumOutputBundle extends Bundle {
      val out = new Spectrum
}

class MultiplySpectrum  extends Module {

  val input = IO(Flipped(Decoupled(new MultiplySpectrumInputBundle)))
  val output = IO(Decoupled(new MultiplySpectrumOutputBundle))

  val multipliers = VecInit.fill(CONSTANTS.SPECTRUM_SAMPLES) {
    val multiply = Module(new Multiply(CONSTANTS.EXPONENT_BITS, CONSTANTS.SIGNIFICAND_BITS))
    multiply.io
  }

  val busy = RegInit(false.B)
  val resultValid = RegInit(false.B)
  val a = Reg(new Spectrum)
  val b = Reg(new Spectrum)

  input.ready := ! busy
  output.valid := resultValid
  output.bits := DontCare

  for (i <- 0 until CONSTANTS.SPECTRUM_SAMPLES) {
    multipliers(i).a := DontCare
    multipliers(i).b := DontCare
  }

  when(busy) {
    for (i <- 0 until CONSTANTS.SPECTRUM_SAMPLES) {
      output.bits.out.values(i) := multipliers(i).out
    }
    resultValid := true.B
    when(output.ready && resultValid) {
      busy := false.B
      resultValid := false.B
    }
  }.otherwise {
    when(input.valid) {
      val bundle = input.deq()
      a := bundle.a
      b := bundle.b
      for (i <- 0 until CONSTANTS.SPECTRUM_SAMPLES) {
        multipliers(i).a := a.values(i)
        multipliers(i).b := b.values(i)
      }
      busy := true.B
    }
  }
}

