# A java core based application to search between 2 nodes in an unweighted graph  [![Build Status](https://travis-ci.org/silaev/graphs.svg?branch=master)](https://travis-ci.org/silaev/graphs) 

#### Prerequisites
Java 8+

#### General info
The app provides addVertex, addEdge and getPath for a directed or undirected unweighted graph. 
A vertex, used a parameter to these methods, must have overridden equals and hash code directly 
or within an ancestor except for the Object class.  
  

#### Requirements to consider before using the app.
1. The project should be built by means of Gradle. For that reason, run `gradlew clean build`
2. To publish locally, run `gradlew clean build -x test publishToMavenLocal`
3. Most part of the code is covered with unit tests based on Junit5
