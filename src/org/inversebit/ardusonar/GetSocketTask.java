package org.inversebit.ardusonar;

import java.io.IOException;
import java.util.UUID;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.AsyncTask;
import android.util.Log;


public class GetSocketTask extends AsyncTask<BluetoothDevice, Void, BluetoothSocket>
{
	
	@Override
	protected BluetoothSocket doInBackground(BluetoothDevice... params)
	{
		BluetoothDevice btd = params[0];
		BluetoothSocket btdSocket = null;
		
		UUID uuid = btd.getUuids()[0].getUuid();
		try
		{
			btdSocket = btd.createRfcommSocketToServiceRecord(uuid);
			btdSocket.connect();
			Log.d(Constants.TAG, "OK: BT socket created");
		}
		catch (IOException e)
		{
			Log.d(Constants.TAG, "FAIL: No BT socket created");
			e.printStackTrace();
		}
		
		return btdSocket;
	}

	@Override
	protected void onPostExecute(BluetoothSocket result)
	{
		super.onPostExecute(result);
		SocketHolder.getMySH().setBluetoothSocket(result);
	}
}
