package bsdf

import chisel3._
import chiseltest._
import org.scalatest.freespec.AnyFreeSpec
import java.lang.Float.floatToIntBits

class DiffuseSpec extends AnyFreeSpec with ChiselScalatestTester {

  val a = floatToIntBits(3.0f).U
  val b = a
  val expected = floatToIntBits(9.0f).U

  "Diffuse should encode/decode correctly" in {
    test(new Diffuse) { diffuse =>
      diffuse.io.a.poke(a)
      diffuse.io.b.poke(b)
      diffuse.io.roundingMode.poke(0.U)
      diffuse.io.detectTininess.poke(0.U)
      for (i <- 0 until 10) {
        diffuse.clock.step(3)
        println("out: " + diffuse.io.out.peek().litValue)
        println("expected: " + expected)
        diffuse.io.out.expect(expected)
      }
    }
  }
}
