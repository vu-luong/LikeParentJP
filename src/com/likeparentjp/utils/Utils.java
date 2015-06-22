package com.likeparentjp.utils;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.widget.ImageView;

import com.likeparentjp.activities.CropActivity;
import com.likeparentjp.utils.ScalingUtilities.ScalingLogic;

/**
 * Class provide helper static methods
 * 
 * @author NhanTQD
 * @author VuLA
 *
 */
public class Utils {

    /**
     * Start pick image activity
     * @param activity Activity to start pick image activity
     * @param requestCode Request code
     */
    public static void pickImage(Activity activity, int requestCode) {
        Intent intentPickImage = new Intent(Intent.ACTION_GET_CONTENT).setType("image/*");
        activity.startActivityForResult(intentPickImage, requestCode);
    }
    
    public static void cropImage(Activity activity, Uri uri, Uri destination) {
        Intent intentCropImage = CropActivity.makeIntent(activity, uri, destination);
        activity.startActivityForResult(intentCropImage, CropActivity.REQUEST_CROP);
    }

    /**
     * Set image view for an Image View
     * 
     * @param imageView
     * @param cropFile 
     * @param data
     */
    public static void setImageView(ImageView imageView, File cropFile) {
        // set bitmap image to null
        imageView.setImageDrawable(null);
        
        //get target width and height
        int targetW = imageView.getWidth();
        int targetH = imageView.getHeight();
        
        //create a bitmap from file
        Bitmap unscaledBitmap = BitmapFactory.decodeFile(cropFile.getPath());
        //scale bitmap
        Bitmap scaledBitmap = ScalingUtilities.
                createScaledBitmap(unscaledBitmap, targetW, targetH, ScalingLogic.FIT);
        //recyle to improve performance
        unscaledBitmap.recycle();
        
        //set image bitmap
        imageView.setImageBitmap(scaledBitmap);
    }

    /**
     * Start crop photo from source uri, extract a cropped photo to destination uri
     * @param source source uri
     * @param destination destination uri
     * @param activity activity to start crop activity
     */
    public static void startCrop(Uri source, Uri destination, Activity activity) {
    }

}
