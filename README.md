Heterogeneous Ensemble Machine Learning Open Classification Kit (HEMLOCK) 

HEMLOCK is a software tool for constructing, evaluating, and applying 
heterogeneous ensemble data models for use in solving supervised machine 
learning problems. Specifically, the main class of problems targeted by HEMLOCK
is the problem of multiple-class classification (also called labeling or 
categorization) of data with continuous or discrete features. HEMLOCK consists 
of various data readers, machine learning algorithms, model combination and 
comparison routines, evaluation methods for model performance testing, and 
interfaces to external, state-of-the-art machine learning software libraries.

# Compiling

In order to compile Hemlock, an Ant build file, `build.xml` is provided.
Provided an Apache Ant framework is installed on the machine, the following
command, executed from the `HEMLOCK` directory, should completely build the
project:  `ant`.  

In order to interface with Weka, weka.jar must be in the class path or in the
`HEMLOCK/tpl` directory at the time of compilation.  You will get a warning
message if weka.jar is not in either of those locations when running the Ant
build file.

# Installation

The project must be built before using as the executables are not distributed.
See the section titled "Compiling" for more information.

In order to interface with Weka, `weka.jar` must be in the class path or in the
`HEMLOCK/tpl` directory while running Hemlock.  If it is not, then any
experiments that request the use of Weka will not be executed and an error
message will be displayed.

# Adding Data Sets

Hemlock can only import one type of data set.  It is a modification of the C45
file format.  Each data set must have a `*.name` file and `*.data` file.  The
first line of the `*.name` file is a space separated list of class labels.
This is followed by an empty line and then one line per attribute where each
line contains `continuous` for continuous attributes or `discrete` followed by
a space separated list of possible values.  Both the names file and the data
file must have the same name and be placed in a folder with the same name.  All
such data directories should be put in a data repository directory.  A data
repository directory is nothing more than a directory which only contains data
directories formatted using the format just described.  An entry in the
`HEMLOCK/.config` file must be added to point to the data repository directory
you have created.  By default the `HEMLOCK/data` directory is already setup as
a data repository so data directories can be immediately dropped in that
location for use by Hemlock.

# Running Hemlock

Use `runHemlock [inputPath] [outPutPath]` to run Hemlock.  The two arguments
are required.  
- `inputPath`: path for experiment file to be run
- `outputPath` directory for result files to be written to

