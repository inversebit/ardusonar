Ardusonar
=========

A simple sonar for android made with arduino. Let's you know the distance at which the objects in the vicinity of the sonar are.
The sonar is made with an ultrasound sensor which sweeps continuosly clockwise and counterclockwise. The collected data is transmitted via bluetooth to an android tablet or smartphone. There it is represented in a simple, understandable way.

Branching
---------
The _master_ branch will always have a stable version of the project, which compiles and works. Major releases will be marked with a tag.  
The _develop_ branch will have the most recent changes, but may be unstable or even may not compile.


Project structure
---------

This project consists of two programs: 
- An app for android devices
- An arduino file which must be loaded to an arduino development board. The arduino program is contained inside the **arduino folder** in the root of the project.

The necessary hardware for the arduino part to work correctly is listed on the [Arduino hardware](https://github.com/Inversebit/ardusonar/wiki/Arduino-hardware) wiki page. In that same some other details regarding hardware connection are documented.
