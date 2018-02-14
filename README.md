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
- After editing file, rebuilding samples is necessary

### Compiling & running samples
- First see **Setup your transport** section

#### Windows via command line
  - **JAVA_HOME** environment variable needs to point java JDK install dir
  - Build all samples: **build_all.bat**
  - Run sample: **run_sample.bat \<folder>**
    - Example: nur_sample_java>run_sample.bat samples\00_Connection
  - Depending on your platform (x86,x64) you may need to change RXTXJAR path in bat scripts. (defaults x64)
  
#### Linux via command line
  - Assumes **java** and **javac** are found in path
  - Assumes **RXTXcomm.jar** found in /usr/share/java/RXTXcomm.jar
  - Assumes **librxtxSerial.so** found in java library path (usually /usr/lib/jni)
    - Depending on distro, you might need to adjust run_sample.sh script with: **-Djava.library.path="/usr/lib/jni/"**
  - Debian based distros you can install RXTX: **sudo apt-get install librxtx-java**
  - Build all samples: **sh build_all.sh**
  - Run sample: **sh run_sample.sh \<folder>**
    - Example: $> sh run_sample.sh samples/00_Connection
  
#### Eclipse IDE
  - Import all projects under samples folder to workspace
  - Might need to fix buildpaths, depending on platform
  - Run sample using right mouse click over project and select: **RunAs -> Java Application**
  - See output from eclipse Console window

### Updating NurApi.jar
You can update to latest NurApi.jar by running script.
Script will download latest NurApi.jar from nur_sdk github to import directory.

- Windows
  - Run in command prompt: UpdateImportsFromGitHub.bat
- Linux
  - Run in shell: sh UpdateImportsFromGitHub.sh
  
### License
All source files in this repository is provided under terms specified in [LICENSE](LICENSE) file.

