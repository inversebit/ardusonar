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
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import org.inversebit.ardusonar.R;
import org.inversebit.ardusonar.CustomViews.GraphView;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;


public class TransmissionActivity extends Activity
{
	public static final int ORIGIN_X = 600;
	public static final int ORIGIN_Y = 400;
	public static final int INITIAL_RADIUS = 40;
	
	public static final int INITIAL_DEGREE = 180;
	public static final int MAX_POINTS = 360;
	private static final int MAX_DEGREES = 180;
	public static float[] points;
	public static int polledDegree;
	private static Timer tim;
	
	private static boolean clockwise;
	private BluetoothDevice btd;
	private BluetoothSocket btdSocket;
	private OutputStream btdOutStr;
	private InputStream btdInStr;
	private AsyncTask<Void, Void, Float> gsr;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		tim = new Timer();
		
		points = new float[MAX_POINTS];
		
		clockwise = true;
		polledDegree = INITIAL_DEGREE;
		
		for(int j = 0; j < points.length; j = j+2)
		{
			addPoint(advanceAndGetDegree(), INITIAL_RADIUS);
		}
		
		setContentView(R.layout.activity_transmission);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.transmission, menu);
		return true;
	}
	
	@Override
	protected void onStart()
	{
		super.onStart();
		
		btd = getIntent().getParcelableExtra("btd");
		Log.d(Constants.TAG, "UUID: " + btd.getUuids()[0]);
		UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
		
		try
		{
			btdSocket = btd.createRfcommSocketToServiceRecord(uuid);
		    btdSocket.connect();
		    btdOutStr = btdSocket.getOutputStream();
		    btdInStr = btdSocket.getInputStream();

		    Log.d(Constants.TAG, "Connected BTSocket");
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public void startPolling(View pView)
	{
		Log.d("LS", "startPolling");
		try
		{
			btdOutStr.write(80);
			Log.d("LS", "wrote byte");
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		tim.scheduleAtFixedRate(new TimerTask() 
		{
			@Override
			public void run() 
			{
				runOnUiThread(new Runnable() 
				{
					public void run() 
					{
						addPointAndRedraw();						
					}
				});
			}
		}, 0, 25);
	}
	
	protected void addPointAndRedraw() 
	{
		if(btdSocket.isConnected())
		{
			gsr = (new GetSensorResult(btdInStr, (GraphView)findViewById(R.id.graphView1))).execute();
		}		
	}
	
	public static void addPoint(int degree, float pRadius)
	{
		pRadius = pRadius * 2;
		Point p = new Point();
		double radians = Math.toRadians(degree);
		p.x = (float) (ORIGIN_X + pRadius * Math.cos(radians));
		p.y = (float) (ORIGIN_Y + pRadius * Math.sin(radians));
		points[(degree - INITIAL_DEGREE)*2] = p.x;
		points[(degree - INITIAL_DEGREE)*2+1] = p.y;
	}
	
	@Override
	public void onBackPressed()
	{
		super.onBackPressed();
		tim.cancel();
		closeComm();
	}
	
	@Override
	protected void onStop()
	{
		super.onStop();
		
		closeComm();
	}

	private void closeComm()
	{
		if(gsr != null)
		{
			gsr.cancel(true);
		}
		
		try
		{
			btdOutStr.close();
			btdInStr.close();
			btdSocket.close();
		}
		catch (IOException e)
		{
			Log.d(Constants.TAG, "Could not close BT socket");
			e.printStackTrace();
		}
	}

	public static int advanceAndGetDegree()
	{		
		if(clockwise)
		{
			polledDegree++;
			
			if(polledDegree == ((INITIAL_DEGREE + MAX_DEGREES)-1))
			{
				clockwise = false;
			}
		}
		else
		{
			polledDegree--;
			
			if(polledDegree == INITIAL_DEGREE)
			{
				clockwise = true;
			}
		}
		
		return polledDegree;
	}
	
}
