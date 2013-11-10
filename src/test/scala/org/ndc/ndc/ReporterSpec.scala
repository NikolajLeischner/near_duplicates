package org.ndc.ndc

import org.scalatest.FlatSpec
import org.scalatest.mock.JMockCycle
import org.scalatest.matchers.ShouldMatchers
import java.io.File
import org.apache.commons.io.FileUtils
import com.github.tototoshi.csv.CSVReader

class ReporterSpec extends FlatSpec with ShouldMatchers {

  behavior of "The Reporter"

  it should "throw an exception if the output directory cannot be found" in {
    val documents = new DocumentMap
    val reporter = new Reporter(documents)

    intercept[IllegalStateException] {
      reporter.initOutputDirectory(new File("invalid_path"))
    }
  }

  it should "clear the output directory" in {
    val documents = new DocumentMap
    val reporter = new Reporter(documents)

    val dir = createTempDir("report_dir")
    reporter.initOutputDirectory(dir)
    createFileInOutputDir(reporter)
    reporter.clearOutputDirectory()

    val files = FileUtils.listFiles(reporter.outputDirectory, null, false)

    files.size() should equal(0)
  }

  it should "throw an exception if the output directory is not empty when generating the report" in {
    val documents = new DocumentMap
    val reporter = new Reporter(documents)

    val dir = createTempDir("report_dir")
    reporter.initOutputDirectory(dir)
    createFileInOutputDir(reporter)

    intercept[IllegalStateException] {
      reporter.storeReport(new Stats(0, "", ""), Set[Cluster]())
    }
  }

  it should "use the provided statistics to write the header of the report" in {
    val documents = new DocumentMap
    val reporter = createAndInitReporterWithTempDir(documents)

    val stats = new Stats(3, "01-01-2014 06:45:01", "0:23:56")
    reporter.storeReport(stats, Set[Cluster]())

    val content = getContentFromCsvFile(reporter.outputDirectory + File.separator + Reporter.statsfilename)

    content.length should equal(1)

    val line = content.head
    for (entry <- stats.entries) {
      line.contains(entry._1) should equal(true)
      line.get(entry._1).get should equal(entry._2.toString)
    }
  }

  it should "use the provided clusters to generate the report" in {
    val documents = new DocumentMap
    val reporter = createAndInitReporterWithTempDir(documents)

    val files = Set(new File("1.xml"), new File("2.txt"), new File("no_ending"))
    val ids = for (file <- files) yield documents.getId(file)
    val clusters = Set(new Cluster(ids), new Cluster(ids))
    reporter.storeReport(new Stats(0, "", ""), clusters)

    val content = getContentFromCsvFile(reporter.outputDirectory + File.separator + Reporter.clustersfilename)

    content.length should equal(files.size * clusters.size)

    val filenames = for (file <- files) yield file.getName()

    for (line <- content) {
      line.contains(Reporter.clustercolumn) should equal(true)
      line.contains(Reporter.filenamecolumn) should equal(true)
      filenames.contains(line.get(Reporter.filenamecolumn).get) should equal(true)
    }
  }

  def createTempDir(name: String): File = {
    val dir = new File(System.getProperty("java.io.tmpdir") + File.separator + "report_dir")
    dir.mkdir()
    dir
  }

  def createAndInitReporterWithTempDir(documents: DocumentMap) = {
    val reporter = new Reporter(documents)
    val dir = createTempDir("report_dir")
    reporter.initOutputDirectory(dir)
    reporter.clearOutputDirectory()
    reporter
  }

  def createFileInOutputDir(reporter: Reporter) {
    val filename = reporter.outputDirectory + File.separator + "file"
    new File(filename).createNewFile()
  }

  def getContentFromCsvFile(filename: String) = {
    val file = new File(filename)
    val reader = CSVReader.open(file)
    val content = reader.allWithHeaders
    reader.close()
    content
  }

}