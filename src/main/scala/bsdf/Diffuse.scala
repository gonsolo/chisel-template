package bsdf

import scala.math.Pi
import chisel3._
import chisel3.util.Decoupled

object CONSTANTS {
  val NUMBER_SPECTRUM_SAMPLES = 4
  val INVERSE_PI = 1 / Pi
}

// Single precision floating point
class PepeFloat extends Bundle {

  def signBits = 1
  def exponentBits = 8
  def significandBits = 23
  def bits = signBits + exponentBits + significandBits

  val sign = Bool()
  val exponent = UInt(exponentBits.W)
  val significand = UInt(significandBits.W)

  val i2f = Module(new hardfloat.INToRecFN(bits, exponentBits, significandBits))

  def recode(x: UInt) = hardfloat.recFNFromFN(exponentBits, significandBits, x)
}

class Vector3 extends Bundle {
  val x = new PepeFloat
  val y = new PepeFloat
  val z = new PepeFloat
}

class Ratio extends Bundle {
  val spectrum = new SampledSpectrum
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

  output.bits.ratio.spectrum := reflectance.bits // TODO * CONSTANTS.INVERSE_PI
  // Also testing fails now!
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
