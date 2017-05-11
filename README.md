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

1. Go to https://github.com/usnistgov/iheos-toolkit2/releases and click on the xdstools-4.3.4.war download.
2. Go to https://tomcat.apache.org/download-70.cgi and download the windows version of tomcat and install. Set up tomcat to run on an unusual port to avoid conflicts with other PULSE applications. 7777 is a good choice. Edit conf/server.xml and replace 8080 with 7777.
3. Go to the installation directory of tomcat and copy the xdstools-4.3.4 into the `webapps` directory.
4. Start tomcat service.
5. Go to localhost:8080/xdstools-4.3.4
6. Follow the instructions at https://github.com/usnistgov/iheos-toolkit2/wiki/installing to set up your external cache and make the popup error message go away.
7. Click conformance tests link at the bottom left corner
8. Click on the Initiating Gateway tab for the Actor to Test
9. Scroll down and hit Initialize Testing Environment
10. Scroll down to Test Data Pattern and copy the patient ID's into a comma seperated list in the patientIds property in the application.properties file
11. (Patient Id's start with P and end before the ^^^ characters)
12. Copy the Assigning Authority into the assigningAuthority property of appplication.properties
13. (Assigning Authority starts after ^^^& and goes until the &ISO)
14. In the endpoints file replace all Query Endpoints with the Cross Community Query Endpoint in the Supporting Environment Configuration section
15. In the endpoints file replace all Retreive Endpoints with the Cross Community Retrieve Endpoint in the Supporting Environment Configuration section
16. Reload Mock and try patient searches



