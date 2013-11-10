package org.ndc.ndc
import scala.collection.mutable.HashMap
import scala.collection.mutable.HashSet

class Clustering {

  private val searchmasks = BitPermutation.masksWithTwoBits ++ BitPermutation.masksWithOneBit ++ Set(0)

  def getSimilarDocuments(map: HashMap[Int, HashSet[Int]], hash: Int) = {
    var similarDocuments = HashSet[Int]()
    for (mask <- searchmasks) {
      val searchKey = hash ^ mask
      similarDocuments = similarDocuments ++ map.get(searchKey).getOrElse(Set())
    }
    similarDocuments.toSet
  }

  def processBatch(idsAndHashes: Array[DocumentIdAndHash]): Set[Cluster] = {
    val hashes = HashMap[Int, HashSet[Int]]()
    for (entry <- idsAndHashes) {
      hashes.getOrElseUpdate(entry.hash, HashSet(entry.documentId)) += entry.documentId
    }

    val result = for (entry <- idsAndHashes.toList)
      yield Cluster(Set.empty ++ getSimilarDocuments(hashes, entry.hash))
    
    result.toSet.filter(_.documents.size > 1)
  }
}