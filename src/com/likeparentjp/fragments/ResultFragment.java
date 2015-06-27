package com.likeparentjp.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.likeparentjp.R;
import com.likeparentjp.activities.MainActivity;
import com.likeparentjp.entities.CircleDisplay;
import com.likeparentjp.operations.LikeParentOps;

public class ResultFragment extends Fragment {
	
    private final String TAG = getClass().getSimpleName(); 
    
    private View mMainView;
    private LinearLayout mButtonContainer;
    private Button mShareButton;
    private Button mRetakeButton;
    private CircleDisplay mDadCircle, mMomCircle;
    
    /**
     * width of screen
     */
    private int V_WIDTH;
    
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");
        mMainView = inflater.inflate(R.layout.fragment_result, container, false);
        return mMainView;
    }
    
    @Override
    public void onStart() {
        super.onStart();
        initializeView();
    }
    
    
    private void initializeView() {
        Log.i(TAG, "initializeView");
        mButtonContainer = (LinearLayout) findViewById(R.id.btn_container);
        mShareButton = (Button) findViewById(R.id.btn_share);
        mRetakeButton = (Button) findViewById(R.id.btn_retake);
        mDadCircle = (CircleDisplay) findViewById(R.id.circleDisplayDad);
        mMomCircle = (CircleDisplay)findViewById(R.id.circleDisplayMom);
        
        //set the width of the screen for V_WIDTH
        V_WIDTH = getActivity().getWindowManager().getDefaultDisplay().getWidth();
        //calculate the result
        ((MainActivity) getActivity()).getOps().analyzeImage();;
        
        //set touch listener for color changing
        mShareButton.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN ) {
                    mButtonContainer.setBackgroundResource(R.drawable.btn1);
                    return true;
                }
                if (event.getAction() == MotionEvent.ACTION_UP ) {
                    mButtonContainer.setBackgroundResource(R.drawable.btn);
                    ((MainActivity) getActivity()).share();
                    
                    return true;
                }
                return false;
            }
        });

        //set on touch for color changing
        mRetakeButton.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN ) {
                    mButtonContainer.setBackgroundResource(R.drawable.btn2);
                    return true;
                }
                if (event.getAction() == MotionEvent.ACTION_UP ) {
                    mButtonContainer.setBackgroundResource(R.drawable.btn);
                    
                    ((MainActivity) getActivity()).retake();
                    
                    return true;
                }
                return false;
            }
        });
    }

    
    private void initialResultCircle(CircleDisplay circleDisplay, String who, float i) {

    	circleDisplay.setV_width(V_WIDTH);
    	circleDisplay.setAnimDuration(2000);
    	circleDisplay.setValueWidthPercent(55f);
    	circleDisplay.setFormatDigits(1);
    	circleDisplay.setDimAlpha(80);
    	circleDisplay.setType(who); 
    	
        
    	circleDisplay.setTouchEnabled(false);
    	circleDisplay.setUnit("%");
    	circleDisplay.setStepSize(0.5f);
    	circleDisplay.showValue(i, 100f, true);
    	
		
	}
    
    /**
     * Helper method to find view
     */
	public View findViewById(int id) {
        return mMainView.findViewById(id);
    }
	
	/**
	 * Post result after complete 
	 * @param percentDad
	 */
    public void postResult(float percentDad) {
        initialResultCircle(mDadCircle, "DAD", percentDad);
        initialResultCircle(mMomCircle, "MOM", 100 - percentDad);
    }

}
