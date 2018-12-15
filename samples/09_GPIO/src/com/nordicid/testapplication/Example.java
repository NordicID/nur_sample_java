package com.nordicid.testapplication;

import com.nordicid.samples.common.SamplesCommon;

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
import com.nordicid.nurapi.NurGPIOConfig;

/**
 * This example shows how to configure GPIO's and receive GPIO events
 */
public class Example {
	
	static void printGpios(NurGPIOConfig[] gpios)
	{
		for (int n=0; n<gpios.length; n++)
		{
			System.out.println("GPIO["+n+"]");
			System.out.println("  action = " + gpios[n].action);
			System.out.println("  available = " + gpios[n].available);
			System.out.println("  edge = " + gpios[n].edge);
			System.out.println("  enabled = " + gpios[n].enabled);
			System.out.println("  type = " + gpios[n].type);
		}		
	}
	
	public static void main(String[] args) {
		NurApi api = null;
		
		try {
			// Create and connect new NurApi object
			// To change connection parameters, please modify SamplesCommon.java
			api = SamplesCommon.createAndConnectNurApi();
			api.setListener(apiListener);
		} 
		catch (Exception ex) {
			ex.printStackTrace();
			return;
		}
		
		System.out.println("NurApi v" + api.getFileVersion());
		
		try {
            NurGPIOConfig[] configs = api.getGPIOConfigure();
            // Print current gpio config
			printGpios(configs);
		
			NurGPIOConfig config = new NurGPIOConfig();
			config.available = true;
            config.enabled = true;
            config.type = NurApi.GPIO_TYPE_INPUT;
            config.action = NurApi.GPIO_ACT_NOTIFY;
            config.edge = NurApi.GPIO_EDGE_BOTH;

            // Configure gpio's 1 and 2
            configs[0] = config;
            configs[1] = config;
            api.setGPIOConfigure(configs);
			
            // Print new gpio config
			printGpios(api.getGPIOConfigure());
            
            // Waiting for gpio events
            System.out.println("Waiting for GPIO events");
            while (api.isConnected()) {
                Thread.sleep(1000);
            }
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
