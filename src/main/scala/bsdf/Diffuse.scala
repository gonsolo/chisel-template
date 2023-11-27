package bsdf

import scala.math.Pi
import chisel3._
import chisel3.util.Pipe
import hardfloat._

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

object CONSTANTS {
  def EXPONENT_BITS = 8
  def SIGNIFICAND_BITS = 24
  def SIGN_BITS = 1
  def BITS = EXPONENT_BITS + SIGNIFICAND_BITS + SIGN_BITS
}

class Diffuse() extends Module {
  val io = IO(new MultiplyBundle(CONSTANTS.BITS))
  val multiply = Module(new Multiply(CONSTANTS.EXPONENT_BITS, CONSTANTS.SIGNIFICAND_BITS))
  multiply.io <> io
}

import circt.stage.ChiselStage
import java.io.PrintWriter

object Diffuse extends App {
  val verilog = ChiselStage.emitSystemVerilog(new Diffuse())
  new PrintWriter("Diffuse.v") {
    write(verilog)
    close
  }
}
