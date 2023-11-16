package bsdf

import chisel3._
import chisel3.util.Decoupled

object CONSTANTS {
  val NUMBER_SPECTRUM_SAMPLES = 4
}

class PepeFloat extends Bundle {
  val foo = UInt(32.W)
}

class Vector3 extends Bundle {
  val x = new PepeFloat
  val y = new PepeFloat
  val z = new PepeFloat
}

class Ratio extends Bundle {
  val r = new PepeFloat
}

class SampledSpectrum extends Bundle {
  val values = VecInit.fill(CONSTANTS.NUMBER_SPECTRUM_SAMPLES)(Wire(new PepeFloat))
}

class DiffuseInputBundle extends Bundle {
  val outDirection = new Vector3
  val inDirection = new Vector3
}

class DiffuseOutputBundle extends Bundle {
  val ratio = new Ratio
}

class Diffuse extends Module {
  //val reflectance = IO(Flipped(Decoupled(new SampledSpectrum)))
  val directions = IO(Flipped(Decoupled(new DiffuseInputBundle)))
  val output = IO(Decoupled(new DiffuseOutputBundle))

  // TODO

  directions.ready := true.B

  output.bits.ratio.r.foo := 1.U
  output.valid := true.B
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
