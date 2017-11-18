package com.nordicid.samples.common;

import com.nordicid.nurapi.NurApiSocketTransport;
import com.nordicid.nurapi.NurApiTransport;
import com.nordicid.nurapi.NurApi;
import com.nordicid.nurapi.NurApiSerialTransport;

public class SamplesCommon {
	
	// -----------------------------
	// SETUP TRANSPORT FOR YOUR ENVIRONMENT
	// -----------------------------
	public static NurApiTransport createTransport() {
		
		// Connect reader over network
		return createSocket("172.16.32.13", 4333);
		
		// Connect reader over serial port (WINDOWS)
		//return createSerial("COM5", 115200);
		
		// Connect reader over serial port (*NIX)
		//return createSerial("/dev/ttyACM0", 115200);
	}
	
	////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////
	// Common
	////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////
	
	public static NurApiTransport createSerial(String port, int baudrate) {
		System.out.println("createSerial("+port+", "+baudrate+")");
		return new NurApiSerialTransport(port, baudrate);
	}

	public static NurApiTransport createSocket(String addr, int port) {
		System.out.println("createSocket("+addr+", "+port+")");
		return new NurApiSocketTransport(addr, port);
	}
	
	public static NurApi createAndConnectNurApi() throws Exception
	{
		// Create a new NurApi object
		NurApi api = new NurApi();
		try {
			// Enable log to stdout
			api.setLogToStdout(true);
			
			// Enable full logging
			//api.setLogLevel(NurApi.LOG_ERROR | NurApi.LOG_VERBOSE | NurApi.LOG_USER | NurApi.LOG_DATA);
			
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
