package org.ndc.ndc

import scala.collection.mutable.HashMap

class IdContainer {
  var nextId = 0
  var idMap = HashMap[String, Int]()

  def insertAndGetId(entry: String): Int = {
    if (idMap.contains(entry)) {
      return idMap.get(entry).get
    } else {
      val id = nextId
      nextId += 1
      idMap.put(entry, id)
      return id
    }
  }

  def getTokens: Set[String] = {
    idMap.keySet.toSet
  }

}