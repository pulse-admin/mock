# PULSE Mock Directory Services

Pulse Mock Directory Services

# Installation instructions

## Clone the repository

```sh
$ git clone https://github.com/pulse/mock.git
```

## Build the jar file

#### Command Line
1. Go to mock/
2. Run `gradlew build` on the command line

#### Eclipse 
1. Right click the mock project -> Gradle (STS) -> Refresh Dependencies
2. Right click the mock project -> Run As -> Gradle (STS) Build

## Deploy the Web App

1. On the command line go to mock/build/libs/
2. Run command: `java -jar mock-0.0.1-SNAPSHOT.jar`