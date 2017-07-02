# Smartcity - Smarthouse
<img src="https://cloud.githubusercontent.com/assets/15971916/24823370/5f96f4fa-1c06-11e7-9784-0089af77932f.png" align="left" hspace="10" vspace="35" width="300">

This project offers a smart-house framework, allowing comfortable development of applications utilizing existing sensors within the smart-house to assess and manipulate the state of the house.

It is comprised of 3 main components: the sensors in the house (or their simulation), the system managing those sensors, and your application (and others) influencing the house at all times.

The system acts as a mediator between the sensors and the applications. The sensors send data to the system and the system notifies the applications that a new data has been received.

In order to efficiently communicate the information between those components, We've implemented an **OS-like system** that runs a local sensors-server, contains an [event driven file-system](https://github.com/TechnionYP5777/Smartcity-Smarthouse/wiki/The-Smathouse's-File-System) and an access control mechanism.

Any Applications and sensors that are developed by 3rd party developers, must implement the platform's API.

For more information, go to our [wiki pages](https://github.com/TechnionYP5777/Smartcity-Smarthouse/wiki).

## Development Status
[![Build Status](https://travis-ci.org/TechnionYP5777/Smartcity-Smarthouse.svg?branch=master)](https://travis-ci.org/TechnionYP5777/Smartcity-Smarthouse)
[![codecov](https://codecov.io/gh/TechnionYP5777/Smartcity-Smarthouse/branch/master/graph/badge.svg)](https://codecov.io/gh/TechnionYP5777/Smartcity-Smarthouse)
[![Awesomeness](https://img.shields.io/badge/awesomeness-100%25-blue.svg)](https://shields.io/)

## Users Reviews
Please feel free to leave a review of your installation experience in our Wiki. .<br>
If your are reviewing us during 2<sup>nd</sup>-3<sup>rd</sup> of July 2017 between 12:30-14:30 GMT+3, you can also receive live support from our team - just open an issue and we'll try to assist you. For more information about how to submit reviews and issues please look into [the following page](https://github.com/TechnionYP5777/Smartcity-Smarthouse/wiki/Users-Reviews-Related-Information).

## Contributions
The project is developed using the `Java` programming language. To run the code you will need JDK 8 and the JavaFX library.


For a better understanding of the project structure, please read the [System Overview](https://github.com/TechnionYP5777/Smartcity-Smarthouse/wiki/The-Smart-House-System-Overview) wiki page, which presents the different parts of the project.

## Installation
Please read our [Installation Guide](https://github.com/TechnionYP5777/Smartcity-Smarthouse/wiki/Installation-Guide). But before, make sure you have installed the required run time environment and libraries.
- Working on **Windows**?

  Simply go to the Oracle official download pages, and download any JDK 8 release installation.
- Working on **Linux**?

  If installing using the `apt-get` program, run the following commands:
  
  `sudo apt-get install openjdk-8-jdk`
  
  `sudo apt-get install openjdk-8-jre`
  
  
  And the following command to install the JavaFX library:
  
  `sudo apt-get install openjfx`
  
  
  These three commands will install the java development kit, java runtime environment and the JavaFX library. Another option would be to go to the official Oracle download page, download the java binaries and do the installation manually.

## Building the application with Maven
From the root directory, run
`mvn clean package -DskipTests`

The following jar files will be created in the `target` folder:
- `System-1.01-jar-with-dependencies.jar` - the smarthouse system
- `Sos-1.01-jar-with-dependencies.jar` - the sos application runner
- `Stove-1.01-jar-with-dependencies.jar` - the stove application runner
- `Vitals-1.01-jar-with-dependencies.jar` - the vitals application runner

You can run any of those jar files with the command:
`java -jar <JAR_FILE_NAME>`

