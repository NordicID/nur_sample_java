# NUR java samples
This repository contains NurApi java samples. 
- [Java docs](https://github.com/NordicID/nur_sdk/tree/master/java)

### Directories
- transports
  - NurApiSerialTransport, RXTX library based transport for NurApi
  - NurApiSocketTransport, TCP/IP transport for NurApi
- samples
  - Set of console examples (see section 'Compiling samples')
- import
  - Contains NurApi.jar (see section 'Updating NurApi.jar')

### Setup your transport
- You need to setup your specific transport in order to run samples
- See file
  - [samples/SamplesCommon/src/com/nordicid/samples/common/SamplesCommon.java](samples/SamplesCommon/src/com/nordicid/samples/common/SamplesCommon.java)

### Compiling samples
Use script to compile all transports and samples:
- Windows
  - JAVA_HOME environment variable needs to point java JDK install dir
  - Run in command prompt: build_all.bat
  - Depending on your platform (x86,x64) you may need to change RXTXJAR in script
- Linux
  - Run in shell: sh build_all.sh
  - Assumes RXTXcomm.jar found in /usr/share/java/RXTXcomm.jar
  - Debian based distros you can install RXTX: sudo apt-get install librxtx-java
- eclipse IDE can be used also compiling
  - Import all projects to workspace
  - Might need to fix buildpaths, depending on platform
  
### Running samples
First see **Setup your transport** section
Use script to run samples:
- Windows
  - JAVA_HOME environment variable needs to point java JDK install dir
  - Run in command prompt: run_sample.bat <folder>
  - Example: nur_sample_java>run_sample.bat samples\00_Connection
  - Depending on your platform (x86,x64) you may need to change RXTXJAR in script
- Linux
  - Run in shell: sh run_sample.sh <folder>
  - Example: $> sh run_sample.sh samples\00_Connection
  - Assumes RXTXcomm.jar found in /usr/share/java/RXTXcomm.jar
  - Assumes librxtxSerial.so found in java library path
- eclipse IDE can be used also running/debugging
  - Import all projects to workspace
  - Might need to fix buildpaths, depending on platform

### Updating NurApi.jar
You can update latest NurApi.jar by running script.
Script will download latest NurApi.jar from nur_sdk github to import directory.

- Windows
  - Run in command prompt: UpdateImportsFromGitHub.bat
- Linux
  - Run in shell: sh UpdateImportsFromGitHub.sh
