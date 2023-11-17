package bsdf

import chisel3._
import chisel3.util.Decoupled

object CONSTANTS {
  val NUMBER_SPECTRUM_SAMPLES = 4
}

class PepeFloat extends Bundle {
  val foo = UInt(32.W)
}

case class FloatingType(exponent: Int, significand: Int) {

  def recode(x: UInt) = hardfloat.recFNFromFN(exponent, significand, x)
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
  val values = Vec(CONSTANTS.NUMBER_SPECTRUM_SAMPLES, new PepeFloat)
}

class DiffuseInputBundle extends Bundle {
  val outDirection = new Vector3
  val inDirection = new Vector3
}

class DiffuseOutputBundle extends Bundle {
  val ratio = new Ratio
}

class Diffuse extends Module {
  val reflectance = IO(Flipped(Decoupled(new SampledSpectrum)))
  val directions = IO(Flipped(Decoupled(new DiffuseInputBundle)))
  val output = IO(Decoupled(new DiffuseOutputBundle))

  // TODO

  reflectance.ready := true.B
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
