package com.likeparentjp.operations;

import java.io.File;
import java.lang.ref.WeakReference;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.likeparentjp.utils.Queue;
import com.likeparentjp.utils.Utils;
import com.soundcloud.android.crop.Crop;

/**
 * Class define operations for analyzing, resetting, etc of 
 * like parent application
 * @author NhanTQD
 * @author VuLA
 *
 */
public class LikeParentOps {
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
     * Queue to handle activity result correctly
     */
    private Queue<View> mExecutionViewQueue = new Queue<View>();
    
    /**
     * 
     * @param mActivity
     */
    public LikeParentOps(Activity mActivity) {
        this.mActivity = new WeakReference<Activity>(mActivity);
    }

    /**
     * Method to handle configuration change, re-set new activity reference
     * @param mActivity new activity instance
     */
    public void onConfigurationChange(Activity mActivity) {
        this.mActivity = new WeakReference<Activity>(mActivity);
    }
    
    /**
     * Method to choose and set image
     */
    public void chooseAndSetImage(View v) {
        Log.i(TAG, "choose and set image");
        
        //enqueue this view to know which view will display 
        //the return Bitmap
        mExecutionViewQueue.enqueue(v);

        
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
                // TODO handle request take photo
            } else if (requestCode == Crop.REQUEST_CROP) {
                handleCrop(data);
            }
        } else {
            //something went wrong
            
        }
    }

    //handle take photo action
    private void handleTakePhoto() {
        Uri destination = Uri.fromFile(new File(mActivity.get().getCacheDir(), "temporary"));
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
        Utils.pickImage(mActivity.get(), REQUEST_CHOOSE_PHOTO);
    }

    private void beginCrop(Uri source) {
        //crop image and save into a file on cache directory 
        Uri destination = Uri.fromFile(getTempCropFile());
        //start crop
        Utils.startCrop(source, destination, mActivity.get());
    }
    
    public File getTempCropFile() {
        return new File(mActivity.get().getCacheDir(), "cropped");
    }

    private void handleCrop(Intent data) {
        //enqueue view from queue
        ImageButton imageButton = (ImageButton) mExecutionViewQueue.dequeue();
        //re-set bit map image for image button
        Utils.setImageView(imageButton, getTempCropFile());
        
    }
    
    
    
}
