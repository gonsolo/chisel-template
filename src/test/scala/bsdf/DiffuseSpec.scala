package bsdf

import chisel3._
import chisel3.experimental.BundleLiterals._
import chiseltest._
import java.lang.Float.floatToIntBits
import org.scalatest.flatspec.AnyFlatSpec
import scala.math.Pi

class DiffuseSpec extends AnyFlatSpec with ChiselScalatestTester {

  behavior of "Diffuse"
  
  it should "return inv pi" in {
    test(new Diffuse).withAnnotations(Seq(WriteVcdAnnotation)) { diffuse =>
      diffuse.input.initSource()
      diffuse.input.setSourceClock(diffuse.clock)
      diffuse.output.initSink()
      diffuse.output.setSinkClock(diffuse.clock)

      val testValues = for { x <- -15 to 10 } yield x.toFloat / 3.0f
      val inputSeq = testValues.map { case x =>
        (new DiffuseInputBundle).Lit(
            _.reflectance.values(0) -> floatToIntBits(x).S //,
            //_.reflectance.values(1) -> floatToIntBits(2 * x).S,
            //_.reflectance.values(2) -> floatToIntBits(x).S
        )
      }
      val resultSeq = testValues.map { case x =>
        (new DiffuseOutputBundle).Lit(
          _.out.values(0) -> floatToIntBits(x / Pi.toFloat).S //,
          //_.out.values(1) -> floatToIntBits(2 * x / Pi.toFloat).S,
          //_.out.values(2) -> floatToIntBits(x / Pi.toFloat).S
        )
      }

      case class FloatRoundException() extends Exception("FloatRoundException")

      def check(expected: BigInt, received: BigInt): Unit = {
        val absDiff = (expected - received).abs
        if (absDiff > 1) throw FloatRoundException()
      }

      for ((a, b) <- inputSeq zip resultSeq) {
        diffuse.input.enqueueNow(a)
        diffuse.output.expectInvalid()
        diffuse.clock.step(5)
        val expected = b.out.values(0).litValue

        diffuse.output.ready.poke(true)
        diffuse.output.valid.expect(true)
        val result = diffuse.output.bits.out.values(0).peek().litValue
        diffuse.clock.step(1)
        diffuse.output.ready.poke(false)

        check(expected, result)
        diffuse.clock.step(1)
      }
    }
  }
}
