package org.ndc.ndc

import scopt.mutable.OptionParser
import java.io.File

class Configuration(programName: String) {

  var input: File = null
  var output: File = new File("not set")
  var clearOutput: Boolean = false
  var filterTrueDuplicates: Boolean = false
  var filterNearDuplicates: Boolean = false

  def initialize(arguments: Array[String]) {
    val parser = new OptionParser(programName) {
      opt("i", "input", "Location of the input documents", { v: String => input = new File(v) })
      opt("o", "output", "File name for the report", { v: String => output = new File(v) })
      booleanOpt("c", "clear", "File name for the report", { v: Boolean => clearOutput = v })
      booleanOpt("t", "trueDupes", "only show true duplicates", { v: Boolean => filterNearDuplicates = v })
      booleanOpt("n", "nearDupes", "show near duplicates and no true duplicates", { v: Boolean => filterTrueDuplicates = v })
    }

    val parsedSuccessfully = parser.parse(arguments)
    if (!parsedSuccessfully || input == null) {
      throw new IllegalStateException("Missing or wrong configuration parameters!")
    }
    else if (filterTrueDuplicates && filterNearDuplicates) {      
      throw new IllegalStateException("Can only filter near duplicates or true duplicates!")
    }
  }

}