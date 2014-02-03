package org.inversebit.ardusonar;

import java.io.IOException;
import java.io.InputStream;

import android.os.AsyncTask;
import android.util.Log;


public class GetSensorResult extends AsyncTask<Void, Void, Float>
{
	private GraphView gv;
	private InputStream is;
	
	public GetSensorResult(InputStream pIs, GraphView pGV)
	{
		is = pIs;
		gv = pGV;
	}
	
	@Override
	protected Float doInBackground(Void... params)
	{
		Integer result = Integer.valueOf(0);
		byte[] buffer = new byte[8];
		int bl = 0;
		int readByte;
		
		try
		{
			while((readByte = is.read()) != 65)
			{
				buffer[bl] = (byte) readByte;
				bl++;
			}
			
			result = Integer.parseInt((new String(buffer, 0, bl, "UTF-8")));
		}
		catch (IOException e)
		{
			e.printStackTrace();
			this.cancel(true);
		}

		return (float)result;
	}
	
	@Override
	protected void onPostExecute(Float result)
	{
		super.onPostExecute(result);

		Log.d("LS", "Gonna add: " + result);		
		
		TransmissionActivity.addPoint(TransmissionActivity.advanceAndGetDegree(), result);
		gv.invalidate();
		
	}
	
	@Override
	protected void onCancelled()
	{
		super.onCancelled();
		Log.d("LS", "GetSensorResult task cancelled");
	}
}
