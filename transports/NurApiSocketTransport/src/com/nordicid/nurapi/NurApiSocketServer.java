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
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

import com.nordicid.nurapi.NurApi;
import com.nordicid.nurapi.NurApiException;
import com.nordicid.nurapi.NurEventClientInfo;

public class NurApiSocketServer implements Runnable
{
	/** Default value of maximum clients that can connect to the server at the same time. */
	static public final int DEFAULT_MAX_CLIENTS = 10;
	
	private NurApi mApi;
	private int mListeningPort;
	private int mMaxClients;
	private ServerSocket mServerSocket = null;
	
	//////
	private ArrayList<NurEventClientInfo> mClientList = new ArrayList<NurEventClientInfo>();
	private boolean mServerIsRunning = false;
	private Thread mNurApiServerThread = null;
	
	private void sendClientConnectedEvent(final NurEventClientInfo ci)
	{
		if(mApi.mListener != null)
		{
			mApi.syncNotification(new Runnable() {
				@Override
				public void run() {
					mApi.mListener.clientConnectedEvent(ci);		
				}
			});
		}
	}
	
	private void sendClientDisconnectedEvent(final NurEventClientInfo ci)
	{
		if(mApi.mListener != null)
		{
			mApi.syncNotification(new Runnable() {
				@Override
				public void run() {
					mApi.mListener.clientDisconnectedEvent(ci);		
				}
			});
		}
	}
	
	/**
	 * Return list of NUR clients that are connected to the server.
	 * @return List of connected NUR clients.
	 */
	public ArrayList<NurEventClientInfo> getServerClientList()
	{
		synchronized (mClientList) {
			return mClientList;
		}
	}
	
	/**
	 * For checking if the server is running.
	 * @return TRUE if server is running. FALSE otherwise.
	 */
	public boolean isServerRunning() { return mServerIsRunning; }
	
	/**
	 * Dispose the server.
	 */
	public void dispose()
	{
		stopServer();
	}

	/**
	 * Start the Socket server.
	 */
	public void startServer() throws Exception
	{
		if(mNurApiServerThread == null)
		{
			mNurApiServerThread = new Thread(this);
			mNurApiServerThread.start();
		}
		else
			throw new NurApiException("The socket server is already running.");
	}
	
	/**
	 * Disconnect all connected NUR clients and stop the Socket server.
	 */
	public void stopServer()
	{
		mServerIsRunning = false;
		try {
			mServerSocket.close();// Close listening socket
		} catch (Exception e) {
			e.printStackTrace();
		}
		NurEventClientInfo ci;
		synchronized (mClientList) {
			for(int i = 0; i < mClientList.size(); i++)// disconnect and dispose all connected NUR clients
			{
				try {
					ci = mClientList.get(i);
					ci.nurApi.dispose();
					sendClientDisconnectedEvent(ci);
					mClientList.remove(i);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		mApi.VLog("NurApiServer is stopped.");
	}
	
	/**
	 * Constructor for the socket server where 10 clients are allowed to connect to the server at the same time.
	 * @param port	Port that server is listening.
	 */
	public NurApiSocketServer(NurApi nurApi, int port)
	{
		this(nurApi, port, DEFAULT_MAX_CLIENTS);	
	}
	/**
	 * Constructor for the socket server where specific number of clients can connect to the server at the same time.
	 * @param port	Port that server is listening.
	 * @param maxClients	Maximum clients that are allowed to connect to the server at the same time.
	 */
	public NurApiSocketServer(NurApi nurApi, int port, int maxClients)
	{
		mApi = nurApi;
		mListeningPort = port;
		mMaxClients = maxClients;
	}
	
	public int getClientListSize()
	{
		synchronized (mClientList) {
			return mClientList.size();
		}
	}
	
	@Override
	public void run()
	{
		mServerIsRunning = true;
		mClientList = new ArrayList<NurEventClientInfo>();
		mApi.VLog("NurApiServer is running.");
		while(mServerIsRunning)
		{
			if(getClientListSize() < mMaxClients)
			{
				try {
					if(mServerSocket == null || mServerSocket.isClosed())
					{
						mApi.VLog(String.format("Server is listening port: %d.", mListeningPort));
						mServerSocket = new ServerSocket(mListeningPort); // More space found, re-open the listening socket.
						mServerSocket.setSoTimeout(2000);
					}
					
					Socket client;
					try {
						client = mServerSocket.accept();
					}  catch (SocketException e) {
						continue; // Socket closed
					} catch (SocketTimeoutException e) {
						continue; // Socket accepted timed out (2000ms)
					}
					NurApi nurApi = new NurApi(new NurApiSocketTransport(client));
					nurApi.connect();
					int port = client.getLocalPort();

					String IpAdress = client.getInetAddress().getHostAddress();
					NurEventClientInfo ci = new NurEventClientInfo(nurApi, IpAdress, port);
					
					synchronized (mClientList) {
						mClientList.add(ci);
					}
					
					mApi.VLog(String.format("Client connected : %s to port : %d.", ci.ipAdress, ci.port));
					sendClientConnectedEvent(ci);
				}
				catch (Exception e2) {
					e2.printStackTrace();
				}
			}
			else
			{
				try {
					if(!mServerSocket.isClosed())
					{
						mApi.VLog(String.format("Maximum clients has been connected to the server, closing the listening port: %d.", mListeningPort));
						mServerSocket.close();	// No space, close the listening socket.
					}
					Thread.sleep(200);			// Waiting space.
				} catch (Exception e) {
//					e.printStackTrace();
				}
			}
		}
	}
}
