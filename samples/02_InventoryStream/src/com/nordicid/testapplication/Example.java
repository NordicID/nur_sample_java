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
import com.nordicid.nurapi.NurTag;
import com.nordicid.nurapi.NurTagStorage;

/**
 * This example shows how to run continuous inventory in asynchronous stream.
 * - Inventory is used to read multiple tag's EPC codes in reader field of view
 */
public class Example {
	
	// We store unique read tags in this storage
	static NurTagStorage uniqueTags = new NurTagStorage();
	
	// API access
	static NurApi api = null;	
	
	// stream event count
	static int eventCount = 0;

	public static void main(String[] args) {
		
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
			
			System.out.println("Starting inventory stream for 30 secs");
			
			// Start inventory stream, see inventoryStreamEvent() below
			api.startInventoryStream();
			
			// Let it run for 30 sec
			Thread.sleep(30000);
			
			System.out.println(String.format("Total %d stream events", eventCount));
			System.out.println(String.format("Inventoried total %d unique tags", uniqueTags.size()));
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
		
		// This event is fired when ever reader completes inventory round and tags are read 
		// NOTE: Depending on reader settings, tag amount and environment this event maybe fired very frequently
		@Override
		public void inventoryStreamEvent(NurEventInventory arg0) {
			eventCount++;
			
			NurTagStorage apiStorage = api.getStorage();
			
			// When accessing api owned storage from event, we need to lock it
			synchronized (apiStorage) {
				// Add inventoried tags to our unique tag storage
				for (int n=0; n<apiStorage.size(); n++) {
					NurTag tag = apiStorage.get(n);
					if (uniqueTags.addTag(tag)) {
						System.out.println(String.format(uniqueTags.size() + "# New unique tag '%s' RSSI %d", tag.getEpcString(), tag.getRssi()));
					}
				}
			}
			
			// If stream stopped, restart
			if (arg0.stopped) {
				try {
					System.out.println("Restarting stream");
					api.startInventoryStream();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
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
