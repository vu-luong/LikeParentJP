package com.likeparentjp.operations;

import java.io.File;
import java.lang.ref.WeakReference;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.likeparentjp.R;
import com.likeparentjp.activities.CropActivity;
import com.likeparentjp.activities.MainActivity;
import com.likeparentjp.fragments.ResultFragment;
import com.likeparentjp.operations.algorithm.LikeParentAlgorithm;
import com.likeparentjp.utils.Utils;

/**
 * Class define operations for analyzing, resetting, etc of 
 * like parent application
 * @author NhanTQD
 * @author VuLA
 *
 */
public class LikeParentOps {
    /**
     * Helper flags
     */
    public static final int FLAG_DAD = 0;
    public static final int FLAG_MOM = 1;
    public static final int FLAG_CHILD = 2;
    /**
     * Request codes to start activities
     */
    private final int REQUEST_CHOOSE_PHOTO = 2312;
    private final int REQUEST_TAKE_PHOTO = 214;
    /**
     * String use for debugging
     */
    private final String TAG = getClass().getSimpleName();
    /**
     * Weak reference of like parent main activity, use to enable garbage collection
     */
    private WeakReference<Activity> mActivity;
    
    /**
     * String array option for alert dialog
     */
    private String[] mStringOption = { "Take Photo", "Choose from Gallery", "Cancel" };

    /**
     * Array Bitmap to analyze  
     */
    private Bitmap[] mDataBitmap;
    /**
     * Instance of Like parent algorithm, use Strategy pattern
     */
    private LikeParentAlgorithm mAlgorithm;
    
    /**
     * Construct new operations objects 
     */
    public LikeParentOps(Activity mActivity, LikeParentAlgorithm algorithm) {
        this.mActivity = new WeakReference<Activity>(mActivity);
        this.mDataBitmap = new Bitmap[3];
        this.mAlgorithm = algorithm;
    }

    /**
     * Method to handle configuration change, re-set new activity reference
     * @param mActivity new activity instance
     */
    public void onConfigurationChange(Activity mActivity) {
        this.mActivity = new WeakReference<Activity>(mActivity);
        reinitializeView();
    }
    
    public void reinitializeView() {
        ImageButton momImg = (ImageButton) mActivity.get().findViewById(R.id.bt_mom);
        momImg.setImageBitmap(mDataBitmap[FLAG_MOM]);
        
        ImageButton dadImg = (ImageButton) mActivity.get().findViewById(R.id.bt_dad);
        dadImg.setImageBitmap(mDataBitmap[FLAG_DAD]);
        
        ImageButton childImg = (ImageButton) mActivity.get().findViewById(R.id.bt_child);
        childImg.setImageBitmap(mDataBitmap[FLAG_CHILD]);
    }

    /**
     * Method to choose and set image
     */
    public void chooseAndSetImage(View v) {
        Log.i(TAG, "choose and set image");
        
        //build a choose dialog
        AlertDialog.Builder builder = new Builder(mActivity.get());
        builder.setTitle("Select a photo").setItems(mStringOption,
                new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                        case 0:                         //Take photo     
                            handleTakePhoto();
                            break;
                        case 1:                         //Choose from gallery
                            handleChooseFromGallery();
                            break;
                        default:
                            //dismiss the dialog
                            dialog.dismiss();
                            break;
                        }
                            
                    }
                })
                .show();
        
            
    }
    /**
     * Handle result from activity 
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CHOOSE_PHOTO) {
                beginCrop(data.getData());
            } else if (requestCode == REQUEST_TAKE_PHOTO) {
                Uri destination = Uri.fromFile(new File(mActivity.get().
                                    getExternalCacheDir(), "temporary.jpg"));
                beginCrop(destination);
            } else if (requestCode == CropActivity.REQUEST_CROP ) { 
                handleCrop(data);
            }
        } else {
            //something went wrong
            
        }
    }

    //handle take photo action
    private void handleTakePhoto() {
        Uri destination = Uri.fromFile(new File(mActivity.get().getExternalCacheDir(), "temporary.jpg"));
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(mActivity.
                get().getPackageManager()) != null) {
            
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                    destination);
            mActivity.get()
                     .startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);

        }

    }
    //handle choose photo action
    private void handleChooseFromGallery() {
        Log.i(TAG, "Choose image from gallery");
        Utils.pickImage(mActivity.get(), REQUEST_CHOOSE_PHOTO);
    }

    private void beginCrop(Uri source) {
        //crop image and save into a file on cache directory 
        Uri destination = Uri.fromFile(getTempCropFile());
        //start crop
        Utils.cropImage(mActivity.get(), source, destination);
    }
    
    public File getTempCropFile() {
        return new File(mActivity.get().getCacheDir(), "cropped");
    }
    

    /**
     * Handle intent data after cropped photo
     * @param data
     */
    private void handleCrop(Intent data) {
        //enqueue view from queue
        MainActivity mainActivity = (MainActivity) mActivity.get();
        //re-set bit map image for image button
        Bitmap resultBitmap = Utils.setImageView((ImageView) mainActivity.getRecentViewRequest(),
                    getTempCropFile());
        mDataBitmap[mainActivity.getRecentFlagRequest()] = resultBitmap;
        
    }
    
    /**
     * Reset images
     */
    public void resetImage() {
        // TODO reset here
    	
    	
    }

    /**
     * Analyze images
     */
    public void analyzeImage() {
        //analyze the percent look-a-like dad of child
        new AsyncTask<Bitmap[], Void, Float>() {
            @Override
            protected Float doInBackground(Bitmap[]... params) {
                Bitmap[] bitmap = params[0];
                Log.i(TAG, "analyzing");
                //return the analyze result
                return mAlgorithm.analyzeDadPercent(
                        bitmap[FLAG_DAD], bitmap[FLAG_MOM],
                        bitmap[FLAG_CHILD]);
            }
            
            @Override
            protected void onPostExecute(Float result) {
                //post back the result
                ((MainActivity) mActivity.get()).postResult(result);
            }
        }.execute(mDataBitmap);
        
        if (mDataBitmap[1] == null && mDataBitmap[2] == null && mDataBitmap[0] == null) {
            Log.i(TAG, "null");
        }
        
        //set 3 bitmaps from mainFragment to 3 views in resultFragment 
        reinitializeView(); 
    }
    
    
    public boolean allViewAreSet(){
    	return mDataBitmap[1] != null && mDataBitmap[2] != null && mDataBitmap[0] != null;
    }
    
    /**
     * use when recreate mainFragment
     */
    public void setDataBitmapToNull(){
    	mDataBitmap = new Bitmap[3];
    }
    
}
