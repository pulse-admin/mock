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

## Installation of NIST tool

Go to https://github.com/usnistgov/iheos-toolkit2/releases and click on the xdstools-4.3.4.war download.
Go to https://tomcat.apache.org/download-70.cgi and download the windows version of tomcat and install.
Go to the installation directory of tomcat and copy the xdstools-4.3.4 into the `webapps` directory.
Start tomcat service.
Go to localhost:8080/xdstools-4.3.4
Click conformance tests link at the bottom left corner
Click on the Initiating Gateway tab for the Actor to Test
Scroll down and hit Initialize Testing Environment
Scroll down to Test Data Pattern and copy the patient ID's into a comma seperated list in the patientIds property in the application.properties file
(Patient Id's start with P and end before the ^^^ characters)
Copy the Assigning Authority into the assigningAuthority property of appplication.properties
(Assigning Authority starts after ^^^& and goes until the &ISO)
In the endpoints file replace all Query Endpoints with the Cross Community Query Endpoint in the Supporting Environment Configuration section
In the endpoints file replace all Retreive Endpoints with the Cross Community Retrieve Endpoint in the Supporting Environment Configuration section
Reload Mock and try patient searches



