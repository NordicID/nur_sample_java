#!/bin/sh

# Build all examples
# usage:
# Execute in shell
# $> sh build_all.sh


# Compile transports
echo "Compiling NurApiSerialTransport"
javac -d "transports/NurApiSerialTransport/bin" -cp "/usr/share/java/RXTXcomm.jar:import/NurApi.jar" transports/NurApiSerialTransport/src/com/nordicid/nurapi/*.java
cd transports/NurApiSerialTransport/bin
jar cf ../../jars/NurApiSerialTransport.jar com/nordicid/nurapi/*.class
cd ../../..

echo "Compiling NurApiSocketTransport"
javac -d "transports/NurApiSocketTransport/bin" -cp "/usr/share/java/RXTXcomm.jar:import/NurApi.jar" transports/NurApiSocketTransport/src/com/nordicid/nurapi/*.java
cd transports/NurApiSocketTransport/bin
jar cf ../../jars/NurApiSocketTransport.jar com/nordicid/nurapi/*.class
cd ../../..

# Compile samples common
echo "Compiling SamplesCommon"
javac -d "samples/SamplesCommon/bin" -cp "transports/jars/*:/usr/share/java/RXTXcomm.jar:import/NurApi.jar" samples/SamplesCommon/src/com/nordicid/samples/common/*.java

compile_example()
{
	echo "Compiling $1"

	# Clean up bin dir
	rm -rf "$1/bin"
	# recreate bin dir
	mkdir -p "$1/bin"

	# compile
	javac -d "$1/bin" -cp "samples/SamplesCommon/bin:transports/jars/*:/usr/share/java/RXTXcomm.jar:import/NurApi.jar" $1/src/com/nordicid/testapplication/*.java
}

# Compile all tests
for d in samples/*_*/ ; do
	compile_example "$d"
done

exit 0

