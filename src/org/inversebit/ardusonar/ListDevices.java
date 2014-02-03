package org.inversebit.ardusonar;


import org.inversebit.ardusonar.R;

import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class ListDevices extends ListActivity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_devices);
		
		getListView().setBackgroundColor(Color.BLACK);
		
		Intent intent = getIntent();
		ArrayAdapter<String> aa = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1);
		
		String[] devices = ((String)intent.getCharSequenceExtra("devices")).split(";");
		
		for(String device:devices)
		{
			aa.add(device);
		}
		
		setListAdapter(aa);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.list_devices, menu);
		return true;
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id)
	{
		super.onListItemClick(l, v, position, id);
		
		Log.d(MainActivity.TAG, "" + position);
		
		Intent intent = this.getIntent();
		this.setResult(position + 1, intent); //+1 to avoid confussion with RESULT_CANCELLED
		finish();
	}
}