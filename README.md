# UML Fertilizer

## What is it?

UML Fertilizer is a tool that allows you to convert a Java project into a .png image out of its directory. How, you may ask? Well, this tool depends on [PlantUML](https://github.com/plantuml/plantuml) to run, so it transpiles Java code to Plant code. As it depends on PlantUML to create the image, it also depends on [Graphviz](https://www.graphviz.org/), as specified by plantuml [on their site](http://plantuml.com/starting).

## Running

Right now, the only way to run it is with command line as a typical jar:
```java -jar uml-fertilizer-1.2-jar-with-dependencies.jar /path/to/model/```

Running the app will generate both a .png image and a .puml file in the same directory as where the jar is executed on. Note that model should be a directory.

## Download

Either download the .jar from the latest release, or compile the source code and then running the generated jar.
