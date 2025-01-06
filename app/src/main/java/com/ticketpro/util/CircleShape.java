package com.ticketpro.util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class CircleShape extends View {
	
	 	private final float x;
	    private final float y;
	    private final int r;
	    private final boolean isFilled;
	    private final int circleGap;
	    
	    private final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	    
	    public CircleShape(Context context, float x, float y, int r, int circleGap,boolean isFilled) {
	        super(context);
	        this.x = x;
	        this.y = y;
	        this.r = r;
	        this.isFilled=isFilled;
	        this.circleGap=circleGap;
	    }

	    @Override
	    protected void onDraw(Canvas canvas) {
	        super.onDraw(canvas);
	        mPaint.setStyle(Paint.Style.STROKE);
	        if(isFilled)
	        	mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
	        
	        mPaint.setColor(Color.YELLOW);
	        canvas.drawCircle(x, y, r, mPaint);
	        canvas.drawCircle(x, y, r-circleGap, mPaint);
        	invalidate();
	    }
}
