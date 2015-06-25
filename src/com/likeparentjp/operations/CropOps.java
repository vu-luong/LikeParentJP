package com.likeparentjp.operations;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.ref.WeakReference;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.edmodo.cropper.CropImageView;
import com.likeparentjp.R;
import com.likeparentjp.activities.CropActivity;
import com.likeparentjp.utils.FaceDetection;
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
     * Display bitmap
     */
    private Bitmap mDisplayBitmap;
    
    /**
     * Count the number of rotate operations
     */
    private int mRotateCount = 0;
    
    /**
     * @param cropActivity
     */
    private DetectFaceTask mDetectFaceTask;

    
    
    public CropOps(CropActivity cropActivity) {
        this.mActivity = new WeakReference<CropActivity>(cropActivity);
        mDetectFaceTask = new DetectFaceTask(mActivity.get());
    }


    public void onConfigurationChange(CropActivity cropActivity) {
        this.mActivity = new WeakReference<CropActivity>(cropActivity);
    }


        /**
     * Crop and save image 
     * @param v
     */
    public void cropAndSaveImage(View v) {
        //rotate the original bitmap
        if (mRotateCount % 4 != 0) {
            mStoredBitmap = Utils.rotateBitmap(mStoredBitmap, mRotateCount);
        }
        //get cropped bitmap
        CropImageView mCiv = (CropImageView) mActivity.get()
                                .findViewById(R.id.CropImageView);
        mCiv.setBitmap(mStoredBitmap);
         
        Bitmap croppedBitmap = mCiv.getCroppedImage();
        //get uri
        Uri saveUri = Uri.parse(mActivity.get().getIntent()
                        .getStringExtra(CropActivity.DESTINATION_TAG));
        
        //saved image
        OutputStream outputStream = null;
        try {
            outputStream = mActivity.get().getContentResolver().openOutputStream(saveUri);
            croppedBitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        
        //cancel the face detect asynctask if it is still running
        mDetectFaceTask.cancel(true);
        
        //finish activity, return result
        mActivity.get().setResult(
                Activity.RESULT_OK);
        mActivity.get().finish();        
    }


    public void rotateCropImage(View v) {
        Log.i(TAG, "rotate crop image");
        mRotateCount++;
    }

    /**
     * Get display bitmap that had been face detected
     * @return
     */
    public Bitmap getDisplayBitmap() {
        return mDisplayBitmap;
    }

    /**
     * Set display bitmap that had been face detected
     * @param mDisplayBitmap
     */
    public void setDisplayBitmap(Bitmap mDisplayBitmap) {
        this.mDisplayBitmap = mDisplayBitmap;
    }

    /**
     * Set up stored bit map and detecting face
     * @param mStoredBitmap
     */
    public void setUpCropImage() {
     // get image uri
        Uri imageUri = mActivity.get()
                                .getIntent()
                                .getData();
        Bitmap bitmap = null;
        try {
            // get a bitmap image from uri
            bitmap = MediaStore.Images.Media.getBitmap(
                            mActivity.get().getContentResolver(), imageUri);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (bitmap != null) {
            mActivity.get().setCropImageBitmap(bitmap);
            this.mStoredBitmap = bitmap;
            mDetectFaceTask.execute(bitmap);
        }
    }

    private class DetectFaceTask extends AsyncTask<Bitmap, Void, Bitmap> {
        
        public DetectFaceTask(CropActivity activity) {
            mActivity.get().showProgressDialog("Detecting Faces...", "Please wait");
        }

        @Override
        protected Bitmap doInBackground(Bitmap... params) {
            return FaceDetection.detectFaces(params[0]);
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            if (mActivity.get() != null) {
                mActivity.get().dismissProgressDialog();
                //toast the number of faces detected
                Toast.makeText(mActivity.get(),
                        "Number of faces found : " + FaceDetection.facesFound,
                        Toast.LENGTH_SHORT).show();
                mActivity.get().setCropImageBitmap(result);
            }
        }

    }


}
