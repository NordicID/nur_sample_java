/* 
  Copyright 2016- Nordic ID 
  NORDIC ID SOFTWARE DISCLAIMER

  You are about to use Nordic ID Demo Software ("Software"). 
  It is explicitly stated that Nordic ID does not give any kind of warranties, 
  expressed or implied, for this Software. Software is provided "as is" and with 
  all faults. Under no circumstances is Nordic ID liable for any direct, special, 
  incidental or indirect damages or for any economic consequential damages to you 
  or to any third party.

  The use of this software indicates your complete and unconditional understanding 
  of the terms of this disclaimer. 
  
  IF YOU DO NOT AGREE OF THE TERMS OF THIS DISCLAIMER, DO NOT USE THE SOFTWARE.  
*/

package com.nordicid.nurapi;

import com.nordicid.nurapi.NurApiException;
import com.nordicid.nurapi.NurApiTransport;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import java.net.InetAddress;
import java.net.InetSocketAddress;

/** 
 * Socket transport for NUR Java API.
 * @author Nordic ID.
 * @version 1.6.1.7
 */
public class NurApiSocketTransport implements NurApiTransport
{
	private boolean isClient;
	private Socket mSocket = null;
	private InputStream mInput = null;
	private OutputStream mOutput = null;
	private String mHost = "";
	private int mPort = 0;
	private boolean mConnected = false;
	
	/**
	 * Server uses this internally.
	 * @param client Client socket that are accepted by the server.
	 */
	public NurApiSocketTransport(Socket client)
	{
		mSocket = client;
	}
	
	/**
	 * Constructor to create transport for client connection.
	 * @param host	IP address to the host NUR device.
	 * @param port	Port to the host NUR device.
	 */
	public NurApiSocketTransport(String host, int port)
	{
		isClient = true;
		mHost = host;
		mPort = port;
	}
	
	@Override
	public void connect() throws Exception
	{
		if(mConnected)
			return;
		
		if(isClient) //client connection
		{
			try {
				InetAddress addr = InetAddress.getByName(mHost);
				SocketAddress sockaddr = new InetSocketAddress(addr, mPort);
				mSocket = new Socket();
				mSocket.connect(sockaddr, 10000);
				mOutput = mSocket.getOutputStream();
				mInput = mSocket.getInputStream();
			} 
			catch (UnknownHostException e) 
			{				 
				e.printStackTrace();
				throw new NurApiException("Error connecting to NUR device. Unknown host: " + mHost);
			}
			catch (Exception e) 
			{
				e.printStackTrace();				
				throw new NurApiException("Exception when connecting to NUR device.");
			}
		}
		else //host connection
		{
			try {
				mOutput = mSocket.getOutputStream();
				mInput = mSocket.getInputStream();
			} catch (Exception e) {
				throw new NurApiException("Error while opening I/O-streams between the client and the server.");
			}
		}
		mConnected = true;
	}

	@Override
	public void disconnect()
	{
		if(!mConnected)
			return;
		
        try{            
        	if(mInput != null) 
        	{
        		mInput.close();
        	}
        }
    	catch(Exception e) {}
        
        try{            
            if(mOutput != null) 
            {
            	mOutput.close();
            }
        }
    	catch(Exception e) {}
        
        try{            
            if(mSocket != null) 
            {
            	mSocket.close();
            }
        }
    	catch(Exception e) {}        
        
        mConnected = false;
	}

	@Override
	public boolean isConnected()
	{
		return (mConnected);
	}
	
	@Override
	public int readData(byte[] buffer) throws IOException
	{
		int read = 0;
		if(mConnected)
		{			
			if (mInput == null)
				return -1;
			
			try {
				int r = mInput.read(buffer, 0, buffer.length);
				if (r < 0)
				{
					disconnect();
					return -1;
				}
				read += r;
			} 
			catch (SocketTimeoutException sto) {
				return -1;
			}		
			catch (SocketException se) {
				disconnect();
				return -1;
			}
		}
		return read;
	}

	@Override
	public int writeData(byte[] buffer, int len) throws IOException
	{
		if (mOutput == null)
			return -1;

		try {			
			mOutput.write(buffer, 0, len);			
		} catch (Exception e) {
			e.printStackTrace();
			len = -1;
		}
		return len;
	}

	@Override
	public boolean disableAck() {
		// Do not disable ACK in socket transport
		return false;
	}
}
