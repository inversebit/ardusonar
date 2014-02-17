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


import java.util.ArrayList;
import java.util.Iterator;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class ListDevices extends ListActivity
{	
	private BluetoothAdapter mBluetoothAdapter;
	private ArrayList<BluetoothDevice> devicesList;
	private String[] devicesNames;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		getListView().setBackgroundColor(Color.WHITE);
	}	
	
	@Override
	protected void onStart()
	{
		super.onStart();
		getBTDevices();
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		
		createSetShowDevicesList();				
	}

	private void createSetShowDevicesList()
	{
		extractDevicesNames();
		setListAdapter(createAdapter());
		setContentView(R.layout.activity_list_devices);
	}

	private void extractDevicesNames()
	{
		int i = 0;
		devicesNames = new String[devicesList.size()];
		
		Iterator<BluetoothDevice> btDeviceIterator = devicesList.iterator();
		while(btDeviceIterator.hasNext()){
			devicesNames[i] = btDeviceIterator.next().getName();
			i++;
		}
	}

	private ArrayAdapter<String> createAdapter()
	{		
		return new ArrayAdapter<String>(this,
										android.R.layout.simple_list_item_1,
										devicesNames);
	}


	private void getBTDevices()
	{
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		
		if (mBluetoothAdapter == null) 
		{
		    showNoBTAlertAndFinish();
		}
		else
		{
			if (!mBluetoothAdapter.isEnabled()) 
			{
				enableBluetooth();
			}

			getPairedDevices();
		}			
	}

	private void enableBluetooth()
	{
		Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
		startActivityForResult(enableBtIntent, Constants.REQUEST_ENABLE_BT);
	}
	
	private void getPairedDevices()
	{	
		devicesList = new ArrayList<BluetoothDevice>();
		devicesList.addAll(mBluetoothAdapter.getBondedDevices());
	}

	private void showNoBTAlertAndFinish()
	{
		AlertDialog alertDialog = new AlertDialog.Builder(ListDevices.this).create();  
		alertDialog.setCancelable(false);
        alertDialog.setTitle("Transformers");
        alertDialog.setMessage("Optimus Prime");
        alertDialog.setButton(	DialogInterface.BUTTON_NEUTRAL, 
        						getString(android.R.string.cancel), 
        						new OnClickListener() {									
									@Override
									public void onClick(DialogInterface dialog, int which)
									{		
										finish();
									}
								});
        alertDialog.show();
       
        
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_ENABLE_BT) 
        {
            if (resultCode != RESULT_OK) 
            {
            	showCannotEnableBTAlert();
            }
            else
            {
            	getPairedDevices();
            }
        }
	}

	private void showCannotEnableBTAlert()
	{
		AlertDialog alertDialog = new AlertDialog.Builder(this).create();  
		alertDialog.setCancelable(false);
        alertDialog.setTitle(getString(R.string.app_needs_bt_title));
        alertDialog.setMessage(getString(R.string.app_needs_bt_text));
        alertDialog.setButton(	DialogInterface.BUTTON_POSITIVE, 
        						getString(R.string.app_needs_bt_yes), 
        						new OnClickListener() {									
									@Override
									public void onClick(DialogInterface dialog, int which)
									{		
										getBTDevices();
									}
								});
        alertDialog.setButton(	DialogInterface.BUTTON_NEGATIVE, 
								getString(R.string.app_needs_bt_no), 
								new OnClickListener() {									
									@Override
									public void onClick(DialogInterface dialog, int which)
									{	
										finish();
									}
								});
        alertDialog.show();
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id)
	{
		super.onListItemClick(l, v, position, id);
		
		if(deviceIsConnectable()){
			launchTransmissionActivity(l, position);
		}
		else{
			showNotConnectableToast();
		}		
	}

	private void launchTransmissionActivity(ListView l, int position)
	{
		Intent intent = new Intent(l.getContext(), TransmissionActivity.class);
		intent.putExtra("btdsocket", getSelectedDeviceSocket());
		startActivity(intent);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.list_devices, menu);
		return true;
	}
	
}