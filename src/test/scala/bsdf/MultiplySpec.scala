package bsdf

import chisel3._
import chiseltest._
import org.scalatest.freespec.AnyFreeSpec
import java.lang.Float.{floatToIntBits, intBitsToFloat}

class MultiplySpec extends AnyFreeSpec with ChiselScalatestTester {

  def print(value: Int, expected: Int) = {
      println("out: " + intBitsToFloat(value))
      println("expected: " + intBitsToFloat(expected))
  }

  def test_multiply(diffuse: bsdf.Multiply, a: Float, b: Float) = {
    val aBits = floatToIntBits(a).S
    val bBits = floatToIntBits(b).S
    val expected = floatToIntBits(a * b)
    diffuse.reset.poke(true.B)
    diffuse.clock.step()
    diffuse.reset.poke(false.B)
    diffuse.io.a.values(0).poke(aBits)
    diffuse.io.b.values(0).poke(bBits)
    //diffuse.io.roundingMode.poke(0.U)
    //diffuse.io.detectTininess.poke(0.U)
    for (i <- 0 until 3) {
      //print(diffuse.io.out.peek().litValue, zero)
      diffuse.clock.step()
    }
    //print(diffuse.io.out.peek().litValue, expected)
    diffuse.io.out.values(0).expect(expected)
  }

  "Multiply should multiply correctly" in {
    test(new Multiply(8, 24)) { m =>
      test_multiply(m, 33.2f, 2.7f);
      test_multiply(m, 1.0f, 2.0f);
      test_multiply(m, 0.0f, 3.3f);
      test_multiply(m, -33.2f, 2.7f);
    }
  }
}
