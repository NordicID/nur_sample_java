@echo off
setlocal enabledelayedexpansion

REM Execute single example
REM usage:
REM Execute bat in command prompt
REM nur_sample_java>run_sample.bat example_path
REM
REM Examples:
REM nur_sample_java>run_sample.bat samples\00_Connection
REM nur_sample_java>run_sample.bat samples\01_SimpleInventory

REM RXTX jar path
SET RXTXJAR=transports/NurApiSerialTransport/rxtx/win_x64/RXTXcomm.jar

REM RXTX native lib jar path
SET RXTXLIBPATH=transports/NurApiSerialTransport/rxtx/win_x64/

"%JAVA_HOME%/bin/java" -Djava.library.path="%RXTXLIBPATH%" -cp "%1/bin;samples/SamplesCommon/bin;transports/jars/*;%RXTXJAR%;import/NurApi.jar" com.nordicid.testapplication.Example

