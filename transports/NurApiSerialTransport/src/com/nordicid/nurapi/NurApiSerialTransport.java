package com.nordicid.nurapi;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;

import java.util.Map;
import java.util.HashMap;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.File;
import java.io.FileNotFoundException;

public class NurApiSerialTransport implements NurApiTransport, SerialPortEventListener
{
	SerialPort port = null;
	CommPortIdentifier ident;
	InputStream  input = null;
    OutputStream output = null;
	
    String portName;
	int baudrate;
	
	Object readNotifier = new Object();
	
	static public ArrayList<String> enumeratePorts()
	{
		ArrayList<String> ret = new ArrayList<String>();
		@SuppressWarnings("rawtypes")
		Enumeration ports = CommPortIdentifier.getPortIdentifiers();
		while (ports.hasMoreElements()) 
		{
			CommPortIdentifier portId = (CommPortIdentifier)ports.nextElement();
			if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL)
			{
				ret.add(portId.getName());
			}
		}
		return ret;
	}
	
	// Ugly function to read windows registry values using reg query cmd line tool.. 
	// https://stackoverflow.com/questions/62289/read-write-to-windows-registry-using-java
	public static String getWinRegValue(String root, String keyPath, String keyName)
            throws IOException, InterruptedException
    {
        Process keyReader = Runtime.getRuntime().exec(
                "reg query \"" + root + keyPath + "\" /v \"" + keyName + "\"");

        BufferedReader outputReader = new BufferedReader(new InputStreamReader(keyReader.getInputStream()));
        StringBuffer outputBuffer = new StringBuffer();
		String readLine;
        while ((readLine = outputReader.readLine()) != null)
        {
            outputBuffer.append(readLine);
        }
        String[] outputComponents = outputBuffer.toString().split("    ");
        keyReader.waitFor();
        return outputComponents[outputComponents.length - 1];
    }
	
	static public Map<String, String> enumeratePortsFriendly() throws Exception 
	{
		Map<String, String> ret = new HashMap<String, String>();
		String val = getWinRegValue("HKEY_LOCAL_MACHINE", "\\SYSTEM\\CurrentControlSet\\Services\\Serenum\\Enum", "Count");
		int count = Integer.decode(val);
		//System.out.println("Count: " + count); 		
		
		for (int n=0; n<count; n++)
		{
			val = getWinRegValue("HKEY_LOCAL_MACHINE", "\\SYSTEM\\CurrentControlSet\\Services\\Serenum\\Enum", String.valueOf(n));
			//System.out.println("Path: " + val); 		
			
			String friendlyName = getWinRegValue("HKEY_LOCAL_MACHINE", "\\SYSTEM\\CurrentControlSet\\Enum\\" + val, "FriendlyName");
			//System.out.println("friendlyName: " + friendlyName); 		
			
			String portName = getWinRegValue("HKEY_LOCAL_MACHINE", "\\SYSTEM\\CurrentControlSet\\Enum\\" + val + "\\Device Parameters", "PortName");
			//System.out.println("portName: " + portName); 		
			
			ret.put(portName, friendlyName);
		}
		return ret;
	}

	public NurApiSerialTransport(String portName, int baudrate)
	{
		this.portName = portName;
		this.baudrate = baudrate;
	}
	
	@Override
	public int readData(byte[] buffer) throws IOException 
	{
		synchronized (readNotifier) 
		{
			try {
				readNotifier.wait();
			} catch (InterruptedException e) {
				//e.printStackTrace();
			}
		}
		
		if (input == null)
			return -1;
		
		int read = 0;
		
		while (input.available() > 0)
		{
			int avail = input.available();
			if (avail > 0) {
				read += input.read(buffer, read, avail);
			}
		}
		
		return read;
	}

	@Override
	public int writeData(byte[] buffer, int len) throws IOException {
		if (output == null)
			return -1;
		output.write(buffer, 0, len);
		return len;
	}

	@Override
	public void connect() throws Exception 
	{
		// Linux doesn't have reliable way to detect ports..
		if (System.getProperty("os.name").toLowerCase().endsWith("x"))
			System.setProperty("gnu.io.rxtx.SerialPorts", portName);
		
		ident = CommPortIdentifier.getPortIdentifier(portName);
		port = (SerialPort)ident.open("NurApi", 1000);
		
		try {
			port.setSerialPortParams(baudrate, 
					SerialPort.DATABITS_8,
			        SerialPort.STOPBITS_1, 
			        SerialPort.PARITY_NONE);
		} catch (Exception e) { }
		
		port.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);
		
		//port.enableReceiveThreshold(1);
		port.disableReceiveThreshold();
		
		//port.enableReceiveTimeout(10);
		port.disableReceiveTimeout();
		port.addEventListener(this);
		port.notifyOnDataAvailable(true);

		input = port.getInputStream();
		output = port.getOutputStream();
	}

	@Override
	public void disconnect() 
	{
		if (port != null)
		{
			synchronized (readNotifier) 
			{
				port.removeEventListener();
				port.close();
				port = null;
				ident = null;
				input = null;
				output = null;
				readNotifier.notifyAll();
			}
		}
	}

	@Override
	public boolean isConnected() 
	{
		return (port != null);
	}

	@Override
	public void serialEvent(SerialPortEvent event) 
	{
		switch (event.getEventType()) 
		{
        	case SerialPortEvent.DATA_AVAILABLE:
        		synchronized (readNotifier) {
        			readNotifier.notifyAll();
				}
        		break;
            default:
            	break;
        }
	}

	@Override
	public boolean disableAck() {
		// Do not disable ACK for serial transport
		return false;
	}
}
