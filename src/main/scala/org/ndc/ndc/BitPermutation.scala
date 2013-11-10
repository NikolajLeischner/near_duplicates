package org.ndc.ndc

object BitPermutation {
  def masksWithOneBit(): Set[Int] = {
    for (offset: Int <- (0 to 31).toSet) yield (1 << offset)
  }

  def masksWithTwoBits(): Set[Int] = {
    val masks = masksWithOneBit
    var result = Set[Int]()
    for (e1 <- masks) {
      result = result ++ (for (e2 <- masks if e1 != e2) yield (e1 | e2))
    }
    return result
  }
}