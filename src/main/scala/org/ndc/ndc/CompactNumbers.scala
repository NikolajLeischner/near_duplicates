package org.ndc.ndc

class CompactNumbers {
  val maxLength = 5
  val pattern = "[0-9]*\\.[0-9]+|[0-9]+"

  private def replaceIfNumber(token: String): String = {
    if (token.matches(pattern)) {
      return token.take(maxLength)
    } else {
      return token
    }
  }

  def processTokens(input: Array[String]): Array[String] = {
    for (token <- input) yield replaceIfNumber(token)
  }

}