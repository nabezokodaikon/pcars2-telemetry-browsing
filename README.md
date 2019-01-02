# Development is closed

# Telemetry browsing for Project Cars 2
* [Source code](https://github.com/nabezokodaikon/pcars2-telemetry-browsing)
* [Change log](./doc/changelog.md)


## Overview
This application is a server that converts UDP data sent by PROJECT CARS 2 to HTTP.

By accessing this server with WebBrowser, you can browse the telemetry etc of PROJECT CARS 2.

This application can only be accessed within the same LAN as the UDP data being transmitted by PROJECT CARS 2.


## Screen shot
### MENU
![MENU](./screenshot/menu.png)

### DEFAULT View
![SIMPLE](./screenshot/default.png)

### ENGINE View
![ENGINE](./screenshot/engine.png)

### DAMAGE View
![DAMAGE](./screenshot/damage.png)

### TYRE View
TODO

### TIME View
![TIME](./screenshot/time.png)

### RANK View
TODO

### BUTTON BOX View
![BUTTON BOX](./screenshot/button-box.png)

### BUTTON BOX CUSTOMIZE View
![BUTTON BOX CUSTOMIZE](./screenshot/button-box-customize.png)


## Dependencies
* Java Runtime Environment(JRE)


## Usage
### Settings
#### Application setting
* Edit the following items in `application.conf`
  * Change `ip-address` to the IP address of the PC running this application.
  * Change to the free port number of the PC that will run `port`.(Please change when it does not work with the default `9000`.)
#### PROJECT CARS 2 setting
1. Select `OPTIONS > SYSTEM`
1. Set as follows.
  * Shared Memory: No
  * UDP Frequency: 4 (Around 4 to 6 is appropriate.)
  * UDP Protocol Version: Project CARS 2

### iPhone or iPad Web page to Home short cut
* From the status bar of Safari, tap Add to Home Screen to create a shortcut on the home screen and display it in full screen.

### iPhone fix it Landscape
[See here.](https://support.apple.com/en-us/HT202612)


## Install
* Expand `pcars2-telemetry-browsing.zip` file.


## Uninstall
* Delete `pcars2-telemetry-browsing` directory.


### Run
#### For Windows
* Please run `start-for-windows.bat` and access to the displayed address by WebBrowser of PC, mobile phone, android or IOS device in LAN.


## Troubleshooting
* If you can not connect to the server, check the PC's network interface.  
PROECT CARS2 connects to the network interface found at the beginning of the PC in the LAN.


## Please note for BUTTON BOX
* BUTTON BOX works only when you play PROJECT CARS 2 on a PC running this application.
* BUTTON BOX is just sending key inputs to the application window.
Therefore, if the window except PROJECT CARS 2 is active, send the key to that active window.
(For example, if the notepad is active, it will be typed in the notepad.)


## CREDITS
### PROJECT CARS 2
* <https://www.projectcarsgame.com/>
### Project CARS Official Forum 
* <http://forum.projectcarsgame.com/forum.php>
### CrewChief
* CrewChief <http://thecrewchief.org>
### scala
* Copyright (c) 2002-2017 EPFL
* Copyright (c) 2011-2017 Lightbend, Inc.
### akka
* Copyright (C) 2009-2017 Lightbend Inc. <http://www.lightbend.com>
### logback
* Copyright (C) 1999-2015, QOS.ch. All rights reserved.
### scala-logging
* Copyright 2014 Typesafe Inc. <http://www.typesafe.com>
### react
* Copyright (c) 2013-present, Facebook, Inc.
### redux
* Copyright (c) 2015-present Dan Abramov
### MapDB
* Apache LicenseVersion 2.0, January 2004
