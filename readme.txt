This is a simplistic near duplicate detection algorithm, as an experiment for writing code test-driven in Scala. 

Prerequisites:
  1. You need a JDK (6.x or 7.x).
  2. Get Scala SBT - it should be available at http://www.scala-sbt.org/
  3. Open a command line in this projects root directory, and type "sbt".
  4. From within sbt you can type one of these commands: "compile", "test", "run", "eclipse". The latter will generate an Eclipse project.
  5. If you like to use Eclipse you should get ScalaIDE from http://scala-ide.org/
  6. You should be familiar with adding plugins to Eclipse - when installing the plugin you should also check the ScalaTest plugin. Otherwise you cannot run all the unit tests from within Eclipse.
  
Running the code:
  1. The simplest way is to type "run" from within sbt.
  2. The command line parser is somewhat limited; the flag options need to be triggered like this "-c false", or "--trueDupes true".
  3. The code does very simplistic text extraction, it expects XML content of this kind:
  
    <?xml version="1.0" encoding="UTF-8"?>
    <document>
     <text>
     Content considered for the near-duplicate-detection.
     </text>
    </document>
    
    Whatever is inside the text-tag will be processed, the rest is discarded.
  
  
  4. An Example for running the prototype: "run -i C:\input_dir -o C:\output_dir --clear true -n true". This will overwrite previous output, and will exclude clusters from the output that only contain true duplicates of one document.  
