package bsdf

import chisel3._
import chisel3.util.Decoupled

class DiffuseInputBundle(val w: Int) extends Bundle {
  val value1 = UInt(w.W)
  val value2 = UInt(w.W)
}

class DiffuseOutputBundle(val w: Int) extends Bundle {
  val value1 = UInt(w.W)
  val value2 = UInt(w.W)
  val gcd    = UInt(w.W)
}

class Diffuse(width: Int) extends Module {
  val input = IO(Flipped(Decoupled(new DiffuseInputBundle(width))))
  val output = IO(Decoupled(new DiffuseOutputBundle(width)))

  // TODO
}

import circt.stage.ChiselStage
import java.io.PrintWriter

object Diffuse extends App {
  val verilog = ChiselStage.emitSystemVerilog(new Diffuse(8))
  new PrintWriter("Diffuse.v") {
    write(verilog)
    close
  }
}
