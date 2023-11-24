package bsdf

import scala.math.Pi
import chisel3._
import chisel3.util.Pipe
import hardfloat._

class MultiplyBundle(width: Int) extends Bundle {
      val a = Input(Bits(width.W))
      val b = Input(Bits(width.W))
      val roundingMode   = Input(UInt(3.W))
      val detectTininess = Input(UInt(1.W))
      val out = Output(Bits(width.W))
      val exceptionFlags = Output(Bits(5.W))
}

class Multiply(exp: Int, sig: Int)  extends Module {

  val bits = exp + sig + 1

  val io = IO(new MultiplyBundle(bits))

  val mul = Module(new MulRecFN(exp, sig))

  val aRecoded = RegInit(0.U(bits.W))
  val bRecoded = RegInit(0.U(bits.W))
  val mulRecoded = RegInit(0.U(bits.W))
  val mulDecoded = RegInit(0.U(bits.W))

  aRecoded := recode(io.a)
  bRecoded := recode(io.b)

  mul.io.a := aRecoded
  mul.io.b := bRecoded
  mul.io.roundingMode := io.roundingMode
  mul.io.detectTininess := io.detectTininess
  mulRecoded := mul.io.out

  mulDecoded := decode(mulRecoded)
  io.out := mulDecoded
  io.exceptionFlags := mul.io.exceptionFlags

  def recode(x: UInt) = recFNFromFN(exp, sig, x)
  def decode(x: UInt) = fNFromRecFN(exp, sig, x)
}

object CONSTANTS {
  def EXPONENT_BITS = 8
  def SIGNIFICAND_BITS = 24
  def SIGN_BITS = 1
  def BITS = EXPONENT_BITS + SIGNIFICAND_BITS + SIGN_BITS
}

class Diffuse extends Module {
  val io = IO(new MultiplyBundle(CONSTANTS.BITS))
  val multiply = Module(new Multiply(CONSTANTS.EXPONENT_BITS, CONSTANTS.SIGNIFICAND_BITS))
  multiply.io <> io
}


import circt.stage.ChiselStage
import java.io.PrintWriter

object Diffuse extends App {
  val verilog = ChiselStage.emitSystemVerilog(new Diffuse)
  new PrintWriter("Diffuse.v") {
    write(verilog)
    close
  }
}
