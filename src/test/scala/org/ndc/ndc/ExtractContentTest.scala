package org.ndc.ndc

import scala.io.Source
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.testng.TestNGSuite
import org.testng.Assert._
import org.testng.annotations.Test
import org.testng.annotations.DataProvider

case class XmlResource(resourcePath: String, expectedContent: String) {
  def getSource: Source = Source.fromURL(getClass.getResource(resourcePath))
}

class ExtractContentTest extends TestNGSuite with ShouldMatchers {

  @DataProvider
  def xmlSources() = {
    Array(Array[Object](XmlResource("/empty_document.xml", "")),
      Array[Object](XmlResource("/empty_text.xml", "")),
      Array[Object](XmlResource("/empty.xml", "")),
      Array[Object](XmlResource("/text.xml", "word secondWord 00. .? ! third")))
  }

  @Test(dataProvider = "xmlSources")
  def returnOnlyTextTagFromXmlDocument(resource: XmlResource) {
    val extract = new ExtractContent
    val result = extract.textFromSource(resource.getSource)

    result should equal(resource.expectedContent)
  }

  @DataProvider
  def invalidXml() = {
    Array(Array[Object]("/invalid_xml/empty.xml"), Array[Object]("/invalid_xml/missing_tag.xml"))
  }

  @Test(dataProvider = "invalidXml")
  def shouldThrowForNonXmlInput(url: String) {
    val extract = new ExtractContent
    val source = Source.fromURL(getClass.getResource(url))

    evaluating { extract.textFromSource(source) } should produce[IllegalStateException]
  }

}