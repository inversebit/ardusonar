package org.inversebit.ardusonar;

import java.io.IOException;

import android.bluetooth.BluetoothSocket;


public class SocketHolder{
	private static SocketHolder mySH = new SocketHolder();
	private BluetoothSocket bts;

	private SocketHolder(){
		bts = null;
	}
	
	public static SocketHolder getMySH(){
		return mySH;
	}
	
	public synchronized BluetoothSocket getBluetoothSocket(){
		//Bad getter
		return bts;
	}
	
	public synchronized void setBluetoothSocket(BluetoothSocket bts){
		this.bts = bts;
	}
	
	public synchronized void releaseBluetoothSocket(){
		if(bts != null){
			try
			{
				bts.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	
}
