package bsdf

import chisel3._
import chiseltest._
import org.scalatest.freespec.AnyFreeSpec

class DiffuseSpec extends AnyFreeSpec with ChiselScalatestTester {

  "Diffuse should return 1" in {
    test(new Diffuse) { dut =>
      dut.output.bits.ratio.r.foo.expect(1.U)
    }
  }
}
