package org.inversebit.ardusonar;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;


public class GraphView extends View
{
	private Paint paint;

	public GraphView(Context context)
	{
		super(context);
		initPaint();
	}

	public GraphView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		initPaint();
	}

	public GraphView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		initPaint();
	}
	
	private void initPaint()
	{
		paint = new Paint();
		paint.setColor(Color.RED);
		paint.setStrokeWidth(4.0f);
	}
	
	@Override
	protected void onDraw (Canvas canvas)
	{
		Log.d("LSGv", "onDraw");
		
		super.onDraw(canvas);
		
		canvas.drawCircle(	TransmissionActivity.ORIGIN_X,
							TransmissionActivity.ORIGIN_Y,
							10,
							paint);
		
		canvas.drawPoints(TransmissionActivity.points, paint);
	}

}
