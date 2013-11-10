package org.ndc.ndc

import java.io.File
import org.apache.commons.io.FileUtils
import com.github.tototoshi.csv.CSVWriter

object Reporter {
  val statsfilename = "stats.csv"
  val clustersfilename = "clusters.csv"
  val clustercolumn = "cluster_id"
  val filenamecolumn = "file_name"  
  val reportDirName = "ndc_report"
}

class Reporter(documents: DocumentMap) {

  var outputDirectory = new File("not set")

  def initOutputDirectory(dir: File) {
    if (!dir.exists()) {
      throw new IllegalStateException("Invalid output directory!")
    } else {
      outputDirectory = new File(dir + File.separator + Reporter.reportDirName)
      outputDirectory.mkdir()
    }
  }

  def clearOutputDirectory() {
    FileUtils.cleanDirectory(outputDirectory)
  }

  private def storeStats(stats: Stats, file: File) {
    val writer = CSVWriter.open(file)
    writer.writeRow(for (entry <- stats.entries) yield entry._1)
    writer.writeRow(for (entry <- stats.entries) yield entry._2)
    writer.close()
  }

  private def storeClusters(clusters: Set[Cluster], file: File) {
    val writer = CSVWriter.open(file)
    writer.writeRow(List(Reporter.clustercolumn, Reporter.filenamecolumn))
    var clusterId = 0
    for (cluster <- clusters) {
      clusterId += 1
      for (id <- cluster.documents) {
        writer.writeRow(List(clusterId, documents.getFileForId(id)))
      }
    }
    writer.close()
  }

  def storeReport(stats: Stats, clusters: Set[Cluster]) {
    if (FileUtils.listFiles(outputDirectory, null, false).size() == 0) {
      val statsfile = new File(outputDirectory + File.separator + Reporter.statsfilename)
      storeStats(stats, statsfile)

      val clustersfile = new File(outputDirectory + File.separator + Reporter.clustersfilename)
      storeClusters(clusters, clustersfile)
    } else {
      throw new IllegalStateException("Output directory is not empty!")
    }
  }
}