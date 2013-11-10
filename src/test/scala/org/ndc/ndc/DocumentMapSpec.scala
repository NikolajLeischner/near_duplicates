package org.ndc.ndc

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

case class Person(var name: String)

class DocumentMapSpec extends FlatSpec with ShouldMatchers {

  val person = Person("")
  person.name = "1"
  
  behavior of "The DocumentMap"

  it should "return only one id for one file" in {
    val map = new DocumentMap

    val file = new java.io.File("some_file")

    val firstId = map.getId(file)
    val secondId = map.getId(file)

    firstId should equal(secondId)
  }

  it should "not return the same id for different files" in {
    val map = new DocumentMap

    val first = new java.io.File("first")
    val second = new java.io.File("second")

    map.getId(first) should not equal (map.getId(second))
  }

  it should "memorize file paths" in {
    val map = new DocumentMap

    val path = "file_path"
    val file = new java.io.File(path)

    val id = map.getId(file)

    map.getFileForId(id).getAbsolutePath() should equal(file.getAbsolutePath())
  }

  it should "throw an IllegalStateException for unknown ids" in {
    val map = new DocumentMap

    val unknownId = 0
    intercept[IllegalStateException] {
      map.getFileForId(unknownId)
    }
  }

}