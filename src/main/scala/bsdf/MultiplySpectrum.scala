package bsdf

import chisel3._

class MultiplySpectrumBundle extends Bundle {
      val a = Input(new Spectrum)
      val b = Input(new Spectrum)
      val out = Output(new Spectrum)
}

class MultiplySpectrum  extends Module {

  val io = IO(new MultiplySpectrumBundle)

  val multiply = Module(new Multiply(CONSTANTS.EXPONENT_BITS, CONSTANTS.SIGNIFICAND_BITS))
  multiply.io.a := io.a
  multiply.io.b := io.b
  io.out := multiply.io.out
}

