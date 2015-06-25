package com.likeparentjp.activities;

import java.io.IOException;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.edmodo.cropper.CropImageView;
import com.likeparentjp.R;
import com.likeparentjp.operations.CropOps;
import com.likeparentjp.utils.LifecycleLoggingActivity;
import com.likeparentjp.utils.RetainedFragmentManager;

/**
 * @author applehouse
 */
public class CropActivity extends LifecycleLoggingActivity {
    public static final int REQUEST_CROP = 234221;
    /**
     * Key to find destination uri to save cropped image
     */
    public static final String DESTINATION_TAG = "i'm yours";
    /**
     * Retain Fragment Manager to handle configuration change
     */
    private RetainedFragmentManager mRetainedFragmentManager = new RetainedFragmentManager(
            getFragmentManager(), TAG);

    /**
     * Crop image view to crop image
     */
    private CropImageView mCropImageView;
    /**
     * Progress face detecting dialog
     */
    private ProgressDialog mProgressDialog;
    /**
     * Operation of this view, play role presenter in pattern
     */
    private CropOps mOps;

    /**
     * Function button
     */
    private LinearLayout mContainerButton;
    private Button mCropButton;
    private Button mRotateButton;
    private String OPERATION_TAG = "crops_tag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop);
        // get Crop Image View reference and set up crop option
        mCropImageView = (CropImageView) findViewById(R.id.CropImageView);
        mCropImageView.setFixedAspectRatio(true);
        mCropImageView.setAspectRatio(CropImageView.DEFAULT_ASPECT_RATIO_X,
                CropImageView.DEFAULT_ASPECT_RATIO_Y);

        // initialize button features
        initialize();

        // handle configuration changes
        handleConfigurationChange();

    }

    private void handleConfigurationChange() {
        if (mRetainedFragmentManager.firstTimeIn()) {
            Log.d(TAG, "First time onCreate() call");

            // first time in, create new Operation object
            mOps = new CropOps(this);
            // store object reference
            mRetainedFragmentManager.put(OPERATION_TAG, mOps);
            // setup crop image
            setupCropImage();
            
        } else {
            Log.d(TAG, "Not the first time");

            // reobtain object
            mOps = mRetainedFragmentManager.get(OPERATION_TAG);
            mOps.onConfigurationChange(this);
            
            //re-set the display bitmap Uri imageUri = getIntent().getData();
            setCropImageBitmap(mOps.getDisplayBitmap());
        }
    }

    private void initialize() {
        mContainerButton = (LinearLayout) findViewById(R.id.btn_container);
        mCropButton = (Button) findViewById(R.id.button_crop);
        mRotateButton = (Button) findViewById(R.id.button_rotate);

        mCropButton.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    mContainerButton.setBackgroundResource(R.drawable.btn2);
                    return true;
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    mContainerButton.setBackgroundResource(R.drawable.btn);
                    cropAndSaveImage(v);
                    return true;
                }
                return false;
            }
        });

        mRotateButton.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    mContainerButton.setBackgroundResource(R.drawable.btn1);
                    return true;
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    mContainerButton.setBackgroundResource(R.drawable.btn);
                    rotateCropImage(v);
                    return true;
                }
                return false;
            }
        });
        
        

    }

    private void setupCropImage() {
        // get image uri
        Uri imageUri = getIntent().getData();
        Bitmap bitmap = null;
        try {
            // get a bitmap image from uri
            bitmap = MediaStore.Images.Media.getBitmap(
                            getContentResolver(), imageUri);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (bitmap != null) {
            setCropImageBitmap(bitmap);
            mOps.setUpCropImage(bitmap);
        }
    }

    /**
     * Set image bit map to this crop image view
     * 
     * @param bitmap
     */
    public void setCropImageBitmap(Bitmap bitmap) {
        mCropImageView.setImageBitmap(bitmap);
        mOps.setDisplayBitmap(bitmap);
    }

    /**
     * Crop and save image when press crop button, delegate to CropOps instance
     * 
     * @param v
     */
    public void cropAndSaveImage(View v) {
        mOps.cropAndSaveImage(v);
    }

    /**
     * onClick method to rotate crop image
     * 
     * @param v
     */
    public void rotateCropImage(View v) {
        mCropImageView.rotateImage(90);
        mOps.rotateCropImage(v);
    }

    /**
     * Static method to return a intent to start Crop activity,
     * shield client from implementation detail
     * 
     * @param activity
     * @param uri
     *            uri to image file to save in after cropping
     * @return Intent to start Crop Activity
     */
    public static Intent makeIntent(Context activity, Uri uri, Uri destination) {
        Intent intent = new Intent(activity, CropActivity.class);
        intent.setData(uri);
        intent.putExtra(DESTINATION_TAG, destination.toString());
        return intent;
    }
    
        
    public void showProgressDialog(String title, String Message) {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle(title);
        mProgressDialog.setMessage(Message);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.show();
    }
    
    public void dismissProgressDialog() {
        if (mProgressDialog != null)
            if (mProgressDialog.isShowing())
                mProgressDialog.dismiss();
    }
    
    @Override
    protected void onDestroy() {
        dismissProgressDialog();
        super.onDestroy();
    }
    
    
    
}
