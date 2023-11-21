package bsdf

import scala.math.Pi
import chisel3._
import chisel3.util.Decoupled

object CONSTANTS {
  val NUMBER_SPECTRUM_SAMPLES = 4
  //val INVERSE_PI: Float = 1.0.toFloat / Pi.toFloat
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
  //val i2f = Module(new hardfloat.INToRecFN(bits, exponentBits, significandBits))

  // TEST
  def recode(x: UInt) = hardfloat.recFNFromFN(exponentBits, significandBits, x)
}


class Vector3 extends Bundle {
  val x = new PepeFloat
  val y = new PepeFloat
  val z = new PepeFloat
}

class SampledSpectrum extends Bundle {
  val values = Vec(CONSTANTS.NUMBER_SPECTRUM_SAMPLES, new PepeFloat)
}

class BsdfInputBundle extends Bundle {
  val outDirection = new Vector3
  val inDirection = new Vector3
}

class BsdfOutputBundle extends Bundle {
  val ratio = new SampledSpectrum
}

//class MultiplyPipe() extends Module {
//  val io = IO(new Bundle {
//    val a = Input(Bits((8 + 23).W))
//    val b = Input(Bits((8 + 23).W))
//    val out = Output(Bits((8 + 23 + 1).W))
//  })
//  val mul = Module(new hardfloat.MulRecFN(8, 23))
//  mul.io.a := hardfloat.recFNFromFN(8, 23, io.a)
//  mul.io.b := hardfloat.recFNFromFN(8, 23, io.b)
//  mul.io.roundingMode := 0.U // nearEven
//  mul.io.detectTininess := 0.U // tininess before
//  mul.io.out := mul.io.out
//  val exceptionFlags = mul.io.exceptionFlags
//}

class MultiplySpectrum extends Module {
  val io = new Bundle {
    val a = new SampledSpectrum
    val b = new SampledSpectrum
    val out = new SampledSpectrum
  }
//  io.a.values.lazyZip(io.b.values).lazyZip(io.out.values).toList foreach { case (a, b, out) =>
//    val mul = new MultiplyPipe
//    mul.io.a := a
//    mul.io.b := b
//    out := mul.io.out
//  }
}

object InversePi {
  def create() : PepeFloat = {
    val inversePi = Wire(new PepeFloat)
    inversePi.sign := false.B
    //inversePi.significand(0) := 0.U
    //inversePi.significand(0) := 1.U
    //inversePi.significand(0) := 1.U
    //inversePi.significand(0) := 1.U
    //inversePi.significand(0) := 1.U
    //inversePi.significand(0) := 1.U
    //inversePi.significand(0) := 0.U
    //inversePi.significand(0) := 1.U
    //inversePi.significand(0) := 0.U
    //inversePi.significand(0) := 1.U
    //inversePi.significand(0) := 0.U
    //inversePi.significand(0) := 0.U
    //inversePi.significand(0) := 0.U
    //inversePi.significand(0) := 1.U
    //inversePi.significand(0) := 0.U
    //inversePi.significand(0) := 1.U
    //inversePi.significand(0) := 1.U
    //inversePi.significand(0) := 1.U
    //inversePi.significand(0) := 1.U
    //inversePi.significand(0) := 1.U
    //inversePi.significand(0) := 0.U
    //inversePi.significand(0) := 0.U
    //inversePi.significand(0) := 1.U
    //inversePi.exponent(0) := 1.U
    //inversePi.exponent(0) := 0.U
    //inversePi.exponent(0) := 0.U
    //inversePi.exponent(0) := 0.U
    //inversePi.exponent(0) := 0.U
    //inversePi.exponent(0) := 0.U
    //inversePi.exponent(0) := 1.U
    //inversePi.exponent(0) := 1.U
    // 32 bit inv pi: sign sig exp 0 01111101010001011111001 10000011
    inversePi
  }
}

trait Bsdf {
  val directions = IO(Flipped(Decoupled(new BsdfInputBundle)))
  //val output = IO(Decoupled(new BsdfOutputBundle))
}

class Diffuse extends Module with Bsdf {
  val reflectance = IO(Flipped(Decoupled(new SampledSpectrum)))

  reflectance.ready := true.B
  directions.ready := true.B

  //val inversePi = IO(InversePi.create())

  //val inversePi = new PepeFloat
  //inversePi.sign := false.B
  //inversePi.sign := false.B
  //inversePi.significand(0) := 0.U
  val inversePiSpectrum = Reg(new SampledSpectrum)

  val multiply = Module(new MultiplySpectrum())
  //multiply.io.a := reflectance.bits
  //multiply.io.b := inversePiSpectrum
  //output.bits.ratio := multiply.io.out
  // Also testing fails now!

  //output.valid := true.B
  //output.bits.ratio := IO(new SampledSpectrum)
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
