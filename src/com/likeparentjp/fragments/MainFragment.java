package com.likeparentjp.fragments;

import com.likeparentjp.R;
import com.likeparentjp.activities.MainActivity;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainFragment extends Fragment {
    protected final String TAG = getClass().getSimpleName();
    private View mMainView;
    private LinearLayout mButtonContainer;
    private Button mResetImage;
    private Button mAnalyze;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        mMainView = inflater.inflate(R.layout.fragment_main, container, false);
        return mMainView;
    }
    
    @Override
    public void onStart() {
        super.onStart();
        initializeView();
    }
    private void initializeView() {
        mButtonContainer = (LinearLayout) findViewById(R.id.btn_container);
        mResetImage = (Button) findViewById(R.id.btn_reset);
        mAnalyze = (Button) findViewById(R.id.btn_analyze);
        
        
        //set 3 bitmaps to 3 views in mainFragment again
        ((MainActivity) getActivity()).getOps().resetView();
        
        
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
                    ((MainActivity) getActivity()).resetImage();
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
                    Log.i(TAG, "UPPPP");
                    mButtonContainer.setBackgroundResource(R.drawable.btn);
                    //TODO--
                    //check: if 3 views are set, then user can touch this button
                    if (((MainActivity) getActivity()).getOps().allViewAreSet())
                    {
                    	
                    	((MainActivity) getActivity()).analyzeImage();
                    } else {
                    	Toast.makeText(getActivity(), 
                    			"Please set pictures for all frames", 
                    			Toast.LENGTH_SHORT).show();
                    }
                    
                    	
                    
                    return true;
                }
                return false;
            }
        });
    }

    
    public View findViewById(int id) {
        return mMainView.findViewById(id);
    }
}
