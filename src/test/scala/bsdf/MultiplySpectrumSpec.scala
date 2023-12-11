package bsdf

import chisel3._
import chisel3.experimental.BundleLiterals._
import chiseltest._
import java.lang.Float.floatToIntBits
import org.scalatest.freespec.AnyFreeSpec

class MultiplySpectrumSpec extends AnyFreeSpec with ChiselScalatestTester {

  "MultiplySpectrum should multiply spectra" in {
    test(new MultiplySpectrum).withAnnotations(Seq(WriteVcdAnnotation)) { multiplySpectrum =>
      multiplySpectrum.input.initSource()
      multiplySpectrum.input.setSourceClock(multiplySpectrum.clock)
      multiplySpectrum.output.initSink()
      multiplySpectrum.output.setSinkClock(multiplySpectrum.clock)

      val testValues = for { x <- 0 to 5 } yield x.toFloat / 3.0f
      val inputSeq = testValues.map { case x =>
        (new MultiplySpectrumInputBundle).Lit(
            _.a.values(0) -> floatToIntBits(x).S,
            _.a.values(1) -> floatToIntBits(2 * x).S,
            _.a.values(2) -> floatToIntBits(x).S,
            _.b.values(0) -> floatToIntBits(x).S,
            _.b.values(1) -> floatToIntBits(2 * x).S,
            _.b.values(2) -> floatToIntBits(x).S
        )
      }
      val resultSeq = testValues.map { case x =>
        (new MultiplySpectrumOutputBundle).Lit(
          _.out.values(0) -> floatToIntBits(x * x).S,
          _.out.values(1) -> floatToIntBits(4 * x * x).S,
          _.out.values(2) -> floatToIntBits(x * x).S
        )
      }

      fork {
        multiplySpectrum.input.enqueueSeq(inputSeq)
      }.fork {
        multiplySpectrum.output.expectDequeueSeq(resultSeq)
      }.join()
    }
  }
}
