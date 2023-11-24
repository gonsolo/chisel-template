package bsdf

import chisel3._
import chiseltest._
import org.scalatest.freespec.AnyFreeSpec
import java.lang.Float.floatToIntBits

class DiffuseSpec extends AnyFreeSpec with ChiselScalatestTester {

  val aFloat = 33.2f
  val bFloat = 2.7f
  val a = floatToIntBits(aFloat).U
  val b = floatToIntBits(bFloat).U
  val expected = floatToIntBits(aFloat * bFloat).U
  val zero = 0.U

  def print(value: BigInt, expected: chisel3.UInt) = {
      println("out: " + value)
      println("expected: " + expected)
  }

  "Diffuse should multiply correctly" in {
    test(new Diffuse) { diffuse =>
      diffuse.io.a.poke(a)
      diffuse.io.b.poke(b)
      diffuse.io.roundingMode.poke(0.U)
      diffuse.io.detectTininess.poke(0.U)
      for (i <- 0 until 3) {
        diffuse.io.out.expect(zero)
        //print(diffuse.io.out.peek().litValue, zero)
        diffuse.clock.step()
      }
      diffuse.io.out.expect(expected)
      //print(diffuse.io.out.peek().litValue, expected)
    }
  }
}
