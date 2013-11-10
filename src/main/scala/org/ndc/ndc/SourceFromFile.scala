package org.ndc.ndc

class SourceFromFile {
  def get(file: java.io.File) = scala.io.Source.fromFile(file)
}