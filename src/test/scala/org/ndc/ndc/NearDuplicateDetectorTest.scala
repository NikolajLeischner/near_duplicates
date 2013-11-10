package org.ndc.ndc

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers
import java.io.File
import org.apache.commons.io.FileUtils
import scala.io.Source

class NearDuplicateDetectorTest extends FlatSpec with ShouldMatchers {

  behavior of "The NearDuplicateDetector"

  it should "not fail for an empty input directory" in {
    val dir = createEmptyDirectory(System.getProperty("java.io.tmpdir") + File.separator + "ndc_test")
    val inputDir = createEmptyDirectory(dir + File.separator + "input")

    NearDuplicateDetector.main(Array("-i", inputDir.getCanonicalPath,
      "-o", dir.getCanonicalPath))
  }

  it should "actually create a report" in {
    val dir = createEmptyDirectory(System.getProperty("java.io.tmpdir") + File.separator + "ndc_test")
    val inputDir = createEmptyDirectory(dir + File.separator + "input")

    writeResourceToFileInDir("/text.xml", "document.xml", inputDir)
    writeResourceToFileInDir("/text.xml", "second_document.xml", inputDir)
    writeResourceToFileInDir("/text.xml", "another_one.xml", inputDir)

    NearDuplicateDetector.main(Array("-i", inputDir.getCanonicalPath,
      "-o", dir.getCanonicalPath))

    val outputdirpath = dir + File.separator + Reporter.reportDirName + File.separator

    val stats = new File(outputdirpath + Reporter.statsfilename)
    stats.exists() should equal(true)

    val clusters = new File(outputdirpath + File.separator + Reporter.clustersfilename)
    clusters.exists() should equal(true)
  }

  def writeResourceToFileInDir(resourcePath: String, filename: String, dir: File) {
    val source = Source.fromURL(getClass.getResource(resourcePath))
    FileUtils.writeStringToFile(new File(dir + File.separator + filename), source.mkString)
  }

  def createEmptyDirectory(name: String) = {
    val dir = new File(name)
    dir.mkdir()
    FileUtils.cleanDirectory(dir)
    dir
  }

}