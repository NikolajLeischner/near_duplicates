package org.ndc.ndc

class NearDuplicateDetectionPipeline(val config: Configuration, 
  val documents: DocumentsSource, 
  val extractor: ExtractContent, 
  val sourceFromFile: SourceFromFile, 
  val simhash: SimHash, 
  val documentMap: DocumentMap, 
  val featureGenerator: FeatureIdsFromText, 
  val reporter: Reporter, 
  val clustering: Clustering) {

  def initializeFromCommandLine(args: Array[String]) {
    config.initialize(args)
    documents.setRootDirectory(config.input)
    
    reporter.initOutputDirectory(config.output)
    if (config.clearOutput) {
      reporter.clearOutputDirectory
    }    
  }

  private def getEntryForDocument(document: java.io.File): DocumentIdAndFeatures = {
    val source = sourceFromFile.get(document)
    val text = extractor.textFromSource(source)
    val id = documentMap.getId(document)
    val features = featureGenerator.getList(text)
    DocumentIdAndFeatures(id, features)
  }

  def readContent(): Array[DocumentIdAndFeatures] = {
    for (file <- documents.getFiles) yield getEntryForDocument(file)
  }

  def calculateHashes(documentsWithFeatures: Array[DocumentIdAndFeatures]) = {
    for (document <- documentsWithFeatures)
      yield DocumentIdAndHash(document.id, simhash.fromFeatures(document.features))
  }

  def computeClusters(idsWithHashes: Array[DocumentIdAndHash]) = {
    clustering.processBatch(idsWithHashes)
  }
  
  def onlyHasTrueDuplicates(cluster: Cluster) = {
    val texts = for (document <- cluster.documents) 
      yield extractor.textFromSource(sourceFromFile.get(documentMap.getFileForId(document)))
    (texts.size <= 1)
  }
  
  def getTrueDuplicateClusters(clusters: Set[Cluster]) = {
    clusters.filter(onlyHasTrueDuplicates)
  }

  def generateReport(stats: Stats, clusters: Set[Cluster]) {
    reporter.storeReport(stats, clusters)
  }

}