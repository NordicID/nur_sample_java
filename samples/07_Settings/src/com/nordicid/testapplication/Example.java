package com.nordicid.testapplication;

import com.nordicid.samples.common.SamplesCommon;

import com.nordicid.nurapi.AntennaMapping;
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
import com.nordicid.nurapi.NurRespDevCaps;
import com.nordicid.nurapi.NurSetup;

/**
 * This example shows how to read/write NUR module RFID settings.
 */
public class Example {
	
	static void printSetup(NurSetup setup)
	{
		System.out.println("setup.linkFreq = " + setup.linkFreq);
		System.out.println("setup.txLevel = " + setup.txLevel);
		System.out.println("setup.inventoryQ = " + setup.inventoryQ);
		System.out.println("setup.inventorySession = " + setup.inventorySession);
		System.out.println("setup.inventoryRounds = " + setup.inventoryRounds);
		System.out.println("setup.antennaMaskEx = " + setup.antennaMaskEx);
		System.out.println("setup.selectedAntenna = " + setup.selectedAntenna);
		System.out.println("setup.rfProfile = " + setup.rfProfile);
		System.out.println("");
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
		
		try {
			NurRespDevCaps caps = api.getDeviceCaps();
			
			// Print antenna mapping
			System.out.println("Antennas:");
			AntennaMapping[] antennaMap = api.getAntennaMapping();
			for (int n=0; n<antennaMap.length; n++) {
				System.out.println(String.format("Antenna ID[%d] = '%s'", antennaMap[n].antennaId, antennaMap[n].name));
			}
			System.out.println("");
						
			// Print out current setup
			System.out.println("Current setup:");
			printSetup(api.getModuleSetup());
			
			int writeFlags = 0;
			NurSetup newSetup = new NurSetup();
			
			// Set full tx level
			newSetup.txLevel = 0;
			writeFlags |= NurApi.SETUP_TXLEVEL;
			System.out.println("Set tx level to " + newSetup.txLevel);
			
			if (caps.hasRfProfile())
			{
				// Set nominal rfprofile (NUR2 devices)
				newSetup.rfProfile = NurApi.RFPROFILE_NOMINAL;
				writeFlags |= NurApi.SETUP_RFPROFILE;
				System.out.println("Set rf profile to " + newSetup.rfProfile);
			} else {
				// Set 256kHz link freq
				newSetup.linkFreq = 256000;
				writeFlags |= NurApi.SETUP_LINKFREQ;
				System.out.println("Set link frequency to " + newSetup.linkFreq);
			}
						
			if (caps.curCfgMaxAnt > 1) {
				// Enable antenna 1 and 2
				newSetup.antennaMaskEx = NurApi.ANTENNAMASK_1 | NurApi.ANTENNAMASK_2;
				System.out.println("Enable antennas 1 ans 2");
			} else {
				// Enable antenna 1
				newSetup.antennaMaskEx = NurApi.ANTENNAMASK_1;
				System.out.println("Enable antenna 1");
			}
			writeFlags |= NurApi.SETUP_ANTMASKEX;

			// Write module settings
			api.setModuleSetup(newSetup, writeFlags);
			System.out.println("OK");
			
			System.out.println("New setup:");
			printSetup(api.getModuleSetup());
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
		}
		
		@Override
		public void deviceSearchEvent(NurEventDeviceInfo arg0) {
		}
		
		@Override
		public void debugMessageEvent(String arg0) {
		}
		
		@Override
		public void connectedEvent() {
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
