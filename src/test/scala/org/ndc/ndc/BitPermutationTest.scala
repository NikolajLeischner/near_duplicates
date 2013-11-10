package org.ndc.ndc

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

class BitPermutationTest extends FlatSpec with ShouldMatchers {

  "The 1 bit masks" should "contain 32 bit-masks where 1 bit is set." in {
    val permutations = BitPermutation.masksWithOneBit

    permutations.size should equal(32)
    
    for (value <- permutations) {
      countBits(value) should equal(1)
    }
  }

  "The 2 bit masks" should "contain 31*16 bit-masks where 2 bits are set." in {
    val permutations = BitPermutation.masksWithTwoBits

    permutations.size should equal(31 * 16)

    for (value <- permutations) {
      countBits(value) should equal(2)
    }
  }

  def countBits(value: Int) = {
    var c = 0
    for (offset <- 0 to 31) {
      c += (value >> offset) & 1
    }
    c
  }
}