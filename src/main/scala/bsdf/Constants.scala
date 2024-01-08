package bsdf

object CONSTANTS {

  // Number of exponent bits of 32-bit floating point values
  def EXPONENT_BITS = 8

  // Number of significand bits of 32-bit floating point values
  // 23 bits and 1 implicit
  def SIGNIFICAND_BITS = 24

  // Number of bits in 32-bit floating point values
  def BITS = EXPONENT_BITS + SIGNIFICAND_BITS

  // Number of spectrum samples
  def SPECTRUM_SAMPLES = 1
}

