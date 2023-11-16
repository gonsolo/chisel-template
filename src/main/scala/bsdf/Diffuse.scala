package bsdf

import chisel3._
import chisel3.util.Decoupled

class Vector3 extends Bundle {
  val x = UInt(16.W)
  val y = UInt(16.W)
  val z = UInt(16.W)
}

class Ratio extends Bundle {
  val r = UInt(16.W)
}

class Spectrum extends Bundle {
  var r = UInt(16.W)
  var g = UInt(16.W)
  var b = UInt(16.W)
}

class DiffuseInputBundle extends Bundle {
  val outDirection = new Vector3
  val inDirection = new Vector3
}

class DiffuseOutputBundle extends Bundle {
  val ratio = new Ratio
}

class Diffuse extends Module {
  var reflectance = IO(Flipped(Decoupled(new Spectrum)))
  val directions = IO(Flipped(Decoupled(new DiffuseInputBundle)))
  val output = IO(Decoupled(new DiffuseOutputBundle))

  // TODO
  output.bits.ratio := 1.U
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
