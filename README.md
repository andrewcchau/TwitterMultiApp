# TwitterMultiApp
A simple program that can post to a user's timeline and retrieve timeline posts.

# Getting Started
Before you start, make sure you have done these few things:
* Have Java 8 installed on your computer
* Have Maven installed on your computer
* A working internet connection


# Running The Program
To run the program, follow these steps:
1. Open command line and navigate to the folder where you have cloned this repo (you should see "pom.xml", this README, and a "twitter-example.yml" file along with some folders)
2. Run ```mvn clean install``` to compile the project and package it into a jar
3. Make a copy of "twitter4j-example.yml". Renamed it "twitter4j.yml" and fill the file in with your consumer key/secret and access token/secret
5. Run ```java -jar target/TwitterMultiApp-1.0.0-SNAPSHOT.jar server twitter4j.yml``` to run the program

## Posting to Twitter
To post to twitter using this program, follow these steps:
1. Make sure the program is running first (see: Running The Program above)
2. Open a program that allows sending of form-urlencoded data such as Postman
3. Create a field named "message" and enter your desired post/message
4. Send the form data to http://localhost:8080/api/1.0/twitter/tweet

## Viewing Twitter Timeline
To view your home timeline, ensure the program is running (see: Running The Program above) and go to http://localhost:8080/api/1.0/twitter/timeline

# Unit Testing
To execute Unit Testing of the program, follow these steps:
1. Follow step 1 of Running The Program above
2. Run ```mvn clean test jacoco:report``` to run unit tests and create a code coverage report
3. To view the code coverage report, open the index.html file located at /target/site/jacoco with your favorite browser