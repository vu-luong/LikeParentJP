package com.likeparentjp.utils;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
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
    public static Bitmap setImageView(ImageView imageView, File cropFile) {
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
//        unscaledBitmap.recycle();
        
        //set image bitmap
        imageView.setImageBitmap(scaledBitmap);
        return scaledBitmap;
    }
    
    public static Bitmap rotateBitmap(Bitmap mBitmap, int times) {
        //totals degree
        int degrees = (90 * times) % 360;
        //rotate Bitmap
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        mBitmap = Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(), mBitmap.getHeight(), matrix, true);
        
        return mBitmap;
    }

}
