package org.ndc.ndc

import scala.xml._
import scala.io.Source

class ExtractContent {

  def textFromSource(source: Source): String =
    {
      var result = ""
      try {
        val content = XML.loadString(source.mkString)
        for (entry <- content \\ "document") {
          result += (entry \\ "text").text
        }
      } catch {
        case e: Exception => throw new IllegalStateException("Failed to parse " + source)
      }
      return result
    }
}