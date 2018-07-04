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

You will get something like this:

```bash
public static java.lang.String valueOf(java.lang.Object);
    descriptor: (Ljava/lang/Object;)Ljava/lang/String;
    flags: ACC_PUBLIC, ACC_STATIC
    Code:
      stack=1, locals=1, args_size=1
         0: aload_0
         1: ifnonnull     9
         4: ldc           #12                 
         6: goto          13
         9: aload_0
        10: invokevirtual #459                
        13: areturn
      StackMapTable: number_of_entries = 2
        frame_type = 9 /* same */
        frame_type = 67 /* same_locals_1_stack_item */
          stack = [ class java/lang/String ]
      LineNumberTable:
        line 2994: 0
```

Then the method signature of the method named `valueOf` with a single parameter of type `java.lang.Object` is given in `descriptor` with `(Ljava/lang/Object;)Ljava/lang/String;`

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


## Eclipse Project Import

The project can be imported into Eclipse using the following steps:

(1) Clone from repository as explained above.

(2) In Eclipse Menu: `File -> Import...`

(3) In Import Wizard select: `General -> Existing Projects into Workspace` and click `Next`

(4) In Import WIzard `Select root directory`: specify path where you have cloned the repository to (you might have to select option `Search for nested projects`)

(5) Click `Finish`