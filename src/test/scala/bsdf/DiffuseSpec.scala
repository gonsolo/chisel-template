package bsdf

import chisel3._
import chisel3.experimental.BundleLiterals._
import chiseltest._
import java.lang.Float.floatToIntBits
import org.scalatest.freespec.AnyFreeSpec
import scala.math.Pi

class DiffuseSpec extends AnyFreeSpec with ChiselScalatestTester {

  "Diffuse should return inv pi" in {
    test(new Diffuse) { diffuse =>
      diffuse.input.initSource()
      diffuse.input.setSourceClock(diffuse.clock)
      diffuse.output.initSink()
      diffuse.output.setSinkClock(diffuse.clock)

      val testValues = for { x <- 0 to 5 } yield x.toFloat / 3.0f
      val inputSeq = testValues.map { case x =>
        (new DiffuseInputBundle).Lit(_.reflectance.values(0) -> floatToIntBits(x).S)
      }
      val resultSeq = testValues.map { case x =>
        (new DiffuseOutputBundle).Lit(_.out.values(0) -> floatToIntBits(x / Pi.toFloat).S)
      }

      fork {
        diffuse.input.enqueueSeq(inputSeq)
      }.fork {
        diffuse.output.expectDequeueSeq(resultSeq)
      }.join()
    }
  }
}
