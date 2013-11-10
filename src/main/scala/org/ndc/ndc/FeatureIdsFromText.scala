package org.ndc.ndc

class FeatureIdsFromText {
  val tokenizer = new Tokenizer
  val filterNonAlphaNumeric = new FilterNonAlphaNumeric
  val compactNumbers = new CompactNumbers
  val shingles = new TwoShingles

  def getList(text: String): Array[Int] = {
    val tokens = tokenizer.fromString(text)
    val onlyAlphaNumeric = filterNonAlphaNumeric.processTokens(tokens)
    val withCompactedNumbers = compactNumbers.processTokens(onlyAlphaNumeric)
    val cleanedTokens = withCompactedNumbers.filter(_ != "")
    val lowercase = cleanedTokens.map(_ toLowerCase)
    val withoutStopWords = lowercase.filter(!EnglishStopWords.contains(_))
    val shingled = shingles.fromTokens(withoutStopWords)
    
    
    for (token <- shingled) yield token.hashCode
  }

}