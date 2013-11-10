package org.ndc.ndc

import org.scalatest.matchers.ShouldMatchers
import org.scalatest.testng.TestNGSuite
import org.testng.Assert._
import org.testng.annotations.Test
import org.testng.annotations.DataProvider

class FeatureIdsFromTextTest extends TestNGSuite with ShouldMatchers {

  case class TextWithTotalAndUniqueFeatureCount(val text: String, val total: Int, val unique: Int)

  @DataProvider
  def provideTextsWithExpectedTokens = {
    Array(Array[Object](TextWithTotalAndUniqueFeatureCount("", 0, 0)),
      Array[Object](TextWithTotalAndUniqueFeatureCount("word", 1, 1)),
      Array[Object](TextWithTotalAndUniqueFeatureCount("two words", 1, 1)),
      Array[Object](TextWithTotalAndUniqueFeatureCount("cat cat cat", 2, 1)),
      Array[Object](TextWithTotalAndUniqueFeatureCount("SaMe same SAMe", 0, 0)),
      Array[Object](TextWithTotalAndUniqueFeatureCount("meaningful\n \t beautiful \f\r\n   whitespace", 2, 2)),
      Array[Object](TextWithTotalAndUniqueFeatureCount("punctuation.., punctuation this-is-compacted", 2, 2)),
      Array[Object](TextWithTotalAndUniqueFeatureCount("123456.., 12345678 12", 2, 2)),
      Array[Object](TextWithTotalAndUniqueFeatureCount("123456.\n., 12345678 1two2 wombat-\n\t\f WORD", 4, 4)))
  }

  @Test(dataProvider = "provideTextsWithExpectedTokens")
  def testInputProducesExpectedFeatures(testCase: TextWithTotalAndUniqueFeatureCount) {
    val featureIds = new FeatureIdsFromText

    val result = featureIds.getList(testCase.text)

    result.size should equal(testCase.total)
    result.toSet.size should equal(testCase.unique)
  }
}