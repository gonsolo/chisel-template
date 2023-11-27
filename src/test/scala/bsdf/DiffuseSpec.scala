package bsdf

import chisel3._
import chiseltest._
import org.scalatest.freespec.AnyFreeSpec
import java.lang.Float.floatToIntBits

class DiffuseSpec extends AnyFreeSpec with ChiselScalatestTester {

  def print(value: BigInt, expected: Int) = {
      println("out: " + value)
      println("expected: " + expected)
  }

  def test_multiply(diffuse: bsdf.Diffuse, a: Float, b: Float) = {
    val aBits = floatToIntBits(a).S
    val bBits = floatToIntBits(b).S
    val expected = floatToIntBits(a * b)
    diffuse.reset.poke(true.B)
    diffuse.clock.step()
    diffuse.reset.poke(false.B)
    diffuse.io.a.poke(aBits)
    diffuse.io.b.poke(bBits)
    diffuse.io.roundingMode.poke(0.U)
    diffuse.io.detectTininess.poke(0.U)
    for (i <- 0 until 3) {
      //print(diffuse.io.out.peek().litValue, zero)
      diffuse.clock.step()
    }
    //print(diffuse.io.out.peek().litValue, expected)
    diffuse.io.out.expect(expected)
  }

  "Diffuse should multiply correctly" in {
    test(new Diffuse()) { diffuse =>
      test_multiply(diffuse, 33.2f, 2.7f);
      test_multiply(diffuse, 1.0f, 2.0f);
      test_multiply(diffuse, 0.0f, 3.3f);
      test_multiply(diffuse, -33.2f, 2.7f);
    }
  }
}
