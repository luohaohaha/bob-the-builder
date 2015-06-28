#How to build bob-the-builder
# Introduction #

The chore of setting up a plugin project to build is quite [daunting](http://zeroturnaround.com/articles/building-eclipse-plug-ins-with-maven-3-and-tycho/). This is where [Tycho](http://www.eclipse.org/tycho/) comes to the rescue.

bob-the-builder leverages tycho to build the artifacts.

# Build steps with [maven3](http://maven.apache.org/) #

  1. [check out the project](http://code.google.com/a/eclipselabs.org/p/bob-the-builder/source/checkout)
  1. `cd bob-the-builder`
  1. `mvn clean install`
  1. `cp BobTheBuilder/target/BobTheBuilder-x.y.z.snapshot.jar <eclipse-installation>/dropins/`
  1. restart Eclipse