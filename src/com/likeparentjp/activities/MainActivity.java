package com.likeparentjp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.likeparentjp.R;
import com.likeparentjp.operations.LikeParentOps;
import com.likeparentjp.utils.LifecycleLoggingActivity;
import com.likeparentjp.utils.RetainedFragmentManager;
import com.likeparentjp.utils.Stack;

/**
 * MainActivity of Like Parent application
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
	 * Entity to control button color changing
	 */
	private LinearLayout mButtonContainer;
	/**
	 * Button to reset image
	 */
	private Button mResetImage;
	/**
	 * Button to analyze
	 */
	private Button mAnalyze;
	/**
     * Stacks to handle activity result correctly
     */
    private Stack<View> mExecutionViewStack = new Stack<View>();
    private Stack<Integer> mFlagStack = new Stack<Integer>();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //initialize view
        mButtonContainer = (LinearLayout) findViewById(R.id.btn_container);
        mResetImage = (Button) findViewById(R.id.btn_reset);
        mAnalyze = (Button) findViewById(R.id.btn_analyze);

        //set touch listener for color changing
        mResetImage.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN ) {
					mButtonContainer.setBackgroundResource(R.drawable.btn1);
                    return true;
                }
				if (event.getAction() == MotionEvent.ACTION_UP ) {
					mButtonContainer.setBackgroundResource(R.drawable.btn);
                    return true;
                }
				return false;
			}
		});

        //set on touch for color changing
        mAnalyze.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN ) {
					mButtonContainer.setBackgroundResource(R.drawable.btn2);
                    return true;
                }
				if (event.getAction() == MotionEvent.ACTION_UP ) {
					mButtonContainer.setBackgroundResource(R.drawable.btn);
                    return true;
                }
				return false;
			}
		});

        //handle configuration change
        handleConfigurationChange();
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
        
        //choose and set image
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
        
}
