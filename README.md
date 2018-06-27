# TwitterMultiApp
A simple program that can post to a user's timeline and retrieve timeline posts.

# Getting Started
Before you start, make sure you have done these few things:
* Have Java 8 installed on your computer
* Have Maven installed on your computer
* A working internet connection


# Running The Program
To run the program, follow these steps:
1. Open command line and navigate to the folder where you have cloned this repo (you should see "pom.xml", this README, a "twitter4jTEMPLATE.properties" file, and a "twitter_template.yml" file along with some folders)
2. Run ```mvn clean install``` to compile the project and package it into a jar
3. Make a copy of "twitter4jTEMPLATE.properties". Renamed it "twitter4j.properties" and fill the file in with your consumer key/secret and access token/secret
5. Run ```java -jar target/TwitterMultiApp-1.0.0-SNAPSHOT.jar server``` to run the program

## Posting to Twitter
To post to twitter using this program, follow these steps:
1. Make sure the program is running first (see: Running The Program above)
2. Open a new tab in terminal (or new terminal window)
3. Run ```curl --data '(MESSAGE)' http://localhost:8080/api/1.0/twitter/tweet``` where (MESSAGE) is the message / tweet you wish to post

## Viewing Twitter Timeline
Make sure the program is running (see: Running The Program above) before following these steps:
* To view your home timeline, go to http://localhost:8080/api/1.0/twitter/timeline
* To view your latest tweet, go to http://localhost:8080/api/1.0/twitter/tweet


# Unit Testing
To execute Unit Testing of the program, follow these steps:
1. Follow step 1 of Running The Program above
2. Run ```mvn clean test``` to run unit tests and create a code coverage report at /target/jacoco.exec