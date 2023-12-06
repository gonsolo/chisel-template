package bsdf

import chisel3._
import hardfloat.{MulRecFN, recFNFromFN, fNFromRecFN}

class MultiplyBundle(width: Int) extends Bundle {
      val a = Input(SInt(width.W))
      val b = Input(SInt(width.W))
      val out = Output(SInt(width.W))
}

class Multiply(exp: Int, sig: Int)  extends Module {

  def recode(x: Bits) = recFNFromFN(exp, sig, x)
  def decode(x: Bits) = fNFromRecFN(exp, sig, x)

  val bits = exp + sig
  val io = IO(new MultiplyBundle(bits))

  val mul = Module(new MulRecFN(exp, sig))
  mul.io.a := recode(io.a)
  mul.io.b := recode(io.b)
  mul.io.roundingMode := 0.U
  mul.io.detectTininess := 0.U
  io.out := decode(mul.io.out).asSInt
}

