package bsdf

import chisel3._
import chisel3.util.Decoupled
import hardfloat.{MulRecFN, recFNFromFN, fNFromRecFN}

class MultiplyBundle(width: Int) extends Bundle {
      val a = Flipped(Decoupled(SInt(width.W)))
      val b = Flipped(Decoupled(SInt(width.W)))
      val out = Decoupled(SInt(width.W))
}

class Multiply(exp: Int, sig: Int)  extends Module {

  def recode(x: Bits) = recFNFromFN(exp, sig, x)
  def decode(x: Bits) = fNFromRecFN(exp, sig, x)

  val busy = RegInit(false.B)

  val bits = exp + sig
  val a = Reg(SInt(bits.W))
  val b = Reg(SInt(bits.W))

  val io = IO(new MultiplyBundle(bits))
  io.a.ready := ! busy
  io.b.ready := ! busy
  io.out.valid := false.B
  io.out.bits := DontCare

  val mul = Module(new MulRecFN(exp, sig))
  mul.io.a := DontCare
  mul.io.b := DontCare
  //mul.io.a := recode(io.a.bits)
  //mul.io.b := recode(io.b.bits)
  mul.io.roundingMode := 0.U
  mul.io.detectTininess := 0.U
  //io.out.bits := decode(mul.io.out).asSInt

  when(busy) {
    mul.io.a := recode(a)
    mul.io.b := recode(b)
    val out = RegNext(decode(mul.io.out).asSInt)
    val valid = RegNext(true.B)
    io.out.valid := valid
    io.out.bits := out
    when(io.out.ready) {
      busy := false.B
    }
  }.otherwise {
    when(io.a.valid && io.b.valid) {
      a := io.a.deq()
      b := io.b.deq()
      busy := true.B
    }
  }
}

