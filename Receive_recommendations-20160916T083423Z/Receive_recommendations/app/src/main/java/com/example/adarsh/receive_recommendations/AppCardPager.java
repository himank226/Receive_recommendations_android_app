package com.example.adarsh.receive_recommendations;
import android.content.Context;
import android.support.v4.view.ViewConfigurationCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

/**
 * An {@link ViewPager}
 */
public class AppCardPager extends ViewPager {

    public static final int OFF_SCREEN_PAGE_LIMIT = 3;

    private int mTouchSlop;
    private float mStartX;
    private float mStartY;

    private AppUnitState mAppUnitState;

    /**
     * Construct an AppCardPager
     */
    public AppCardPager(Context context) {
        super(context, null);
    }

    /**
     * Construct an AppCardPager with the given context and attributes
     *
     * context: the application context
     *  attrs :  a set of initial attributes
     */
    public AppCardPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        final ViewConfiguration configuration = ViewConfiguration.get(context);
        mTouchSlop = ViewConfigurationCompat.getScaledPagingTouchSlop(configuration) / 2;
    }

    public void setAppUnitState(AppUnitState appUnitState) {
        mAppUnitState = appUnitState;
    }

    /**
     * Determines if this class should handle the given event
     *
     * event: the event to handle
     * @return false to always handle events in this class, true otherwise
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (mAppUnitState == AppUnitState.Apps && getChildCount() > 1) {
            return handleTouch(event);
        }
        return false;
    }

    /**
     * Handle touch events to smooth out scrolling and distinguish between distinct tap events
     *
     * @param event
     * @return
     */
    private boolean handleTouch(MotionEvent event) {
        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
            mStartX = event.getX();
            mStartY = event.getY();
        } else if (action == MotionEvent.ACTION_UP) {
            float x = event.getX();
            float y = event.getY();

            if (Math.abs(mStartY - y) > (Math.abs(mStartX - x) + mTouchSlop)) {
                return true;
            }
        }
        return super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mAppUnitState == AppUnitState.Apps && getChildCount() > 1) {
            return super.onTouchEvent(event);
        }
        return false;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            child.measure(
                    widthMeasureSpec,
                    MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            int h = child.getMeasuredHeight();
            if (h > height) {
                height = h;
            }
        }

        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
