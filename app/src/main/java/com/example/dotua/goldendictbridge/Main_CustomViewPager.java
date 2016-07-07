package com.example.dotua.goldendictbridge;

/**
 * Created by dotua on 29-Jun-16.
 */

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by hegazy on 2/13/15.
 */
public class Main_CustomViewPager extends android.support.v4.view.ViewPager{
    private boolean enabled;

    public Main_CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.enabled = true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return enabled && super.onTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return enabled && super.onInterceptTouchEvent(event);
    }

    public void setPagingEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isPagingEnabled() {
        return enabled;
    }

}
