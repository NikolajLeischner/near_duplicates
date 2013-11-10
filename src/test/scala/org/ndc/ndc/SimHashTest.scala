package org.ndc.ndc

import org.scalatest.matchers.ShouldMatchers
import org.scalatest.testng.TestNGSuite
import org.testng.Assert._
import org.testng.annotations.Test
import org.testng.annotations.DataProvider

class SimHashTest extends TestNGSuite with ShouldMatchers {

  @DataProvider
  def provideFeaturesAndExpectedHash() = {
    Array(
      Array[Object](Array[Int](), new java.lang.Integer(0)),
      Array[Object](Array(1), new java.lang.Integer(1)),
      Array[Object](Array(1, 1, 1), new java.lang.Integer(1)),
      Array[Object](Array(31, 31, 0), new java.lang.Integer(31)),
      Array[Object](Array(31, 31, ~31, ~31), new java.lang.Integer(0)),
      Array[Object](Array(31, ~31, ~31), new java.lang.Integer(~31)))
  }

  @Test(dataProvider = "provideFeaturesAndExpectedHash")
  def testGeneratesTheExpectedHashes(features: Array[Int], expectedHash: Int) {
    val simhash = new SimHash

    simhash.fromFeatures(features) should equal(expectedHash)
  }
}