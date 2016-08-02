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

### Command Line
Run the command: `gradlew bootRun`

### Eclipse
Right click 'MockApplication.java' file in workspace explorer -> Run as... -> Java Application

## Change the time interval for Mock HIE SOAP querying
1. In `src/main/resources` change the variable value for 'patientSearchInterval' (note that the value is in seconds)
2. Restart the application

## Change the "Active Organizations"

While the mock service is running via `bootRun`, you can change the status of, or parameters for, the organizations in the system by navigating to `mock/build/resources/main/` and editing the file named `organizations.xml`.
