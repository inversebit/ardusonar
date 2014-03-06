package org.inversebit.ardusonar;


import java.io.IOException;
import java.util.UUID;

import android.os.Bundle;
import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;


public class SocketGetter extends Activity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		BluetoothDevice btd = getIntent().getParcelableExtra(Constants.deviceExtra);
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
			btdSocket = null;
			Log.d(Constants.TAG, "FAIL: No BT socket created");
			e.printStackTrace();
		}
		
		SocketHolder.getMySH().setBluetoothSocket(btdSocket);
		
		Intent returnIntent = new Intent();
		setResult(RESULT_OK, returnIntent);     
		finish();
	}
}
