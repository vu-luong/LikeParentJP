package com.likeparentjp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.likeparentjp.R;
import com.likeparentjp.fragments.MainFragment;
import com.likeparentjp.fragments.ResultFragment;
import com.likeparentjp.operations.LikeParentOps;
import com.likeparentjp.utils.LifecycleLoggingActivity;
import com.likeparentjp.utils.RetainedFragmentManager;
import com.likeparentjp.utils.Stack;

/**
 * MainActivity of Like Parent application, play role View of MVP pattern
 * @author AnhLV
 * @author NhanTQD
 *
 */
public class MainActivity extends LifecycleLoggingActivity {
    /**
     * Retain Fragment Manager to handle configuration change
     */
    private RetainedFragmentManager mRetainedFragmentManager =
            new RetainedFragmentManager(getFragmentManager(), TAG);
    /**
     * Like parent operations
     */
    private LikeParentOps mOps;
    /**
     * TAG of LikeParentOps object state
     */
    private String OPERATION_TAG = "Like_parent";
	
    /**
     * Initial main fragment
     */
    private MainFragment mMainFragment;
    
    /**
     * Initial result fragment
     */
    
    private ResultFragment mResultFragment;
    
	/**
     * Stacks to handle activity result correctly
     */
    private Stack<View> mExecutionViewStack = new Stack<View>();
    private Stack<Integer> mFlagStack = new Stack<Integer>();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        if (findViewById(R.id.fragment_container) != null) {
            if (savedInstanceState == null) {
                //set initial fragment
                mMainFragment = new MainFragment();
                mResultFragment = new ResultFragment();
                
                getFragmentManager().beginTransaction()
                                    .add(R.id.fragment_container, mMainFragment)
                                    .commit();
            }
        }
        
        //handle configuration change
        handleConfigurationChange();
    }
    
    public void switchToResultFragment(){
    	getFragmentManager().beginTransaction()
        .replace(R.id.fragment_container, mResultFragment)
        .commit();
    }
    
    private void handleConfigurationChange() {
        if (mRetainedFragmentManager.firstTimeIn())  {
            Log.d(TAG, "First time onCreate() call");

            //first time in, create new Operation object
            mOps = new LikeParentOps(this);
            //store object reference
            mRetainedFragmentManager.put(OPERATION_TAG, mOps);
        } else {
            Log.d(TAG, "Not the first time");

            //reobtain object
            mOps = mRetainedFragmentManager.get(OPERATION_TAG);
            mOps.onConfigurationChange(this);
        }
        
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mOps.onActivityResult(requestCode, resultCode, data);
    }
    
    

    /**
     * This method run when user click on image's frame
     */
    public void chooseAndSetImage(View v) {
        //push view onto stack 
        mExecutionViewStack.push(v);
        
        //push flag onto stack
        if (v.getId() == R.id.bt_dad) {
            mFlagStack.push(LikeParentOps.FLAG_DAD);
        } else if (v.getId() == R.id.bt_mom) {
            mFlagStack.push(LikeParentOps.FLAG_MOM);
        } else if (v.getId() == R.id.bt_child) {
            mFlagStack.push(LikeParentOps.FLAG_CHILD);
        }
        
        //choose and set image, call to presenter
        mOps.chooseAndSetImage(v);
    }
    
    /**
     * Return recent view that send request to set photo
     * @return
     */
    public View getRecentViewRequest() {
        return mExecutionViewStack.pop();
    }
    
    /**
     * Return flag of recent view that send request
     * @return
     */
    public int getRecentFlagRequest() {
        return mFlagStack.pop();
    }

    /**
     * Reset image, delegate to mOps
     */
    public void resetImage() {
        mOps.resetImage();
    }

    /**
     * Analyze image, delegate to mOps
     */
    public void analyzeImage() {
        mOps.analyzeImage();
        switchToResultFragment();
    }
        
}