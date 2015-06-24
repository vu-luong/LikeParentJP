package com.likeparentjp.operations;

import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.lang.ref.WeakReference;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.view.View;

import com.edmodo.cropper.CropImageView;
import com.likeparentjp.R;
import com.likeparentjp.activities.CropActivity;
import com.likeparentjp.utils.Utils;


public class CropOps {
    /**
     * String use for debugging
     */
    private final String TAG = getClass().getSimpleName();
    /**
     * Weak reference of crop activity, use to enable garbage collection
     */
    private WeakReference<CropActivity> mActivity;
    /**
     * Stored Bitmap
     */
    private Bitmap mStoredBitmap;
    
    /**
     * Count the number of rotate operations
     */
    private int mRotateCount = 0;
    
    
    public CropOps(CropActivity cropActivity) {
        this.mActivity = new WeakReference<CropActivity>(cropActivity);
    }


    public void onConfigurationChange(CropActivity cropActivity) {
        this.mActivity = new WeakReference<CropActivity>(cropActivity);
    }

    /**
     * Return stored bitmap of crop activity
     * @return
     */
    public Bitmap getStoredBitmap() {
        return mStoredBitmap;
    }

    /**
     * Set stored bitmap of crop activity
     * @param mStoredBitmap
     */
    public void setStoredBitmap(Bitmap mStoredBitmap) {
        this.mStoredBitmap = mStoredBitmap;
    }


    /**
     * Crop and save image 
     * @param v
     */
    public void cropAndSaveImage(View v) {
        Log.i(TAG, "Crop and save photo");
        if (mRotateCount % 4 != 0) {
            mStoredBitmap = Utils.rotateBitmap(mStoredBitmap, mRotateCount);
        }
        CropImageView mCiv = (CropImageView) mActivity.get().
                                            findViewById(R.id.CropImageView);
        mCiv.setBitmap(getStoredBitmap());
         
        Bitmap croppedBitmap = mCiv.getCroppedImage();
        //get uri :
        Uri saveUri = Uri.parse(mActivity.get().getIntent().
                            getStringExtra(CropActivity.DESTINATION_TAG));
        
        //saved image
        OutputStream outputStream = null;
        try {
            outputStream = mActivity.get().getContentResolver().openOutputStream(saveUri);
            croppedBitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        mActivity.get().setResult(
                Activity.RESULT_OK);
        mActivity.get().finish();        
    }


    public void rotateCropImage(View v) {
        Log.i(TAG, "rotate crop image");
        mRotateCount++;
    }
    

}
