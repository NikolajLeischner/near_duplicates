package org.ndc.ndc

import org.scalatest.FlatSpec

class ConfigurationSpec extends FlatSpec {

  behavior of "The Configuration"

  it should "accept an input and output parameter" in
    {
      val config = new Configuration("")
      val input = "some_dir"
      val output = "result"
      config.initialize(Array("-i", input, "-o", output))

      expect(input) {
        config.input.getName()
      }
      expect(output) {
        config.output.getName()
      }
    }

  it should "fail if no input parameter is set" in
    {
      val config = new Configuration("")

      intercept[IllegalStateException] {
        config.initialize(Array("-o", "result.xml"))
      }
    }

  it should "accept a parameter for clearing the output" in {
    val config = new Configuration("")

    config.initialize(Array("-i", "dont-care", "-o", "dont-care", "-c", "true"))

    expect(true) { config.clearOutput }

  }

  it should "accept either a parameter for near duplicate clusters, or for true true duplicates" in {
    var config = new Configuration("")
    intercept[IllegalStateException] {
      config.initialize(Array("-i", "dont-care", "-o", "dont-care", "-t", "true", "-n", "true"))
    }

    config = new Configuration("")
    config.initialize(Array("-i", "dont-care", "-o", "dont-care", "-n", "true"))
    expect(true) { config.filterTrueDuplicates }

    config = new Configuration("")
    config.initialize(Array("-i", "dont-care", "-o", "dont-care", "-t", "true"))
    expect(true) { config.filterNearDuplicates }
  }

}