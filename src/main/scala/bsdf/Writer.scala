package bsdf

import circt.stage.ChiselStage
import java.io.PrintWriter

object Writer extends App {
  val verilog = ChiselStage.emitSystemVerilog(
    gen = new Diffuse(),
    firtoolOpts = Array("--lowering-options=disallowLocalVariables")
  )
  new PrintWriter("Diffuse.v") {
    write(verilog)
    close
  }
}
