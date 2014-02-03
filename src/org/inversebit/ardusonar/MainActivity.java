package org.inversebit.ardusonar;



import java.util.Set;

import org.inversebit.ardusonar.R;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;


public class MainActivity extends Activity
{
	public static final String TAG = "UP";
	
	private static final int REQUEST_ENABLE_BT = 1;
	private static final int REQUEST_DEVICE_NUM = 2;
	
	private BluetoothAdapter mBluetoothAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void startApp(View pView)
	{
		//Get BT adapter
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (mBluetoothAdapter == null) 
		{
		    ((TextView)findViewById(R.id.tvStatusMA)).setText("Bluetooth not supported");
		    Log.d(TAG, "Bluetooth not supported");
		}
		
		//Enable BT
		if (!mBluetoothAdapter.isEnabled()) 
		{
		    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
		    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
		}	
		else
		{
			getPairedDevices();
		}
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ENABLE_BT) 
        {
            if (resultCode == RESULT_OK) 
            {
            	getPairedDevices();
            }
            else
            {
            	((TextView)findViewById(R.id.tvStatusMA)).setText("Cannot enable BT");
            	Log.d(TAG, "Cannot enable BT");
            }
        }
        else
        {
        	if(requestCode == REQUEST_DEVICE_NUM)
        	{
        		Log.d(TAG, "RC: " + (resultCode-1));
        		Intent intent = new Intent(getApplicationContext(), TransmissionActivity.class);
        		intent.putExtra("btd", (BluetoothDevice)mBluetoothAdapter.getBondedDevices().toArray()[resultCode-1]);
        		startActivity(intent);
        	}
        }
    }

	private void getPairedDevices()
	{

		Intent intent = new Intent(getApplicationContext(), ListDevices.class);
		String devices = new String();
		
		Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
		// If there are paired devices
		if (pairedDevices.size() > 0) 
		{
		    // Loop through paired devices
		    for (BluetoothDevice device : pairedDevices) 
		    {
		    	
		        devices = devices + (device.getName() + ";");
		    }
		    
		    intent.putExtra("devices", devices);
		    startActivityForResult(intent, REQUEST_DEVICE_NUM);
		}	
		else
		{
			((TextView)findViewById(R.id.tvStatusMA)).setText("No BT devices paired");
        	Log.d(TAG, "No BT devices paired");
		}
	}

}
