# Smartcity - Smarthouse
<img src="https://cloud.githubusercontent.com/assets/15971916/24823370/5f96f4fa-1c06-11e7-9784-0089af77932f.png" align="left" hspace="10" vspace="9" width="300">

An autonomous system which receives data from house sensors, stores it in a database and runs applications that operate based on this data.

The system keeps track of the well being and day to day activity of the user. It monitors the user's state via the sensors and analyzes his or her behavior via the applications. The system can send alerts and notifications, and on request from an application through the defined API, will contact the appropriate contacts (according to their emergency-level).

The system acts as a mediator between the sensors and the applications. The sensors send data to the system and the system notifies the applications that a new data has been received.

Applications and sensors that are developed by 3rd party developers, must implement the platform's API.

## Development Status
[![Build Status](https://travis-ci.org/TechnionYP5777/Smartcity-Smarthouse.svg?branch=master)](https://travis-ci.org/TechnionYP5777/Smartcity-Smarthouse)
[![codecov](https://codecov.io/gh/TechnionYP5777/Smartcity-Smarthouse/branch/master/graph/badge.svg)](https://codecov.io/gh/TechnionYP5777/Smartcity-Smarthouse)
[![Awesomeness](https://img.shields.io/badge/awesomeness-100%25-blue.svg)](https://shields.io/)

## Contributions
The project is developed using the `Java` programming language. To run the code you will need JDK 8 and the JavaFX library. For a better understanding of the project structure, please read the [System Overview](https://github.com/TechnionYP5777/Smartcity-Smarthouse/wiki/The-Smart-House-System-Overview) wiki page, which presents the different parts of the project.

## Windows
To install the required run time environment and libraries, simply go to the Oracle official download pages, and download any JDK 8 release installation.

## Linux
If installing using the `apt-get` program, run the following commands:

`sudo apt-get install openjdk-8-jdk`

`sudo apt-get install openjdk-8-jre`

And the following command to install the JavaFX library:

`sudo apt-get install openjfx`

These three commands will install the java development kit, java runtime environment and the JavaFX library. Another option would be to go to the official Oracle download page, download the java binaries and do the installation manually.

## Running the application from the command line
The application can be launched from the command line with the following commands:
- `mvn exec:java@main` - launches the system
- `mvn exec:java@sensor_sim_sos` - launches the SOS sensor
- `mvn exec:java@sensor_sim_stove` - launches the Stove sensor
- `mvn exec:java@sensor_sim_vitals` - launches the Vitals sensor

## About
Icons by www.icons8.com
