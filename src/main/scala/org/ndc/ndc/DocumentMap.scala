package org.ndc.ndc

import scala.collection.mutable.HashMap
import java.io.File

class DocumentMap {
  val idContainer = new IdContainer
  val inverseMap = HashMap[Int, File]()

  def getId(file: File): Int = {
    val path = file.getAbsolutePath()
    val id = idContainer.insertAndGetId(path)
    inverseMap.put(id, file)
    return id
  }

  def getFileForId(id: Int): File = {
    if (!inverseMap.contains(id)) {
      throw new IllegalStateException("Tried to retrieve a path for an unknown document!")
    } else {
      inverseMap.get(id).get
    }
  }
  
  def size = inverseMap.size

}