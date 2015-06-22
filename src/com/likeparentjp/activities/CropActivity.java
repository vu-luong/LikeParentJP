package com.likeparentjp.activities;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.ref.WeakReference;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;

import com.edmodo.cropper.CropImageView;
import com.likeparentjp.R;
import com.likeparentjp.utils.FaceDetection;
import com.likeparentjp.utils.LifecycleLoggingActivity;

/**
 * 
 * @author applehouse
 *
 */
public class CropActivity extends LifecycleLoggingActivity {
    public static final int REQUEST_CROP = 234221;
    /**
     * Key to find destination uri to save cropped image 
     */
    private static final String DESTINATION_TAG = "i'm yours";
    /**
     * Crop image view to crop image
     */
    private CropImageView mCropImageView;
    
    /**
     * Stored Bitmap
     */
    private Bitmap mStoredBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop);
        //get Crop Image View reference
        mCropImageView = (CropImageView) findViewById(R.id.CropImageView);
        //set crop image
        setupCropImage();
    }

    private void setupCropImage() {
        //set up crop option
        
        mCropImageView.setFixedAspectRatio(true);
        mCropImageView.setAspectRatio(CropImageView.DEFAULT_ASPECT_RATIO_X,
                                      CropImageView.DEFAULT_ASPECT_RATIO_Y);
        //get image uri
        Uri imageUri = getIntent().getData();
        Bitmap bitmap = null;
        try {
            //get a bitmap image from uri
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        if (bitmap != null) {
            mStoredBitmap = bitmap;
            setCropImageBitmap(bitmap);
            new DetectFaceTask(this).execute(bitmap);
        }
    }
    
    /**
     * Set image bit map to this crop image view
     * @param bitmap
     */
    private void setCropImageBitmap(Bitmap bitmap) {
        mCropImageView.setImageBitmap(bitmap);
    }
    
    public void cropAndSaveImage(View v) {
        mCropImageView.setBitmap(mStoredBitmap);
        //TODO - consider using another thread
        Bitmap croppedBitmap = mCropImageView.getCroppedImage();
        //get uri :
        Uri saveUri = Uri.parse(getIntent().getStringExtra(DESTINATION_TAG));
        
        //saved image
        OutputStream outputStream = null;
        try {
            outputStream = getContentResolver().openOutputStream(saveUri);
            croppedBitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        setResult(RESULT_OK);
        finish();
    }
    
    /**
     * onClick method to rate crop image
     * @param v
     */
    public void rotateCropImage(View v) {
        mCropImageView.rotateImage(90);
    }
    
    /**
     * Static method to return a intent to start this activity (Crop activity),
     * shield client from implementation detail
     * @param activity
     * @param uri
     * @return Intent to start Crop Activity
     */
    public static Intent makeIntent(Activity activity, Uri uri, Uri destination) {
        Intent intent = new Intent(activity, CropActivity.class);
        intent.setData(uri);
        intent.putExtra(DESTINATION_TAG, destination.toString());
        return intent;
    }
    
    
    private static class DetectFaceTask extends AsyncTask<Bitmap, Void, Bitmap> {
        private WeakReference<Activity> mActivity;
        
        public DetectFaceTask(Activity activity) {
            this.mActivity = new WeakReference<Activity>(activity);
        }
        
        @Override
        protected Bitmap doInBackground(Bitmap... params) {
            return FaceDetection.detectFaces(params[0]);
        }
        
        @Override
        protected void onPostExecute(Bitmap result) {
            CropActivity activity = (CropActivity) mActivity.get();
            activity.setCropImageBitmap(result);
        }
        
    }
    
    

}
