package org.ndc.ndc

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers
import java.io.File

class ClusteringSpec extends FlatSpec with ShouldMatchers {

  behavior of "The Clustering"

  it should "assign equal hashes to the same cluster" in {
    val hash = 1
    val ids = Range(0, 3).toArray
    val idsWithEqualHashes = for (id <- ids) yield DocumentIdAndHash(id, hash)

    val clustering = new Clustering

    val clusters = clustering.processBatch(idsWithEqualHashes)

    clusters.size should equal(1)

    for (document <- clusters.head.documents) {
      ids.contains(document) should equal(true)
    }
  }

  it should "assign hashes that differ in 1 or 2 bits to the same cluster" in {
    val idsWithSimilarHashes = Array(
      DocumentIdAndHash(0, 1),
      DocumentIdAndHash(1, (1 | (1 << 5))),
      DocumentIdAndHash(2, (1 | (1 << 10))),
      DocumentIdAndHash(3, (1 | 2)))

    val clustering = new Clustering

    val clusters = clustering.processBatch(idsWithSimilarHashes)

    clusters.size should equal(1)
    clusters.head.documents.size should equal(idsWithSimilarHashes.size)
  }

  it should "assign hashes that have a large bit-wise distance to different clusters" in {
    val idsWithDistancedHashes = Array(
      DocumentIdAndHash(0, 1),
      DocumentIdAndHash(1, 1),
      DocumentIdAndHash(2, 15),
      DocumentIdAndHash(3, 15))

    val clustering = new Clustering

    val clusters = clustering.processBatch(idsWithDistancedHashes)

    clusters.size should equal(idsWithDistancedHashes.size / 2)
    for (cluster <- clusters) {
      cluster.documents.size should equal(2)
    }
  }
  
  it should "filter out clusters that have only one document" in {
    val twoClusters = Array(
      DocumentIdAndHash(0, 1),
      DocumentIdAndHash(1, 31 << 5),
      DocumentIdAndHash(2, 31 << 5))

    val clustering = new Clustering

    val clusters = clustering.processBatch(twoClusters)

    clusters.size should equal(1)    
  }

}