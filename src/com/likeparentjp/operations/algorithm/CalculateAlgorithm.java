package com.likeparentjp.operations.algorithm;

import android.graphics.Bitmap;
import android.util.Log;

public class CalculateAlgorithm implements LikeParentAlgorithm {

    @Override
    public float analyzeDadPercent(Bitmap dad, Bitmap mom, Bitmap child) {
    	
    	int color_dad = 
    			Math.abs(dad.getPixel(dad.getWidth()/2, dad.getHeight()/2));
    	
		int color_mom = 
				Math.abs(mom.getPixel(mom.getWidth()/2, mom.getHeight()/2));
		
		int color_kid = 
				Math.abs(child.getPixel(child.getWidth()/2, child.getHeight()/2));
		
		return cal(color_dad, color_mom, color_kid);
		
    }
    
    private float cal(int a, int b, int c) {
		// TODO Auto-generated method stub
		int m1 = Math.abs(c - b);
		int m2 = Math.abs(c - a);
		if (m1 + m2 == 0) return 50f; else {
			float r = m1*100.0f / (m1 + m2);
			if (r <= 17) return r + 17.9f;
			if (r >= 51 && r <= 55) return r - 14.5f;
			if (r >= 83) return r - 15.5f;
			
			Log.e("m1: ", m1 + "");
			Log.e("m2: ", m2 + "");
			
			Log.e("res: ", m2 + "");
			return (m1*100.0f / (m1 + m2));
		}
	}

}
