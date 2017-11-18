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
