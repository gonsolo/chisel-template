package bsdf

import chisel3._

class Spectrum extends Bundle {
  val values = Vec(1, SInt(CONSTANTS.BITS.W))
}

