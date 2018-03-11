# Java 2 Graph

This program generates a control flow graph of a given Java method.

It uses Apache BCEL to parse Java Bytecode files.

Author: Andreas Fuchs

## Setup and Usage

Clone from GitHub repository:

```bash
$ git clone https://github.com/wwu-pi/java2graph
$ cd java2graph
```

Compile the sources:

```bash
$ cd j2g
$ mvn clean compile
```

The Main class requires some arguments:

```bash
-c the full qualified class name
-mn the method name
-ms the method signature
-o the output directory where to generate the graph file to
```

If a class in not part of your JRE, you can specify an additional class path with:

```bash
-cp /extra/class/path
```

For example, to generate the control flow graph for a method `equals` of the `java.lang.String` class that compares itself to another `Object` you can use:

```bash
-c java.lang.String
-mn equals
-ms (Ljava/lang/Object;)Z
-o /path/to/output
```

Note that a method in a class is identified by its method name (e.g. `equals`) and method signature (e.g. `(Ljava/lang/Object;)Z` for a method with one argument of type `java.lang.Object` and a boolean (`Z`) return type).

You can find the method structure with `javap`:

For example, to find it for all `java.lang.String` methods:

```bash
javap -v -c java.lang.String
```

## Run the Program

In order to run the program, you must be in the `j2g` folder.

You can run the program with the following command:

```bash
$ mvn exec:java -Dexec.mainClass="j2g.Main" -Dexec.args="-c your.Class -mn yourMethodName -ms ()V -o /your/output/directory"
```

For example, to generate an output to `C:/_J2G/output` for the method `equals` with the signature `(Ljava/lang/Object;)Z` of the class `java.lang.String`, you run:

```bash
$ mvn exec:java -Dexec.mainClass="j2g.Main" -Dexec.args="-c java.lang.String -mn equals -ms (Ljava/lang/Object;)Z -o C:/_J2G/output"
```
