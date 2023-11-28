package bsdf

object CONSTANTS {
  def EXPONENT_BITS = 8

  // 23 bits and 1 implicit
  def SIGNIFICAND_BITS = 24

  def BITS = EXPONENT_BITS + SIGNIFICAND_BITS
}

