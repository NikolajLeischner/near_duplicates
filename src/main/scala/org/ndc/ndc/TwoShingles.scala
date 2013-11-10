package org.ndc.ndc

class TwoShingles {
  
  def fromTokens(tokens: Array[String]): Array[String] = {
    if (tokens.length <= 1) {
      return tokens
    }
    else {      
      (for (position <- Range(0, tokens.length - 1)) 
        yield tokens(position) + tokens(position + 1)).toArray
    }
  }  
}