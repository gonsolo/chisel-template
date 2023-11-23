package bsdf

import scala.math.Pi
import chisel3._
import chisel3.util.Decoupled
import hardfloat._

object CONSTANTS {
  def EXPONENT_BITS = 8
  def SIGNIFICAND_BITS = 23
  def EXP_SIG_WIDTH = (EXPONENT_BITS + SIGNIFICAND_BITS).W
  def WIDTH = (EXPONENT_BITS + SIGNIFICAND_BITS + 1).W
}

class Diffuse extends Module {

  val io = IO(
    new Bundle {
      val a = Input(Bits(CONSTANTS.WIDTH))
      //val b = Input(Bits(CONSTANTS.WIDTH))
      val out = Output(Bits(CONSTANTS.WIDTH))
      //val exceptionFlags = Output(Bits(5.W))
    }
  )

  //val mul = Module(new hardfloat.MulRecFN(CONSTANTS.EXPONENT_BITS, CONSTANTS.SIGNIFICAND_BITS))
  //mul.io.a := recode(io.a)
  //mul.io.b := recode(io.b)
  //mul.io.roundingMode := 0.U
  //mul.io.detectTininess := 0.U
  ////io.out := decode(mul.io.out)
  //io.out := mul.io.out
  //io.exceptionFlags := mul.io.exceptionFlags

  //io.out := fNFromRecFN(CONSTANTS.EXPONENT_BITS, CONSTANTS.SIGNIFICAND_BITS, tmp)
  //io.out := recFNFromFN(CONSTANTS.EXPONENT_BITS, CONSTANTS.SIGNIFICAND_BITS, io.a)

  val aRecoded = RegInit(0.U(CONSTANTS.WIDTH))
  aRecoded := recFNFromFN(CONSTANTS.EXPONENT_BITS, CONSTANTS.SIGNIFICAND_BITS, io.a)
  io.out := fNFromRecFN(CONSTANTS.EXPONENT_BITS, CONSTANTS.SIGNIFICAND_BITS, aRecoded)

  //def recode(x: UInt) = hardfloat.recFNFromFN(CONSTANTS.EXPONENT_BITS, CONSTANTS.SIGNIFICAND_BITS, x)
  //def decode(x: UInt) = hardfloat.fNFromRecFN(CONSTANTS.EXPONENT_BITS, CONSTANTS.SIGNIFICAND_BITS, x)
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
