package org.ndc.ndc

class Tokenizer {

  def fromString(input: String) = {
    input.split(Array(' ', '\n', '\t', '\r', '\f')).filter(_ != "")
  }
}