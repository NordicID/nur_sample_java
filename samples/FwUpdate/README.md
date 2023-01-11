# NUR Firmware update
Updating the firmware of the Nordic ID device is easy using the NiduProg command line runnable jar. Alternatively, the update code can be embedded in a java application using `NiduLib.jar` Nordic ID maintains a zip file containing all the latest firmware for the RFID readers. NiduProg can download the latest firmware from the NordicID server and update if necessary.

### NiduProg.jar (Runnable jar) command line usage

-  `java -jar niduprog.jar <transport type> <addr> <port> <zip path> <flags>`
(Load firmware zip from specified path.) Download locally here: [NidLatestFW.zip](https://raw.githubusercontent.com/NordicID/nur_firmware/master/zip/NIDLatestFW.zip)

-  `java -jar niduprog.jar <transport type> <addr> <port> NIDSERVER <flags>`
(Load latest firmware zip from NordicID server. (Recommended method. Includes latest updates)
  
-  `java -jar niduprog.jar <transport type> <addr> <port> <URL> <flags>`
(Load latest firmware zip from URL)

**Transport types**
 - `TCP` Socket connection <addr> = hostname or IP address <port> = target port number.
 Example: `java -jar niduprog.jar TCP 192.168.10.2 4333 NIDSERVER`
-  `COM` Serial port connection <addr> = COM port number <port> = Baudrate. -1 = default
Example: `java -jar niduprog.jar COM 14 -1 NIDSERVER`
-  `INT` Integrated reader connection. `<addr> = 0 <port> = 0`
Example: `java -jar niduprog.jar INT 0 0 NIDSERVER`

**Flags**
 - `-s` Silence mode. Not showing any messages about the updating process
 - `-log` Show more detailed information about the updating process
 - `-vlog` Show verbose log for NUR

**Exit codes**
0. Success. Device upto date
1. Invalid zip
2. Connection error
3. Programming error
4. Download error

### NiduProg source
Reference code for embedding NiduLib to java app.
[NiduProg.java](NiduProg/src/NiduProg.java)

### Reference libraries
**transports** (\nur_sample_java\transports\jars)
- `NurApiSerialTransport.jar` RXTX library based transport for NurApi
- `NurApiSocketTransport.jar` TCP/IP transport for NurApi
**import** (\nur_sample_java\import)
- `NurApi.jar`
- `NiduLib.jar`
- `org.json.jar`
  
### Example running NiduProg.jar
```
C:\GitHub\nur_sample_java\samples\FwUpdate>java -jar NiduProg.jar TCP 192.168.1.177 4333 NIDSERVER
NiduProg version 1.1 (NiduLib=3 NurApi=1.9.1.5)
-------------------------------------------------------
Loading update file from: NIDSERVER
Downloaded 1172728 bytes
Connecting TCP 192.168.1.177 4333
Connection OK. Validating..
Validating success
Start update..
Nur2Application_711A.bin
NUR2-1W-SecChip_v3330.bin
Status programming process of all update items is about to start
Update begin for Nur2Application_711A
Update begin for NUR2-1W-SecChip_v3330
Updating completed.

Success! Device upto date
```
### License
All source files in this repository is provided under terms specified in [LICENSE](LICENSE) file.

