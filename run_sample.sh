#!/bin/sh

java -cp "$1/bin:samples/SamplesCommon/bin:transports/jars/*:transports/NurApiSerialTransport/nrjavaserial-5.2.1.jar:import/NurApi.jar" com.nordicid.testapplication.Example

