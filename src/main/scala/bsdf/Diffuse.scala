package bsdf

import chisel3._
import chisel3.util.Decoupled
import circt.stage.ChiselStage
import java.io.PrintWriter
import java.lang.Float.floatToIntBits
import scala.math.Pi

class DiffuseInputBundle(width: Int) extends Bundle {
      val reflectance = Input(SInt(width.W))
}

class DiffuseOutputBundle(width: Int) extends Bundle {
      val out = Output(SInt(width.W))
}

class Diffuse() extends Module {
  val input = IO(Flipped(Decoupled(new DiffuseInputBundle(CONSTANTS.BITS))))
  val output = IO(Decoupled(new DiffuseOutputBundle(CONSTANTS.BITS)))
  val multiply = Module(new Multiply(CONSTANTS.EXPONENT_BITS, CONSTANTS.SIGNIFICAND_BITS))
  val invPi = RegInit(floatToIntBits(1.0f / Pi.toFloat).S)

  val busy = RegInit(false.B)
  val resultValid = RegInit(false.B)
  val reflectance = Reg(SInt())

  input.ready := ! busy
  output.valid := resultValid
  output.bits := DontCare

  multiply.io.a := DontCare
  multiply.io.b := invPi
  multiply.io.roundingMode := 0.U
  multiply.io.detectTininess := 0.U

  when(busy) {
    output.bits.out := multiply.io.out
    resultValid := true.B
    when(output.ready && resultValid) {
      busy := false.B
      resultValid := false.B
    }
  }.otherwise {
    when(input.valid) {
      val bundle = input.deq()
      reflectance := bundle.reflectance
      multiply.io.a := reflectance
      busy := true.B
    }
  }
}

object Diffuse extends App {
  val verilogDiffuse = ChiselStage.emitSystemVerilog(new Diffuse())
  new PrintWriter("Diffuse.v") {
    write(verilogDiffuse)
    close
  }
}
