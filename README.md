# TwitterMultiApp
A simple program that can post to a user's timeline and retrieve timeline posts.

# Getting Started
Before you start, make sure you have done these few things:
* Have Java 8 installed on your computer
* Go into src/twitter4j.properties and fill in the blanks with your respective consumer key/secret and access token/secret

# Running The Program
To run the program, follow these steps:
1. Open command line and navigate to the src folder
2. Run "javac -cp ../twitter4j-core-4.0.4.jar Twitter_Main.java Twitter_Publish.java Twitter_Retrieve.java" to compile the java files with twitter4j library
3. Run "jar cfm Twitter_Main.jar META-INF/MANIFEST.MF \*.class" to create a jar file named "Twitter_Main.jar"
4. Run "java -jar Twitter_Main.jar" to execute the jar file
