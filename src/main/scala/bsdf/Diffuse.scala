package bsdf

import chisel3._
import circt.stage.ChiselStage
import java.io.PrintWriter
import java.lang.Float.floatToIntBits
import scala.math.Pi


object CONSTANTS {
  def EXPONENT_BITS = 8

  // 23 bits and 1 implicit
  def SIGNIFICAND_BITS = 24

  def BITS = EXPONENT_BITS + SIGNIFICAND_BITS
}

class DiffuseBundle(width: Int) extends Bundle {
      val reflectance = Input(SInt(width.W))
      val out = Output(SInt(width.W))
}

class Diffuse() extends Module {
  val io = IO(new DiffuseBundle(CONSTANTS.BITS))
  val multiply = Module(new Multiply(CONSTANTS.EXPONENT_BITS, CONSTANTS.SIGNIFICAND_BITS))
  val invPi = RegInit(floatToIntBits(1.0f / Pi.toFloat).S)

  multiply.io.a := io.reflectance
  multiply.io.b := invPi
  multiply.io.roundingMode := 0.U
  multiply.io.detectTininess := 0.U

  io.out := multiply.io.out
}

object Diffuse extends App {
  val verilogDiffuse = ChiselStage.emitSystemVerilog(new Diffuse())
  new PrintWriter("Diffuse.v") {
    write(verilogDiffuse)
    close
  }
}
