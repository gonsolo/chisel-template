package bsdf

import chisel3._
import hardfloat.{MulRecFN, recFNFromFN, fNFromRecFN}

class MultiplyBundle(width: Int) extends Bundle {
      val a = Input(new Spectrum)
      val b = Input(new Spectrum)
      val out = Output(new Spectrum)
}

class Multiply(exp: Int, sig: Int)  extends Module {

  def recode(x: Bits) = recFNFromFN(exp, sig, x)
  def decode(x: Bits) = fNFromRecFN(exp, sig, x)

  val bits = exp + sig
  val io = IO(new MultiplyBundle(bits))

  val mul = Module(new MulRecFN(exp, sig))
  mul.io.a := recode(io.a.values(0))
  mul.io.b := recode(io.b.values(0))
  mul.io.roundingMode := 0.U
  mul.io.detectTininess := 0.U
  io.out.values(0) := decode(mul.io.out).asSInt
}

