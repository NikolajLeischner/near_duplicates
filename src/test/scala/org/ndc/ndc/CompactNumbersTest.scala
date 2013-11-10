package org.ndc.ndc

import org.scalatest.matchers.ShouldMatchers
import org.scalatest.testng.TestNGSuite
import org.testng.Assert._
import org.testng.annotations.Test
import org.testng.annotations.DataProvider

class CompactNumbersTest extends TestNGSuite with ShouldMatchers {

  @DataProvider
  def provideOriginalAndCompactedNumbers = {
    Array(Array[Object](Array("1"), Array("1")),
      Array[Object](Array("12345"), Array("12345")),
      Array[Object](Array("notANumber"), Array("notANumber")),
      Array[Object](Array("2345234523453345"), Array("23452")),
      Array[Object](Array("234520", "n3arly"), Array("23452", "n3arly")))
  }

  @Test(dataProvider = "provideOriginalAndCompactedNumbers")
  def testNumbersAreCompacted(input: Array[String], expectedCompacted: Array[String]) {

    val compact = new CompactNumbers

    val result = compact.processTokens(input)

    result should equal(expectedCompacted)
  }

}
