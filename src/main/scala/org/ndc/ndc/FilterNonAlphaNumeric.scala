package org.ndc.ndc

class FilterNonAlphaNumeric {

  def processTokens(tokens: Array[String]): Array[String] = {
    val alphanumerics = ('a' to 'z') ++ ('A' to 'Z') ++ ('0' to '9')

    return for (token <- tokens) yield token.filter(alphanumerics contains _)
  }

}