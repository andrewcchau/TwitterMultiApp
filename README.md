# TwitterMultiApp
A simple program that can post to a user's timeline and retrieve timeline posts.

# Getting Started
Before you start, make sure you have done these few things:
* Have Java 8 installed on your computer
* Have Maven installed on your computer


# Running The Program
To run the program, follow these steps:
1. Open command line and navigate to the folder where you have cloned this repo (you should see "pom.xml", this README, and the "twitter4jTEMPLATE.properties" file along with some folders)
2. Run "mvn clean" to ensure that you're working with a clean directory
3. Run "mvn compile" to have Maven compile the java files with twitter4j dependencies
4. Make a copy of "twitter4jTEMPLATE.properties". Renamed it "twitter4j.properties" and fill the file in with your consumer key/secret and access token/secret
5. Move the filled in "twitter4j.properties" into the newly created "target" folder
6. Run "java -jar target/TwitterMultiApp-1.0.0-SNAPSHOT-jar-with-dependencies.jar" to execute the jar file
