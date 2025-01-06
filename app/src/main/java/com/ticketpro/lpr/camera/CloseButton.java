package com.ticketpro.lpr.camera;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

public class CloseButton extends ImageView {

	public interface OnCloseButtonListener {
		void onCloseButtonFocus(CloseButton b, boolean pressed);
		void onCloseButtonClick(CloseButton b);
	}

	private OnCloseButtonListener mListener;
	private boolean mOldPressed;

	public CloseButton(Context context) {
		super (context);
	}

	public CloseButton(Context context, AttributeSet attrs) {
		super (context, attrs);
	}

	public CloseButton(Context context, AttributeSet attrs,
			int defStyle) {
		super (context, attrs, defStyle);
	}

	public void setOnCloseButtonListener(OnCloseButtonListener listener) {
		mListener = listener;
	}

	 @Override
	 protected void drawableStateChanged() {
		 super.drawableStateChanged();
		 final boolean pressed = isPressed();
		 if (pressed != mOldPressed) {
			 if (!pressed) {
				 post(new Runnable() {
					 public void run() {
						 callShutterButtonFocus(pressed);
					 }
				 });
			 } else {
				 callShutterButtonFocus(pressed);
			 }
			 mOldPressed = pressed;
		 }
	 }

	 private void callShutterButtonFocus(boolean pressed) {
		 if (mListener != null) {
			 mListener.onCloseButtonFocus(this,pressed);
		 }
	 }

	 @Override
	 public boolean performClick() {
		 boolean result = super.performClick();
		 if (mListener != null) {
			 mListener.onCloseButtonClick(this);
		 }
		 return result;
	 }
}