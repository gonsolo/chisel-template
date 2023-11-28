package bsdf

import chisel3._
import chiseltest._
import org.scalatest.freespec.AnyFreeSpec
import java.lang.Float.floatToIntBits
import java.lang.Float.intBitsToFloat
import scala.math.Pi

import chisel3.experimental.BundleLiterals._


class DiffuseSpec extends AnyFreeSpec with ChiselScalatestTester {

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

  "Multiply should multiply correctly" in {
    test(new Multiply(8, 24)) { m =>
      test_multiply(m, 33.2f, 2.7f);
      test_multiply(m, 1.0f, 2.0f);
      test_multiply(m, 0.0f, 3.3f);
      test_multiply(m, -33.2f, 2.7f);
    }
  }

  "Diffuse should return inv pi" in {
    test(new Diffuse) { diffuse =>
      diffuse.input.initSource()
      diffuse.input.setSourceClock(diffuse.clock)
      diffuse.output.initSink()
      diffuse.output.setSinkClock(diffuse.clock)

      val testValues = for { x <- 0 to 5 } yield x.toFloat / 3.0f
      val inputSeq = testValues.map { case x =>
        (new DiffuseInputBundle(32)).Lit(_.reflectance -> floatToIntBits(x).S)
      }
      val resultSeq = testValues.map { case x =>
        (new DiffuseOutputBundle(32)).Lit(_.out -> floatToIntBits(x / Pi.toFloat).S)
      }

      fork {
        diffuse.input.enqueueSeq(inputSeq)
      }.fork {
        diffuse.output.expectDequeueSeq(resultSeq)
      }.join()
    }
  }
}
