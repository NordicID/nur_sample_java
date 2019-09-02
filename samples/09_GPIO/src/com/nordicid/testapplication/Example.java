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
import com.nordicid.nurapi.NurRespDevCaps;
import com.nordicid.nurapi.NurRespGPIOStatus;

/**
 * This example shows how to configure GPIO's and receive GPIO events
 */
public class Example {
	
	static NurApi api = null;
	static NurRespDevCaps caps; //Device capabilities stored here after connection
	
	static void printGpios(NurGPIOConfig[] gpios)
	{
		for (int n=0; n<gpios.length; n++)
		{
			System.out.println("GPIO["+n+"] action=" + gpios[n].action + " available=" + gpios[n].available +" edge=" + gpios[n].edge + " enabled=" + gpios[n].enabled + " type=" + gpios[n].type);
		}		
	}
	
	public static void main(String[] args) {		
		
		try {
			// Create and connect new NurApi object
			// To change connection parameters, please modify SamplesCommon.java
			api = SamplesCommon.createAndConnectNurApi();
			api.setListener(apiListener);	
			caps = api.getDeviceCaps(); //read device capabilities
			System.out.println("NurApi v" + api.getFileVersion());
			System.out.println("Available GPIO's " + caps.curCfgMaxGPIO);
			//api.setLogLevel(NurApi.LOG_DATA);
		} 
		catch (Exception ex) {
			ex.printStackTrace();
			return;
		}		
		
		try {				
			
            NurGPIOConfig[] configs = api.getGPIOConfigure();
            			
			if(caps.curCfgMaxGPIO == 8) {
				
				//We have a max 8 GPIO's (smart Sampo) Set first 4 GPIO's to input and set notify event from both edges (raising. falling).
				NurGPIOConfig config = new NurGPIOConfig();
				config.available = true;
	            config.enabled = true;
	            config.type = NurApi.GPIO_TYPE_INPUT;
	            config.action = NurApi.GPIO_ACT_NOTIFY;
	            config.edge = NurApi.GPIO_EDGE_BOTH;
	
	            // Configure gpio's 1,2,3,4 as inputs. gpio's 5-8 are outputs and cannot configure as inputs
	            configs[0] = config;
	            configs[1] = config;
	            configs[2] = config;
	            configs[3] = config;
	            	            	            
	            api.setGPIOConfigure(configs);
			}
			
			
            // Print current gpio config
			printGpios(api.getGPIOConfigure());
            
			System.out.println("------------------------------");
			System.out.println("Menu: Hit key and press enter..");	
			System.out.println("c = Show GPIO Configure");	
			System.out.println("s = Show GPIO status");	
			System.out.println("a = Output demo (Smart Sampo) using setGPIOStatus");	
			System.out.println("q = exit");	
			 while(true) {
	            // Waiting for gpio events
	            System.out.println("Waiting for GPIO events. Press q to exit");	            
	           
	            int val = System.in.read();
	            if(val<31) continue;	            
	            char c=(char)val; 
	           	            
	            if(c == 'q') break;
	            if(c == 's') 
	            {
	            	//Get GPIO status by its number (0-7) GPIO1 = 0
	            	NurRespGPIOStatus resp = api.getGPIOStatus(NurApi.GPIO1);	            	
	    			System.out.println("GPIO1 Enabled=" + resp.enabled + " State=" + resp.state + " type=" + resp.type);
	    			resp = api.getGPIOStatus(NurApi.GPIO2);
	    			System.out.println("GPIO2 Enabled=" + resp.enabled + " State=" + resp.state + " type=" + resp.type);
	    			resp = api.getGPIOStatus(NurApi.GPIO3);
	    			System.out.println("GPIO3 Enabled=" + resp.enabled + " State=" + resp.state + " type=" + resp.type);
	    			resp = api.getGPIOStatus(NurApi.GPIO4);
	    			System.out.println("GPIO4 Enabled=" + resp.enabled + " State=" + resp.state + " type=" + resp.type);	    			
	    			resp = api.getGPIOStatus(NurApi.GPIO5);
	    			System.out.println("GPIO5 Enabled=" + resp.enabled + " State=" + resp.state + " type=" + resp.type);
	    			resp = api.getGPIOStatus(NurApi.GPIO6);
	    			System.out.println("GPIO6 Enabled=" + resp.enabled + " State=" + resp.state + " type=" + resp.type);
	    			resp = api.getGPIOStatus(NurApi.GPIO7);
	    			System.out.println("GPIO7 Enabled=" + resp.enabled + " State=" + resp.state + " type=" + resp.type);	
	    			resp = api.getGPIOStatus(NurApi.GPIO8);
	    			System.out.println("GPIO8 Enabled=" + resp.enabled + " State=" + resp.state + " type=" + resp.type);	
	    			
	            }
	            if(c == 'c')
	            {
	            	// Print new gpio config
	    			printGpios(api.getGPIOConfigure());
	            }
	            
	            if(c == 'a')
	            {
	            	if(caps.curCfgMaxGPIO == 8)
	            	{
	            		//We have Smart Sampo
	            		//Set output 5-8 one by one true using setGPIOStatus where parameter is bit mask of GPIO
	            		//then set all false and set two output same time true
	            		//See also function api.setGPIOStatusEx where single output can be set using GPIO number as parameter (Example: NurApi.GPIO1)
	            		try {
	            			api.setGPIOStatus(0x10,true); //Set GPIO 5 true
	            			Thread.sleep(1000);
	            			api.setGPIOStatus(0x20,true); //Set GPIO 6 true
	            			Thread.sleep(1000);
	            			api.setGPIOStatus(0x40,true); //Set GPIO 7 true
	            			Thread.sleep(1000);
	            			api.setGPIOStatus(0x80,true); //Set GPIO 8 true
	            			Thread.sleep(1000);
	            			
	            			api.setGPIOStatus(0xF0,false); //Clear all
	            			Thread.sleep(1000);
	            			api.setGPIOStatus(0xA0,true); //Output's 5 and 7 true
	            			Thread.sleep(1000);
	            			api.setGPIOStatus(0xF0,false); //Clear all
	            			api.setGPIOStatus(0x50,true); //output 6 and 8 true
	            			Thread.sleep(1000);
	            			api.setGPIOStatus(0xF0,false); //Clear all
	            			
	            			
	            		} catch (Exception e) {
	            			e.printStackTrace();
						}
	            	} else System.out.println("Not supported");
	            }
	            
	            Thread.sleep(500);
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
			
			if(caps.curCfgMaxGPIO == 8)
			{
				//If device is Smart Sampo where 4 inputs and 4 outputs, this demo changes output state of GPIO when state change on input..
				try {	
						//Example if state change on input GPIO1, output GPIO5 changes state.
						api.setGPIOStatusEx(4+arg0.source, (arg0.direction>0) ? true:false);
						
				} catch (Exception e) {					
					e.printStackTrace();
				}
			}
			
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
