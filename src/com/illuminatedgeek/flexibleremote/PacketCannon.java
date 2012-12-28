package com.illuminatedgeek.flexibleremote;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import android.os.AsyncTask;

public class PacketCannon {
	public DatagramSocket ds;
	public DatagramPacket dp;
	
	private InetAddress host;
	private int port;
	
	public PacketCannon(String host, int port) throws SocketException, UnknownHostException {
		ds = new DatagramSocket();
		ds.setBroadcast(true);
		
		this.host = InetAddress.getByName(host);
		this.port = port;
	}
	
	public void sendPacket(String... data){
		new SendPackets().execute(data);
	}
	
	private class SendPackets extends AsyncTask<String, Void, Void> {
		@Override
		protected Void doInBackground(String... arg0) {
			for(String packet : arg0){
				sendString(packet);
			}
			return null;
		}
		
		private void sendString(String data) {
			try{
				dp = new DatagramPacket(data.getBytes(), data.length(), host, port);
				ds.send(dp);
			} catch (Exception e) {e.printStackTrace();}
		} 

	}
}
