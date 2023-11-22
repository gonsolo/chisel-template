package bsdf

import chisel3._
import chiseltest._
import org.scalatest.freespec.AnyFreeSpec

class DiffuseSpec extends AnyFreeSpec with ChiselScalatestTester {

  val a = 1116025651.U // float32: 66.6
  val b = 1073741824.U // float32: 2.0
  val c = 1124414259.U // float32: 133.2

  "Diffuse should multiply correctly" in {
    test(new Diffuse) { diffuse =>
      diffuse.io.a.poke(a)
      diffuse.io.b.poke(b)
      diffuse.clock.step(100)
      diffuse.io.out.expect(c)
    }
  }
}
