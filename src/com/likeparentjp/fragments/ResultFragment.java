package com.likeparentjp.fragments;

import com.likeparentjp.R;
import com.likeparentjp.activities.MainActivity;
import com.likeparentjp.entities.CircleDisplay;

import android.app.Fragment;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.LinearLayout;

public class ResultFragment extends Fragment {
	
    private View mMainView;
    private LinearLayout mButtonContainer;
    private Button mShareButton;
    private Button mRetakeButton;
    private CircleDisplay mDadCircle, mMomCircle;
    
    /**
     * width of screen
     */
    private int v_width;
    
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        mMainView = inflater.inflate(R.layout.fragment_result, container, false);
        return mMainView;
    }
    
    @Override
    public void onStart() {
        super.onStart();
        initializeView();
    }
    
    
    private void initializeView() {
        mButtonContainer = (LinearLayout) findViewById(R.id.btn_container);
        mShareButton = (Button) findViewById(R.id.btn_share);
        mRetakeButton = (Button) findViewById(R.id.btn_retake);
        mDadCircle = (CircleDisplay) findViewById(R.id.circleDisplayDad);
        mMomCircle = (CircleDisplay)findViewById(R.id.circleDisplayMom);
        
    	v_width = getActivity().getWindowManager().getDefaultDisplay().getWidth();
        
    	initialResultCircle(mDadCircle, "DAD", 50);
    	initialResultCircle(mMomCircle, "MOM", 60);
    	
 
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
                    ((MainActivity) getActivity()).resetImage();
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
                    ((MainActivity) getActivity()).analyzeImage();
                    return true;
                }
                return false;
            }
        });
    }

    
    private void initialResultCircle(CircleDisplay circleDisplay, String who, int i) {

    	circleDisplay.setAnimDuration(2000);
    	circleDisplay.setValueWidthPercent(55f);
    	circleDisplay.setFormatDigits(1);
    	circleDisplay.setDimAlpha(80);
    	circleDisplay.setType(who); 
    	circleDisplay.setV_width(v_width);
    	
        
    	circleDisplay.setTouchEnabled(false);
    	circleDisplay.setUnit("%");
    	circleDisplay.setStepSize(0.5f);
    	circleDisplay.showValue(i, 100f, true);
		
	}

	private View findViewById(int id) {
        return mMainView.findViewById(id);
    }
}
