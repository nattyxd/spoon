package uk.ac.aston.baulchjn.mobiledev.spoon.helper;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

    public class NonScrollableViewPager extends ViewPager {

        public NonScrollableViewPager(Context context) {
            super(context);
        }

        public NonScrollableViewPager(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        @Override
        public boolean onInterceptTouchEvent(MotionEvent event) {
            // Never allow swiping to switch between pages
            return false;
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            // Never allow swiping to switch between pages
            return false;
        }
    }