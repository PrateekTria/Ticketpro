package com.ticketpro.util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.view.View;

public class MarkerView extends View {
	
	 	private float x;
	    private float y;
	    private int radius;
	    private String alpha;
	    private int color=Color.BLUE;
	    private int defaultColor=Color.BLUE;
	    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	    
	    public MarkerView(Context context, float x, float y, int radius,String alpha,int color) {
	        super(context);
	        this.x = x;
	        this.y = y;
	        this.radius = radius;
	        this.alpha=alpha;
	        this.color=color;
	        this.defaultColor=color;
	    }
	    
	    @Override
		protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
	    	setMeasuredDimension(this.radius * 2, this.radius * 2);
		}
	    

	    @Override
	    protected void onDraw(Canvas canvas) {
	        super.onDraw(canvas);
	        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
	        mPaint.setColor(this.color);
	        canvas.drawCircle(x, y, radius, mPaint);
	       
	        mPaint.setTextAlign(Align.CENTER);
	        if(this.radius > 5)
	        	mPaint.setTextSize(this.radius - 5);
	        
	        mPaint.setStyle(Paint.Style.STROKE);
	        mPaint.setColor(Color.WHITE);
	        canvas.drawText(this.alpha, x, y+((this.radius-5)/2), mPaint);
	        requestLayout();
	        invalidate();
	    }
	    
	    public void drawSelected(){
	    	this.color=Color.GREEN;
	    	this.refreshDrawableState();
	    }
	    
	    public void drawNormal(){
	    	this.color=this.defaultColor;
	    	this.refreshDrawableState();
	    }

	    public void setPosition(int x,int y){
			this.x=x;
			this.y=y;
			this.refreshDrawableState();
		}
	    
}
