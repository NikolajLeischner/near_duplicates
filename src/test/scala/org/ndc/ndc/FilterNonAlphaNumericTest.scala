package org.ndc.ndc

import org.scalatest.matchers.ShouldMatchers
import org.scalatest.testng.TestNGSuite
import org.testng.Assert._
import org.testng.annotations.Test
import org.testng.annotations.DataProvider

class FilterNonAlphaNumericTest extends TestNGSuite with ShouldMatchers {

  @DataProvider
  def provideInputAndExpectedTokens() = {
    Array(Array[Object](Array("this_gets_merged"), Array("thisgetsmerged")),
      Array[Object](Array("123"), Array("123")),
      Array[Object](Array("removedot."), Array("removedot")),
      Array[Object](Array("!�$%&&(=)(/"), Array("")))
  }

  @Test(dataProvider = "provideInputAndExpectedTokens")
  def testFiltersTokens(input: Array[String], expectedOutput: Array[String]) {

    val filter = new FilterNonAlphaNumeric

    val result = filter.processTokens(input)

    result should equal(expectedOutput)
  }
}