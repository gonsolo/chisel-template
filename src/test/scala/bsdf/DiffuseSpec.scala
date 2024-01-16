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

      val testValues = for { x <- -4 to 4 } yield x.toFloat / 3.0f
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

      for ((a, b) <- inputSeq zip resultSeq) {
        diffuse.output.expectInvalid()
        diffuse.input.enqueueNow(a)
        diffuse.output.expectInvalid()
        diffuse.clock.step(5)
        diffuse.output.expectDequeueNow(b)
        diffuse.clock.step(1)
      }
    }
  }
}
