package bsdf

import chisel3._

class Spectrum extends Bundle {
  val values = Vec(CONSTANTS.SPECTRUM_SAMPLES, SInt(CONSTANTS.BITS.W))
}

