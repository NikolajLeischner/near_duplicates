package org.ndc.ndc

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.mock.JMockCycle
import org.jmock.Expectations._
import java.io.File
import scala.io.Source

class NearDuplicateDetectionPipelineSpec extends FlatSpec with ShouldMatchers {

  def getAppWithMocks(cycle: JMockCycle) = {
    import cycle._
    val config = mock[Configuration]
    val documents = mock[DocumentsSource]
    val extractor = mock[ExtractContent]
    val sourceFromFile = mock[SourceFromFile]
    val simhash = mock[SimHash]
    val documentMap = mock[DocumentMap]
    val featureGenerator = mock[FeatureIdsFromText]
    val reporter = mock[Reporter]
    val clustering = mock[Clustering]
    new NearDuplicateDetectionPipeline(config, documents, extractor, sourceFromFile, simhash,
      documentMap, featureGenerator, reporter, clustering)
  }

  behavior of "The NearDuplicateDetectionPipeline"

  it should "let a Configuration do the command line parsing" in
    {
      val cycle = new JMockCycle
      import cycle._
      val engine = getAppWithMocks(cycle)
      import engine._

      val args = Array("some_parameter", "another_one")

      expecting { e =>
        import e._
        oneOf(config).initialize(args)
        allowing(config).input
        allowing(config).output
        allowing(config).clearOutput
        allowing(config).filterNearDuplicates
        allowing(config).filterTrueDuplicates
        ignoring(documents)
        ignoring(reporter)
      }

      whenExecuting {
        engine.initializeFromCommandLine(args)
      }
    }

  it should "initialize a DocumentsSource and Reporter from its Configuration" in
    {
      val cycle = new JMockCycle
      import cycle._
      val engine = getAppWithMocks(cycle)
      import engine._

      val file = new File("")

      expecting { e =>
        import e._
        allowing(config).input
        will(returnValue(file))
        allowing(config).output
        will(returnValue(file))
        allowing(config).clearOutput
        allowing(config).filterNearDuplicates
        allowing(config).filterTrueDuplicates
        allowing(config).initialize(withArg(any(classOf[Array[String]])))
        oneOf(documents).setRootDirectory(file)
        oneOf(reporter).initOutputDirectory(file)
      }

      whenExecuting {
        engine.initializeFromCommandLine(Array())
      }
    }

  it should "extract the content of all docouments" in
    {
      val cycle = new JMockCycle
      import cycle._
      val engine = getAppWithMocks(cycle)
      import engine._

      val files = Array(new File("some file"), new File("other file"))

      val docId = 1
      val features = Array(0, 1, 2)

      expecting { e =>
        import e._
        oneOf(documents).getFiles()
        will(returnValue(files))

        for (file <- files) {
          oneOf(sourceFromFile).get(file)
          oneOf(extractor).textFromSource(withArg(any(classOf[Source])))
          oneOf(documentMap).getId(file)
          will(returnValue(docId))
          oneOf(featureGenerator).getList(withArg(any(classOf[String])))
          will(returnValue(features))
        }
      }

      var result = Array[DocumentIdAndFeatures]()
      whenExecuting {
        result = engine.readContent
      }

      result.size should equal(files.size)

      for (entry <- result) {
        entry should equal(DocumentIdAndFeatures(docId, features))
      }
    }

  it should "compute hashes for all documents" in {
    val cycle = new JMockCycle
    import cycle._
    val engine = getAppWithMocks(cycle)
    import engine._

    val features = Array(DocumentIdAndFeatures(0, Array(0, 1)), DocumentIdAndFeatures(2, Array(4)))

    expecting { e =>
      import e._
      for (document <- features) {
        oneOf(simhash).fromFeatures(document.features)
      }
    }

    whenExecuting {
      engine.calculateHashes(features)
    }
  }

  it should "create clusters from the document hashes" in {
    val cycle = new JMockCycle
    import cycle._
    val engine = getAppWithMocks(cycle)
    import engine._

    val idsAndHashes = Array[DocumentIdAndHash]()

    expecting { e =>
      import e._
      oneOf(clustering).processBatch(idsAndHashes)
    }

    whenExecuting {
      engine.computeClusters(idsAndHashes)
    }
  }

  it should "generate a report of the result of the clustering" in {
    val cycle = new JMockCycle
    import cycle._
    val engine = getAppWithMocks(cycle)
    import engine._

    val stats = mock[Stats]
    val clusters = Set[Cluster]()

    expecting { e =>
      import e._
      oneOf(reporter).storeReport(stats, clusters)
    }

    whenExecuting {
      engine.generateReport(stats, clusters)
    }
  }
  
  it should "filter true duplicates" in {
    val cycle = new JMockCycle
    import cycle._
    val engine = getAppWithMocks(cycle)
    import engine._

    val docIds = Set(1, 2, 3)
    val stats = mock[Stats]
    val file = mock[File]
    val source = mock[Source]
    val clusters = Set[Cluster](Cluster(docIds))

    expecting { e =>
      import e._
      exactly(docIds.size).of(documentMap).getFileForId(withArg(any(classOf[Int])))
      will(returnValue(file))
      exactly(docIds.size).of(sourceFromFile).get(file)
      exactly(docIds.size).of(extractor).textFromSource(withArg(any(classOf[Source])))
      will(returnValue("content"))
    }

    whenExecuting {
      engine.getTrueDuplicateClusters(clusters)
    }
    
  }
  
}