package bsdf

import chisel3._
import hardfloat.{MulRecFN, recFNFromFN, fNFromRecFN}

class MultiplyBundle(width: Int) extends Bundle {
      val a = Input(SInt(width.W))
      val b = Input(SInt(width.W))
      val roundingMode   = Input(UInt(3.W))
      val detectTininess = Input(UInt(1.W))
      val out = Output(SInt(width.W))
      val exceptionFlags = Output(Bits(5.W))
}

class Multiply(exp: Int, sig: Int)  extends Module {

  def recode(x: Bits) = recFNFromFN(exp, sig, x)
  def decode(x: Bits) = fNFromRecFN(exp, sig, x)

  val bits = exp + sig + 1
  val io = IO(new MultiplyBundle(bits))

  val mul = Module(new MulRecFN(exp, sig))
  mul.io.a := recode(io.a)
  mul.io.b := recode(io.b)
  mul.io.roundingMode := io.roundingMode
  mul.io.detectTininess := io.detectTininess
  io.out := decode(mul.io.out).asSInt
  io.exceptionFlags := mul.io.exceptionFlags
}

