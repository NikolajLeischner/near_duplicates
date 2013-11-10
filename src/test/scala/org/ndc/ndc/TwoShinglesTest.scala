package org.ndc.ndc

import org.scalatest.matchers.ShouldMatchers
import org.scalatest.testng.TestNGSuite
import org.testng.Assert._
import org.testng.annotations.Test
import org.testng.annotations.DataProvider

class TwoShinglesTest extends TestNGSuite with ShouldMatchers {

  @DataProvider
  def provideExpectedShinglesForTokens() = {
    Array(
      Array[Object](Array("a"), Array("a")),
      Array[Object](Array("a", "b"), Array("ab")),
      Array[Object](Array("a", "b", "c"), Array("ab", "bc")),
      Array[Object](Array("a", "b", "c", "d"), Array("ab", "bc", "cd")),
      Array[Object](Array("a", "b", "c", "d", "e"), Array("ab", "bc", "cd", "de")))
  }

  @Test(dataProvider = "provideExpectedShinglesForTokens")
  def testGeneratesTheExpectedHashes(tokens: Array[String], expectedShingles: Array[String]) {
    val shingles = new TwoShingles

    shingles.fromTokens(tokens) should equal(expectedShingles)
  }  
  
}