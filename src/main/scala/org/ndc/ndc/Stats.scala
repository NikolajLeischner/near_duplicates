package org.ndc.ndc

class Stats private (val entries: Array[(String, String)]) {

  def this(count: Int, timestamp: String, processingTime: String) = {
    this(Array(("document_count", count.toString),
      ("generated_at", timestamp),
      ("processing_time", processingTime)))
  }

}