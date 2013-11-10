package org.ndc.ndc
import com.github.nscala_time.time.Imports._

object NearDuplicateDetector {
  def main(args: Array[String]) {
    
    val start = DateTime.now
    
    val programName = "NearDuplicateDetector"
    val config = new Configuration(programName)
    val documents = new DocumentsSource
    val extractor = new ExtractContent
    val sourceFromFile = new SourceFromFile
    val simhash = new SimHash
    val documentMap = new DocumentMap
    val featureGenerator = new FeatureIdsFromText
    val clustering = new Clustering
    val reporter = new Reporter(documentMap)

    val pipeline = new NearDuplicateDetectionPipeline(config, documents, extractor, sourceFromFile,
      simhash, documentMap, featureGenerator, reporter, clustering)

    pipeline.initializeFromCommandLine(args)
    val content = pipeline.readContent
    val hashes = pipeline.calculateHashes(content)
    val clusters = pipeline.computeClusters(hashes)
    val duplicates = pipeline.getTrueDuplicateClusters(clusters)

    val duration = start to DateTime.now

    var output = clusters
    if (config.filterNearDuplicates) {
      output = duplicates
    } else if (config.filterTrueDuplicates) {
      output = clusters -- duplicates
    }
    
    pipeline.generateReport(new Stats(documentMap.size, start.toString, 
        duration.toDuration.getMillis.toString + "ms"), output)
  }
}
