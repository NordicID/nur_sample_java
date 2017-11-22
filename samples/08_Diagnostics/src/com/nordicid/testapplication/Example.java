package com.nordicid.testapplication;

import com.nordicid.samples.common.SamplesCommon;

import com.nordicid.nurapi.NurApi;
import com.nordicid.nurapi.NurApiListener;
import com.nordicid.nurapi.NurDiagReportListener;
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
import com.nordicid.nurapi.NurRespDiagConfig;
import com.nordicid.nurapi.NurRespDiagGetReport;
import com.nordicid.nurapi.NurRespReaderInfo;

/**
 * This example shows how to get diagnostics report from NUR module.
 */
public class Example {
	
	static void printDiagReport(NurRespDiagGetReport report)
	{
		System.out.println("repResp.flags = " + report.flags);
		System.out.println("repResp.uptime = " + report.uptime);
		System.out.println("repResp.rfActiveTime = " + report.rfActiveTime);
		System.out.println("repResp.temperature = " + report.temperature);
		System.out.println("repResp.bytesIn = " + report.bytesIn);
		System.out.println("repResp.bytesOut = " + report.bytesOut);
		System.out.println("repResp.bytesIgnored = " + report.bytesIgnored);
		System.out.println("repResp.antennaErrors = " + report.antennaErrors);
		System.out.println("repResp.hwErrors = " + report.hwErrors);
		System.out.println("repResp.invTags = " + report.invTags);
		System.out.println("repResp.invColl = " + report.invColl);
		System.out.println("repResp.readTags = " + report.readTags);
		System.out.println("repResp.readErrors = " + report.readErrors);
		System.out.println("repResp.writeTags = " + report.writeTags);
		System.out.println("repResp.writeErrors = " + report.writeErrors);
		System.out.println("repResp.errorConds = " + report.errorConds);
		System.out.println("repResp.setupErrs = " + report.setupErrs);
		System.out.println("repResp.invalidCmds = " + report.invalidCmds);
	}
	
	static NurDiagReportListener diagListener = new NurDiagReportListener() {
		@Override
		public void diagReportEvent(NurRespDiagGetReport report) {
			System.out.println("***** diagReportEvent()");
			System.out.println("***** Diagnostics report:");
			printDiagReport(report);			
		}
	};
	
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
			// Set diagnostics event listener
			api.setDiagnosticsListener(diagListener);
			
			// Get current diagnostics report. flags=NurApi.DIAG_GETREPORT_NONE or NurApi.DIAG_GETREPORT_RESET_STATS
			NurRespDiagGetReport report = api.getDiagReport(NurApi.DIAG_GETREPORT_NONE);
			System.out.println("Diagnostics report:");
			printDiagReport(report);
			
			// Get current diagnostics config
			NurRespDiagConfig cfgResp = api.getDiagConfig();
			System.out.println("Current config:");
			System.out.println("cfgResp.flags = " + cfgResp.flags);
			System.out.println("cfgResp.interval = " + cfgResp.interval);
			
			// Set all diagnostics with 5 sec interval
			api.setDiagConfig(
					NurApi.DIAG_CFG_NOTIFY_PERIODIC | 
					NurApi.DIAG_CFG_NOTIFY_WARN |
					NurApi.DIAG_CFG_FW_DEBUG_LOG | 
					NurApi.DIAG_CFG_FW_ERROR_LOG, 5);
			
			// Sleep for 7 sec, we should get some async diagnostics event within that time
			Thread.sleep(7000);
			
			// Restore config
			api.setDiagConfig(cfgResp.flags, cfgResp.interval);
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
