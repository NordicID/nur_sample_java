package com.nordicid.testapplication;

import com.nordicid.samples.common.SamplesCommon;

import java.util.ArrayList;

import com.nordicid.nurapi.NurApi;
import com.nordicid.nurapi.NurApiListener;

import com.nordicid.nurapi.NurEventAutotune;
import com.nordicid.nurapi.NurEventClientInfo;
import com.nordicid.nurapi.NurEventDeviceInfo;
import com.nordicid.nurapi.NurEventEpcEnum;
import com.nordicid.nurapi.NurEventFrequencyHop;
import com.nordicid.nurapi.NurEventIOChange;
import com.nordicid.nurapi.NurEventInventory;
import com.nordicid.nurapi.NurEventNxpAlarm;
import com.nordicid.nurapi.NurEventProgrammingProgress;
import com.nordicid.nurapi.NurEventTagTrackingChange;
import com.nordicid.nurapi.NurEventTagTrackingData;
import com.nordicid.nurapi.NurEventTraceTag;
import com.nordicid.nurapi.NurEventTriggeredRead;
import com.nordicid.nurapi.NurRespReaderInfo;
import com.nordicid.nurapi.NurEthConfig;

/**
 * This example shows how to perform device search on local network
 */
public class Example {
	
	static void printDevices(ArrayList<NurEthConfig> devices)
	{
		for (int n=0; n<devices.size(); n++)
		{
            NurEthConfig dev = devices.get(n);
			System.out.println("Device["+dev.title+"]");
			System.out.println("  ip = " + dev.ip);
			System.out.println("  hostMode = " + (dev.hostMode==0 ? "Server" : "Client"));
            if (dev.hostMode==0) {
                System.out.println("  serverPort = " + dev.serverPort);
            } else {
                System.out.println("  hostip = " + dev.hostip);
                System.out.println("  hostPort = " + dev.hostPort);
            }
		}		
	}
	
	public static void main(String[] args) {
		NurApi api = null;
		
		try {
			// Create new NurApi object
			// To change parameters, please modify SamplesCommon.java
			api = SamplesCommon.createNurApi();
			api.setListener(apiListener);
		} 
		catch (Exception ex) {
			ex.printStackTrace();
			return;
		}
		
		System.out.println("NurApi v" + api.getFileVersion());
		
		try {
            ArrayList<NurEthConfig> devices = api.queryEthDevices();
            printDevices(devices);
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}

		System.out.println("See you again!.");
		try {
			// Disconnect the connection
			api.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Dispose the NurApi
		api.dispose();
	}
	
	static NurApiListener apiListener = new NurApiListener() {
		
		@Override
		public void triggeredReadEvent(NurEventTriggeredRead arg0) {
		}
		
		@Override
		public void traceTagEvent(NurEventTraceTag arg0) {
		}
		
		@Override
		public void programmingProgressEvent(NurEventProgrammingProgress arg0) {
		}
		
		@Override
		public void logEvent(int arg0, String arg1) {
		}
		
		@Override
		public void inventoryStreamEvent(NurEventInventory arg0) {
		}
		
		@Override
		public void inventoryExtendedStreamEvent(NurEventInventory arg0) {
		}
		
		@Override
		public void frequencyHopEvent(NurEventFrequencyHop arg0) {
		}
		
		@Override
		public void disconnectedEvent() {
			// We receive disconnected event when NurApi transport is disconnected
			System.out.println("disconnectedEvent()");
			
		}
		
		@Override
		public void deviceSearchEvent(NurEventDeviceInfo arg0) {
		}
		
		@Override
		public void debugMessageEvent(String arg0) {
		}
		
		@Override
		public void connectedEvent() {
			// We receive connected event when NurApi transport is connected
			System.out.println("connectedEvent()");
		}
		
		@Override
		public void clientDisconnectedEvent(NurEventClientInfo arg0) {
		}
		
		@Override
		public void clientConnectedEvent(NurEventClientInfo arg0) {
		}
		
		@Override
		public void bootEvent(String arg0) {
		}
		
		@Override
		public void IOChangeEvent(NurEventIOChange arg0) {
			System.out.println("IOChangeEvent() source " + arg0.source + "; direction " + arg0.direction);
		}

		@Override
		public void autotuneEvent(NurEventAutotune arg0) {
		}

		@Override
		public void epcEnumEvent(NurEventEpcEnum arg0) {
		}

		@Override
		public void nxpEasAlarmEvent(NurEventNxpAlarm arg0) {
		}

		@Override
		public void tagTrackingChangeEvent(NurEventTagTrackingChange arg0) {
		}

		@Override
		public void tagTrackingScanEvent(NurEventTagTrackingData arg0) {
		}
	};
}
