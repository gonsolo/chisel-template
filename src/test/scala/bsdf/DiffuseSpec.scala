package bsdf

import chisel3._
import chiseltest._
import org.scalatest.freespec.AnyFreeSpec

class DiffuseSpec extends AnyFreeSpec with ChiselScalatestTester {

  val a = 1116025651.U // float32: 66.6
  //val b = 1073741824.U // float32: 2.0
  //val c = 1124414259.U // float32: 133.2

  "Diffuse should multiply correctly" in {
    test(new Diffuse) { diffuse =>
      diffuse.io.a.poke(a)
      //diffuse.io.b.poke(b)
      println("a: " + diffuse.io.a.peek().litValue)
      //println("b: " + diffuse.io.b.peek().litValue)
      println("Output (should be 0): " + diffuse.io.out.peek().litValue)
      diffuse.io.out.expect(0)
      diffuse.clock.step()
      println("Output (should be " + a.litValue + "): " + diffuse.io.out.peek().litValue)
      diffuse.io.out.expect(a)
    }
  }
}
