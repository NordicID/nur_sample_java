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

REM manually set JAVA_HOME
SET JAVA_HOME=D:\Program Files\Amazon Corretto\jdk1.8.0_252
if "%JAVA_HOME%" == "" (
	echo FAILED
	echo JAVA_HOME Environment not set
	echo Set JAVA_HOME to point java install dir in system environment
	echo Or set JAVA_HOME in this bat file
	exit /B 2
)

REM RXTX jar path
SET RXTXJAR=transports/NurApiSerialTransport/rxtx/win_x64/RXTXcomm.jar

REM RXTX native lib jar path
SET RXTXLIBPATH=transports/NurApiSerialTransport/rxtx/win_x64/

"%JAVA_HOME%/bin/java" -Djava.library.path="%RXTXLIBPATH%" -cp "%1/bin;samples/SamplesCommon/bin;transports/jars/*;%RXTXJAR%;import/NurApi.jar" com.nordicid.testapplication.Example

