package org.ndc.ndc

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

class IdContainerSpec extends FlatSpec with ShouldMatchers {

  behavior of "The IdContainer"

  it should "assign different integers to different tokens" in {
    val idContainer = new IdContainer

    val tokens = Set("first", "second")

    val result = for (t <- tokens) yield idContainer.insertAndGetId(t)

    expect(tokens.size) {
      result.size
    }
  }

  it should "return the same integer when adding a token twice" in {
    val idContainer = new IdContainer

    val token = "token"
    val firstId = idContainer.insertAndGetId(token)
    val secondId = idContainer.insertAndGetId(token)

    firstId should equal(secondId)
  }

  it should "memorize the original tokens" in {
    val idContainer = new IdContainer

    val token = "token"
    idContainer.insertAndGetId(token)

    idContainer.getTokens should equal(Set(token))
  }

}