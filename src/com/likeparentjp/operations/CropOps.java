package com.likeparentjp.operations;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;

import android.app.Activity;
import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
    /**
     * Max size of both dimension of image
     */
    private int IMAGE_MAX_SIZE = 1024;

    
    
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
        ContentResolver mContentResolver = mActivity.get().getContentResolver();
        InputStream in = null;
        Bitmap bitmap = null;
        try {
            in = mContentResolver.openInputStream(imageUri);

            //Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;

            BitmapFactory.decodeStream(in, null, o);
            in.close();

            int scale = 1;
            if (o.outHeight > IMAGE_MAX_SIZE || o.outWidth > IMAGE_MAX_SIZE ) {
                scale = (int) Math.pow(2, 
                        (int) Math.round(Math.log(IMAGE_MAX_SIZE 
                                / (double) Math.max(o.outHeight, o.outWidth)) 
                                / Math.log(0.5)));
            }

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            in = mContentResolver.openInputStream(imageUri);
            bitmap = BitmapFactory.decodeStream(in, null, o2);
            in.close();

        } catch (Exception e) {
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
                if (FaceDetection.facesFound > 0)
	                Toast.makeText(mActivity.get(),
	                        "Number of faces found : " + FaceDetection.facesFound,
	                        Toast.LENGTH_SHORT).show();
                else {
                	Toast.makeText(mActivity.get(),
	                        "No face detected, please try again with another picture",
	                        Toast.LENGTH_LONG).show();
                	
//                	mActivity.get().finish();
                }
                	
                
                
                mActivity.get().setCropImageBitmap(result);
            }
        }

    }


}
