/**
Copyright (C) 2014  Inversebit

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.inversebit.ardusonar;


import java.io.IOException;
import java.util.UUID;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;


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
