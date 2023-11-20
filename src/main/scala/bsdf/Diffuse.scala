package bsdf

import scala.math.Pi
import chisel3._
import chisel3.util.Decoupled

object CONSTANTS {
  val NUMBER_SPECTRUM_SAMPLES = 4
  val INVERSE_PI: Float = 1.0.toFloat / Pi.toFloat
}


// Single precision floating point
class PepeFloat extends Bundle {

  def signBits = 1
  def significandBits = 23
  def exponentBits = 8
  def bits = signBits + exponentBits + significandBits

  val sign = Bool()
  val significand = UInt(significandBits.W)
  val exponent = UInt(exponentBits.W)

  // TEST
  val i2f = Module(new hardfloat.INToRecFN(bits, exponentBits, significandBits))

  // TEST
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

class MultiplyPipe() extends Module {
  val io = new Bundle {
    val a = new SampledSpectrum
    val b = new SampledSpectrum
    val result = new SampledSpectrum
  }
  // TODO
}

object InversePi {
  def create() : PepeFloat = {
    val inversePi = new PepeFloat
    inversePi.sign := false.B
    inversePi.significand(0) := 0.U
    inversePi.significand(0) := 1.U
    inversePi.significand(0) := 1.U
    inversePi.significand(0) := 1.U
    inversePi.significand(0) := 1.U
    inversePi.significand(0) := 1.U
    inversePi.significand(0) := 0.U
    inversePi.significand(0) := 1.U
    inversePi.significand(0) := 0.U
    inversePi.significand(0) := 1.U
    inversePi.significand(0) := 0.U
    inversePi.significand(0) := 0.U
    inversePi.significand(0) := 0.U
    inversePi.significand(0) := 1.U
    inversePi.significand(0) := 0.U
    inversePi.significand(0) := 1.U
    inversePi.significand(0) := 1.U
    inversePi.significand(0) := 1.U
    inversePi.significand(0) := 1.U
    inversePi.significand(0) := 1.U
    inversePi.significand(0) := 0.U
    inversePi.significand(0) := 0.U
    inversePi.significand(0) := 1.U
    inversePi.exponent(0) := 1.U
    inversePi.exponent(0) := 0.U
    inversePi.exponent(0) := 0.U
    inversePi.exponent(0) := 0.U
    inversePi.exponent(0) := 0.U
    inversePi.exponent(0) := 0.U
    inversePi.exponent(0) := 1.U
    inversePi.exponent(0) := 1.U
    // 32 bit inv pi: sign sig exp 0 01111101010001011111001 10000011
    inversePi
  }
}

class Diffuse extends Module {
  val reflectance = IO(Flipped(Decoupled(new SampledSpectrum)))
  val directions = IO(Flipped(Decoupled(new DiffuseInputBundle)))
  val output = IO(Decoupled(new DiffuseOutputBundle))

  reflectance.ready := true.B
  directions.ready := true.B

  val inversePi = InversePi.create()

  val multiply = Module(new MultiplyPipe())
  multiply.io.a := reflectance.bits
  multiply.io.b := inversePi
  output.bits.ratio.spectrum := multiply.io.result
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
