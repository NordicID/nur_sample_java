package com.nordicid.samples.common;

import com.nordicid.nurapi.NurApiSocketTransport;
import com.nordicid.nurapi.NurApiTransport;
import com.nordicid.nurapi.NurApi;
import com.nordicid.nurapi.NurApiSerialTransport;

public class SamplesCommon {
	

	/**
	 * Create transport for NurApi.
	 * NOTE: SETUP TRANSPORT FOR YOUR ENVIRONMENT
	 */	
	public static NurApiTransport createTransport() {
		
		// Connect reader over network
		return createSocket("192.168.1.110", 4333);
		//return createSocket("ar8xaabbcc.local", 4333);
		
		// Connect reader over serial port (WINDOWS)
		//return createSerial("COM30", 115200);
		
		// Connect reader over serial port (Linux)
		//return createSerial("/dev/ttyACM0", 115200);
		//return createSerial("/dev/ttyS0", 115200);
		//return createSerial("/dev/ttyAMA0", 115200);
	}
	
	////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////
	// Common
	////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////
	
	/** Create serial port transport 
	 * @param port 			Serial port identifier. Windows e.g. "COM5", Linux e.g. (usb) "/dev/ttyACM0", (serial) "/dev/ttyS0"
	 * @param baudrate		Baudrate, usually 115200
	 */
	public static NurApiTransport createSerial(String port, int baudrate) {
		System.out.println("createSerial("+port+", "+baudrate+")");
		return new NurApiSerialTransport(port, baudrate);
	}

	/**
	 * Create TCP/IP socket transport
	 * @param addr 	ipv4 address or hostname
	 * @param port	TCP/IP port, usually 4333
	 */
	public static NurApiTransport createSocket(String addr, int port) {
		System.out.println("createSocket("+addr+", "+port+")");
		return new NurApiSocketTransport(addr, port);
	}
    
    public static NurApi createNurApi()
	{
		// Create a new NurApi object
		NurApi api = new NurApi();
		// Enable log to stdout
		api.setLogToStdout(true);
			
		// Enable full logging
		// api.setLogLevel(NurApi.LOG_ERROR | NurApi.LOG_VERBOSE | NurApi.LOG_USER | NurApi.LOG_DATA);
			
		return api;
	}
	
	public static NurApi createAndConnectNurApi() throws Exception
	{
		// Create a new NurApi object
		NurApi api = createNurApi();
		try {			
			// Set transport for NurApi
			api.setTransport(SamplesCommon.createTransport());
			
			// Open a connection for NurApi
			api.connect();
			
			return api;
		} 
		catch (Exception ex) {
			api.dispose();
			System.out.println("*** COULD NOT CONNECT READER ***");
			System.out.println("*** CHECK YOUR CONNECTION SETTINGS ***");
			System.out.println("*** See createTransport() in SamplesCommon.java ***");
			throw ex;
		}
	}
}
