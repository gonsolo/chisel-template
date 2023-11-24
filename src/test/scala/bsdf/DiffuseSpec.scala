package bsdf

import chisel3._
import chiseltest._
import org.scalatest.freespec.AnyFreeSpec
import java.lang.Float.floatToIntBits

class DiffuseSpec extends AnyFreeSpec with ChiselScalatestTester {

  val a = floatToIntBits(3.0f).U
  val b = a
  val expected = floatToIntBits(9.0f).U
  val zero = 0.U

  def print(value: BigInt, expected: chisel3.UInt) = {
      println("out: " + value)
      println("expected: " + expected)
  }

  "Diffuse should encode/decode correctly" in {
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
