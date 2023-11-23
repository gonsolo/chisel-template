package bsdf

import chisel3._
import chiseltest._
import org.scalatest.freespec.AnyFreeSpec
import java.lang.Float.floatToIntBits

class DiffuseSpec extends AnyFreeSpec with ChiselScalatestTester {

  val a = floatToIntBits(66.6f).U

  "Diffuse should encode/decode correctly" in {
    test(new Diffuse) { diffuse =>
      diffuse.io.a.poke(a)
      diffuse.io.out.expect(0)
      diffuse.clock.step()
      diffuse.io.out.expect(a)
    }
  }
}
