package com.nordicid.testapplication;

import com.nordicid.nurapi.NurApi;
import com.nordicid.nurapi.NurApiListener;
import com.nordicid.nurapi.NurApiSocketServer;
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


/**
 * This example just shows howto create server for listening incoming client connections.
 * Reader must be set in to the client mode and create connection in this application listening port 5000 (default)
 */
public class Example {
			

	public static void main(String[] args) {
		
		NurApi api = null;
		NurApiSocketServer mSocketServer=null;
		
		try {
			// Create NurApi object as parent			
			api = new NurApi();
			api.setLogLevel(NurApi.LOG_VERBOSE);
			api.setListener(apiListener);

			//Listening port 5000
			mSocketServer = new NurApiSocketServer(api, 5000);
			mSocketServer.startServer();
			
		} 
		catch (Exception ex) {
			ex.printStackTrace();
			return;
		}
		
		System.out.println("NurApi v" + api.getFileVersion());		
		
		try {		
			System.out.println("Press ENTER to exit");	
			System.in.read();

		}
		catch(Exception e) {}

		System.out.println("See you again!.");
		mSocketServer.stopServer();
		
		// Dispose the NurApi
		api.dispose();
		System.exit(1);
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
			System.out.println("LOG:" + arg1);
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
			System.out.println("DBG:" + arg0);
		}
		
		@Override
		public void connectedEvent() {
			// We receive connected event when NurApi transport is connected
			System.out.println("connectedEvent()");
		}
		
		@Override
		public void clientDisconnectedEvent(NurEventClientInfo arg0) {			
			System.out.println("Client disconnected: " + arg0.ipAdress);			
		}
		
		@Override
		public void clientConnectedEvent(NurEventClientInfo arg0) {
			try {
					//Client has been connected. You can deal with the connected reader using this Nurapi handle
					NurApi clientHandle = arg0.nurApi;
					System.out.println("Client Connected: " + arg0.ipAdress + " Device=" + clientHandle.getReaderInfo().name);
				}
				catch(Exception e) {
					System.out.println("Exception (clientDisconnectedEvent) " + e.getMessage());
				}
		}
		
		@Override
		public void bootEvent(String arg0) {
		}
		
		@Override
		public void IOChangeEvent(NurEventIOChange arg0) {
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
