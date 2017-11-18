#!/bin/sh

java -cp "$1/bin:samples/SamplesCommon/bin:transports/jars/*:/usr/share/java/RXTXcomm.jar:import/NurApi.jar" com.nordicid.testapplication.Example

