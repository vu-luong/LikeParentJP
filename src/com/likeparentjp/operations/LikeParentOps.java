package com.likeparentjp.operations;

import java.lang.ref.WeakReference;

import android.app.Activity;
import android.util.Log;
import android.view.View;

/**
 * Class define operations for analyzing, resetting, etc of 
 * like parent application
 * @author NhanTQD
 * @author VuLA
 *
 */
public class LikeParentOps {
    /**
     * String use for debugging
     */
    private final String TAG = getClass().getSimpleName();
    /**
     * Weak reference of like parent main activity, use to enable garbage collection
     */
    private WeakReference<Activity> mActivity;
    
    public LikeParentOps(Activity mActivity) {
        this.mActivity = new WeakReference<Activity>(mActivity);
    }

    /**
     * Method to handle configuration change, re-set new activity reference
     * @param mActivity new activity instance
     */
    public void onConfigurationChange(Activity mActivity) {
        this.mActivity = new WeakReference<Activity>(mActivity);
    }
    
    /**
     * Method to choose and set image
     */
    public void chooseAndSetImage(View v) {
        Log.i(TAG, "choose and set image");
    }
    
    
}
