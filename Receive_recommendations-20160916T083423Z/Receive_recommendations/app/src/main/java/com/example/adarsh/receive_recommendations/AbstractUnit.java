package com.example.adarsh.receive_recommendations;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.facebook.ads.NativeAd;

/**
 * An AbstractUnit represents a responsive layout that adjusts its size relative to the device
 */
public abstract class AbstractUnit extends LinearLayout {

    public static final float SCREEN_TO_WIDTH_RATIO = 0.8f;
    private static final double WIDTH_TO_HEIGHT_RATIO = 2.11;

    /**
     * Constructs an AbstractUnit
     */
    public AbstractUnit(Context context) {
        super(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), getMeasuredHeight());
    }

    /**
     * Calculate and return the scaled size of the unit
     *
     * context the application context
     * return scaled size of the unit
     */
    public static int getWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return (int) (size.x * SCREEN_TO_WIDTH_RATIO);
    }

    /**
     * Scale the layout of the unit
     */
    protected void configureLayoutToAspectRatio(View view) {
        view.setLayoutParams(
                new LayoutParams(
                        LayoutParams.MATCH_PARENT,
                        (int) (getWidth(getContext()) / WIDTH_TO_HEIGHT_RATIO)));
    }

    /**
     * Configure the { NativeAd} for the UI //used in Appunit class which extends this class
     *
     */
    public void configure(NativeAd ad) {

    }


    /**
     * Set the state of the application
     *
     *  state    the state to apply
     * animated true for animation, false otherwise
     * retry    whether or not to retry an ads fetch
     */
    public void setState(AppUnitState state, boolean animated, boolean retry) {

    }
}
