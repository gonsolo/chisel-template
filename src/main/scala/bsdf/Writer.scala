package bsdf

import circt.stage.ChiselStage
import java.io.PrintWriter

object Writer extends App {
  val verilog = ChiselStage.emitSystemVerilog(new Diffuse())
  new PrintWriter("Diffuse.v") {
    write(verilog)
    close
  }
}
