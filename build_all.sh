#!/bin/sh

# Build all examples
# usage:
# Execute in shell
# $> sh build_all.sh

# Compile transports
echo "Compiling NurApiSerialTransport"
mkdir -p transports/NurApiSerialTransport/bin
javac -d "transports/NurApiSerialTransport/bin" -cp "transports/NurApiSerialTransport/nrjavaserial-5.2.1.jar:import/NurApi.jar" transports/NurApiSerialTransport/src/com/nordicid/nurapi/*.java
cd transports/NurApiSerialTransport/bin
jar cf ../../jars/NurApiSerialTransport.jar com/nordicid/nurapi/*.class
cd ../../..

echo "Compiling NurApiSocketTransport"
mkdir -p transports/NurApiSocketTransport/bin
javac -d "transports/NurApiSocketTransport/bin" -cp "import/NurApi.jar" transports/NurApiSocketTransport/src/com/nordicid/nurapi/*.java
cd transports/NurApiSocketTransport/bin
jar cf ../../jars/NurApiSocketTransport.jar com/nordicid/nurapi/*.class
cd ../../..

# Compile samples common
echo "Compiling SamplesCommon"
mkdir -p samples/SamplesCommon/bin
javac -d "samples/SamplesCommon/bin" -cp "transports/jars/*:transports/NurApiSerialTransport/nrjavaserial-5.2.1.jar:import/NurApi.jar" samples/SamplesCommon/src/com/nordicid/samples/common/*.java

compile_example()
{
	echo "Compiling $1"

	# Clean up bin dir
	rm -rf "$1/bin"
	# recreate bin dir
	mkdir -p "$1/bin"

	# compile
	javac -d "$1/bin" -cp "samples/SamplesCommon/bin:transports/jars/*:transports/NurApiSerialTransport/nrjavaserial-5.2.1.jar:import/NurApi.jar" $1/src/com/nordicid/testapplication/*.java
}

# Compile all tests
for d in samples/*_*/ ; do
	compile_example "$d"
done

exit 0

