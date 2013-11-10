package org.ndc.ndc
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.testng.TestNGSuite
import org.testng.Assert._
import org.testng.annotations.Test
import org.testng.annotations.DataProvider

class TokenizerTest extends TestNGSuite with ShouldMatchers {

  @DataProvider
  def provideInputAndExpectedTokens() = {
    Array(Array[Object]("", Array[String]()),
      Array[Object]("one_word", Array("one_word")),
      Array[Object]("one_word one_word", Array("one_word", "one_word")),
      Array[Object]("12. word", Array("12.", "word")),
      Array[Object]("text\nwith\t different  \r\n   \f  whitespaces",
        Array("text", "with", "different", "whitespaces")))
  }

  @Test(dataProvider = "provideInputAndExpectedTokens")
  def testTokenizesInput(input: String, expectedTokens: Array[String]) {
    val tokenize = new Tokenizer

    val result = tokenize.fromString(input)

    result should equal(expectedTokens)
  }
}