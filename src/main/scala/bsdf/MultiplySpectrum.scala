package bsdf

import chisel3._

class MultiplySpectrumBundle extends Bundle {
      val a = Input(new Spectrum)
      val b = Input(new Spectrum)
      val out = Output(new Spectrum)
}

class MultiplySpectrum  extends Module {

  val io = IO(new MultiplySpectrumBundle)


  val multipliers = VecInit.fill(CONSTANTS.SPECTRUM_SAMPLES) {
    val multiply = Module(new Multiply(CONSTANTS.EXPONENT_BITS, CONSTANTS.SIGNIFICAND_BITS))
    multiply.io
  }
  for (i <- 0 until CONSTANTS.SPECTRUM_SAMPLES) {
    multipliers(i).a := io.a.values(i)
    multipliers(i).b := io.b.values(i)
    io.out.values(i) := multipliers(i).out
  }
}

