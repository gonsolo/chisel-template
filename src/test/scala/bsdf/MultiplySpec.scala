package bsdf

import chisel3._
import chiseltest._
import org.scalatest.freespec.AnyFreeSpec
import java.lang.Float.{floatToIntBits, intBitsToFloat}

class MultiplySpec extends AnyFreeSpec with ChiselScalatestTester {

  def print(value: SInt, expected: Int) = {
    val i = value.litValue.toInt
    println("out: " + intBitsToFloat(i))
    println("expected: " + intBitsToFloat(expected))
  }

  def test_multiply(diffuse: bsdf.Multiply, a: Float, b: Float) = {
    val aBits = floatToIntBits(a).S
    val bBits = floatToIntBits(b).S
    val expected = floatToIntBits(a * b)
    diffuse.reset.poke(true.B)
    diffuse.clock.step()
    diffuse.reset.poke(false.B)
    diffuse.io.a.bits.poke(aBits)
    diffuse.io.a.valid.poke(true.B)
    diffuse.io.b.bits.poke(bBits)
    diffuse.io.b.valid.poke(true.B)
    diffuse.io.out.ready.poke(true.B)
    diffuse.io.out.valid.expect(false.B)
    for (i <- 0 until 3) {
      diffuse.clock.step()
    }
    diffuse.io.out.valid.expect(true.B)
    diffuse.io.out.bits.expect(expected)
    //print(diffuse.io.out.bits.peek(), expected)
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
