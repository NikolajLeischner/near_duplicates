package org.ndc.ndc

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers
import java.io.File
import org.apache.commons.io.FileUtils

class DocumentsSourceSpec extends FlatSpec with ShouldMatchers {

  behavior of "The DocumentsSource"

  it should "fail if there is an error accessing the given directory" in {
    val source = new DocumentsSource

    intercept[IllegalStateException] {
      source.setRootDirectory(new File("invalid_path"))
    }
  }

  it should "return paths to the documents in the configured root directory" in {
    val source = new DocumentsSource
    val dir = new File(System.getProperty("java.io.tmpdir") + File.separator + "ndc_input_root")
    dir.mkdir()
    FileUtils.cleanDirectory(dir)

    val filenames = Set("file1", "file2")

    val expectedFiles = for (name <- filenames) yield dir + File.separator + name
    for (file <- expectedFiles) yield new File(file).createNewFile()

    source.setRootDirectory(dir)

    for (file <- source.getFiles()) {
      expectedFiles.contains(file.getAbsolutePath()) should equal(true)
    }
  }
}