@echo off
setlocal enabledelayedexpansion

REM Build all examples
REM usage:
REM Execute bat in command prompt
REM nur_sample_java>build_all.bat

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

REM Compile samples common
echo. Compiling NurApiSerialTransport
IF NOT EXIST transports\NurApiSerialTransport\bin (md transports\NurApiSerialTransport\bin)
"%JAVA_HOME%\bin\javac" -d "transports/NurApiSerialTransport/bin" -cp "%RXTXJAR%;import/NurApi.jar" transports/NurApiSerialTransport/src/com/nordicid/nurapi/*.java
cd transports\NurApiSerialTransport\bin
"%JAVA_HOME%\bin\jar" cf ../../jars/NurApiSerialTransport.jar com/nordicid/nurapi/*.class
cd ..\..\..

echo. Compiling NurApiSocketTransport
IF NOT EXIST transports\NurApiSocketTransport\bin (md transports\NurApiSocketTransport\bin)
"%JAVA_HOME%\bin\javac" -d "transports/NurApiSocketTransport/bin" -cp "%RXTXJAR%;import/NurApi.jar" transports/NurApiSocketTransport/src/com/nordicid/nurapi/*.java
cd transports\NurApiSocketTransport\bin
"%JAVA_HOME%\bin\jar" cf ../../jars/NurApiSocketTransport.jar com/nordicid/nurapi/*.class
cd ..\..\..

REM Compile samples common
echo. Compiling SamplesCommon
IF NOT EXIST samples\SamplesCommon\bin (md samples\SamplesCommon\bin)
"%JAVA_HOME%\bin\javac" -d "samples/SamplesCommon/bin" -cp "transports/jars/*;%RXTXJAR%;import/NurApi.jar" samples/SamplesCommon/src/com/nordicid/samples/common/*.java

REM Compile all tests
FOR /d %%I in (samples\*_*) DO call:compile_example %%I 

goto:eof

:compile_example
echo. Compiling %~1

REM Clean up bin dir
IF EXIST %~1\bin (rmdir /s /q %~1\bin)
REM recreate bin dir
IF NOT EXIST %~1\bin (md %~1\bin)

REM compile
"%JAVA_HOME%\bin\javac" -d "%~1/bin" -cp "samples/SamplesCommon/bin;transports/jars/*;%RXTXJAR%;import/NurApi.jar" %~1/src/com/nordicid/testapplication/*.java
goto:eof