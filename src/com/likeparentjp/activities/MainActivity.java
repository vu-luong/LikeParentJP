package com.likeparentjp.activities;

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
    
    /**
     * This method run when user click on Dad image's frame
     */
    public void chooseAndSetDadImage(View v) {
        mOps.chooseAndSetDadImage(v);
    }
    /**
     * This method run when user click on Mom image's frame
     */
    public void chooseAndSetMomImage(View v) {
        mOps.chooseAndSetMomImage(v);
    }
    
    /**
     * This method run when user click on Mom image's frame
     */
    public void chooseAndSetChildImage(View v) {
        mOps.chooseAndSetChildImage(v);
    }

}
