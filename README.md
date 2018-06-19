# TwitterMultiApp
A simple program that can post to a user's timeline and retrieve timeline posts.

# Getting Started
Before you start, make sure you have done these few things:
* Have Java 8 installed on your computer
* Have Maven installed on your computer


# Running The Program
To run the program, follow these steps:
1. Open command line and navigate to the folder where you have cloned this repo (you should see "pom.xml", this README, a "twitter4jTEMPLATE.properties" file, and a "twitter_template.yml" file along with some folders)
2. Run "mvn clean install" to compile the project and package it into a jar
3. Make a copy of "twitter4jTEMPLATE.properties". Renamed it "twitter4j.properties" and fill the file in with your consumer key/secret and access token/secret
5. Run "java -jar target/TwitterMultiApp-1.0.0-SNAPSHOT.jar server twitter_template.yml" to run the program

## Posting to Twitter
To post to twitter using this program, follow these steps:
1. Make sure the program is running first (see: Running The Program above)
2. Open a new tab in terminal (or new terminal window)
3. Type "curl -H "Content-Type: application/json" --data '(MESSAGE)' http://localhost:8080/api/1.0/twitter/tweet" where (MESSAGE) is the message / tweet you wish to post

## Viewing Twitter Timeline
* To view your home timeline, follow these steps:
1. Make sure the program is running (see: Running The Program above)
2. Go to your favorite web browser and go to "http://localhost:8080/api/1.0/twitter/timeline"

* To view your latest tweet, follow these steps:
1. Make sure the program is running (see: Running The Program above)
2. Go to your favorite web browser and go to "http://localhost:8080/api/1.0/twitter/tweet"
