# PULSE Mock Directory Services

Pulse Mock Directory Services

# Installation instructions

## Clone the repository

```sh
$ git clone https://github.com/pulse/mock.git
```

## Build the jar file

### Command Line
1. Go to mock/
2. Run `gradlew build` on the command line

### Eclipse
1. Right click the mock project -> Gradle (STS) -> Refresh Dependencies
2. Right click the mock project -> Run As -> Gradle (STS) Build

## Deploy the Web App

Run the command: `gradlew bootRun`

## Change the "Active Organizations"

While the mock service is running via `bootRun`, you can change the status of, or parameters for, the organizations in the system by navigating to `mock/build/resources/main/` and editing the file named `organizations.xml`.
