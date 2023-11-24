package bsdf

import scala.math.Pi
import chisel3._
import chisel3.util.Pipe
import hardfloat._

object CONSTANTS {
  def EXPONENT_BITS = 8
  def SIGNIFICAND_BITS = 24
  def EXP_SIG_WIDTH = (EXPONENT_BITS + SIGNIFICAND_BITS).W
  def WIDTH = (EXPONENT_BITS + SIGNIFICAND_BITS + 1).W
}

class Diffuse extends Module {

  val io = IO(
    new Bundle {
      val a = Input(Bits(CONSTANTS.WIDTH))
      val b = Input(Bits(CONSTANTS.WIDTH))
      val roundingMode   = Input(UInt(3.W))
      val detectTininess = Input(UInt(1.W))
      val out = Output(Bits(CONSTANTS.WIDTH))
      val exceptionFlags = Output(Bits(5.W))
    }
  )

  val mul = Module(new MulRecFN(CONSTANTS.EXPONENT_BITS, CONSTANTS.SIGNIFICAND_BITS))

  val reg1 = RegInit(0.U(CONSTANTS.WIDTH))
  val reg2 = RegInit(0.U(CONSTANTS.WIDTH))
  val reg3 = RegInit(0.U(CONSTANTS.WIDTH))

  reg1 := recode(io.a)
  //val pipe1 = Pipe(true.B, reg1, 2)

  mul.io.a := reg1
  mul.io.b := reg1
  mul.io.roundingMode := io.roundingMode
  mul.io.detectTininess := io.detectTininess

  reg2 := mul.io.out
  reg3 := decode(reg2)

  io.out := reg3
  //io.out := io.a

  io.exceptionFlags := mul.io.exceptionFlags

  def recode(x: UInt) = recFNFromFN(CONSTANTS.EXPONENT_BITS, CONSTANTS.SIGNIFICAND_BITS, x)

  def decode(x: UInt) = fNFromRecFN(CONSTANTS.EXPONENT_BITS, CONSTANTS.SIGNIFICAND_BITS, x)
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
