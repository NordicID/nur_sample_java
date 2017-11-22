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
import com.nordicid.nurapi.NurIRConfig;
import com.nordicid.nurapi.NurRespInventory;
import com.nordicid.nurapi.NurTag;

/**
 * This example shows how to run single synchronous inventory command with extra read command.
 * - Inventory is used to read multiple tag's EPC codes in reader field of view
 * - With inventory read you can read any other memory bank during inventory
 * - NOTE: This will slow down inventory speed by approx. half
 */
public class Example {
	
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
			// Clear tag storage
			api.clearIdBuffer(true);
			
			// Setup inventory read config:
			NurIRConfig nurIrConfig = new NurIRConfig();
			
			// Use inventory read
			nurIrConfig.IsRunning = true;

			// type=IRTYPE_EPCDATA (return epc and data)
			nurIrConfig.irType = NurApi.IRTYPE_EPCDATA;
			
			// bank=TID
			nurIrConfig.irBank = NurApi.BANK_TID;
			
			// addr=word address 0
			nurIrConfig.irAddr = 0;
			
			// nWords=2 words read count
			nurIrConfig.irWordCount = 2;
			
			// Start inventory read mode
			api.setIRConfig(nurIrConfig);
			
			// Perform inventory w/ default settings
			NurRespInventory resp = api.inventory();
			System.out.println("inventory numTagsFound: " + resp.numTagsFound);
			
			if (resp.numTagsFound > 0)
			{
				// Fetch and print tags
				api.fetchTags();
				for (int n=0; n<api.getStorage().size(); n++) {
					NurTag tag = api.getStorage().get(n);
					System.out.println(String.format("tag[%d] EPC '%s' DATA '%s'", n, tag.getEpcString(), tag.getDataString()));
				}
			}
			
			// Stop inventory read
			nurIrConfig.IsRunning = false;
			api.setIRConfig(nurIrConfig);
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
