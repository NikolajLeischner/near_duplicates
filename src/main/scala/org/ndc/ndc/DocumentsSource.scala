package org.ndc.ndc

import java.io.File

class DocumentsSource {

  var rootDirectory = new File("not set")

  def setRootDirectory(root: File) {
    if (!root.exists()) {
      throw new IllegalStateException("Directory '" + root.getAbsolutePath() + "' does not exist!")
    } else {
      rootDirectory = root
    }
  }

  def getFiles(): Array[File] =
    {
      return rootDirectory.listFiles()
    }

}