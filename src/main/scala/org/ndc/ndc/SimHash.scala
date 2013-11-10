package org.ndc.ndc

class SimHash {

  def fromFeatures(features: Array[Int]): Int = {
    val bitcount = 32
    
    val table = Array.fill(bitcount)(0)
    
    for (value <- features) {
      
      for (bit <- Range(0, bitcount)) {    
        
        if (((value >> bit) & 1) == 1) {
          table(bit) += 1
        }
        else {
          table(bit) -= 1          
        }
      }
    }
    
    var simhash = 0
    for (bit <- Range(0, bitcount)) {
      if (table(bit) > 0) {
        simhash = simhash | (1 << bit)
      }
    }
    
    return simhash    
  }

}