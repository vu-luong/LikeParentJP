package com.likeparentjp.utils;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.media.FaceDetector;
import android.media.FaceDetector.Face;
import android.util.Log;

public class FaceDetection {
	private static final int MAX_FACES = 5;
	
	public static int facesFound;
	
	public static Bitmap detectFaces(Bitmap cameraBitmap) {
		if (null != cameraBitmap) {
			int width = cameraBitmap.getWidth();
			int height = cameraBitmap.getHeight();

			FaceDetector detector = new FaceDetector(width, height,
					MAX_FACES);
			Face[] faces = new Face[MAX_FACES];

			Bitmap bitmap565 = Bitmap.createBitmap(width, height,
					Config.RGB_565);
			Paint ditherPaint = new Paint();
			Paint drawPaint = new Paint();

			ditherPaint.setDither(true);
			drawPaint.setColor(Color.RED);
			drawPaint.setStyle(Paint.Style.STROKE);
			drawPaint.setStrokeWidth(2);

			Canvas canvas = new Canvas();
			canvas.setBitmap(bitmap565);
			canvas.drawBitmap(cameraBitmap, 0, 0, ditherPaint);

			facesFound = detector.findFaces(bitmap565, faces);
			PointF midPoint = new PointF();
			float eyeDistance = 0.0f;
			float confidence = 0.0f;

			Log.i("FaceDetector", "Number of faces found: " + facesFound);

			if (facesFound > 0) {
				for (int index = 0; index < facesFound; ++index) {
					faces[index].getMidPoint(midPoint);
					eyeDistance = faces[index].eyesDistance();
					confidence = faces[index].confidence();

					Log.i("FaceDetector", "Confidence: " + confidence
							+ ", Eye distance: " + eyeDistance
							+ ", Mid Point: (" + midPoint.x + ", " + midPoint.y
							+ ")");

					canvas.drawRect((int) midPoint.x - eyeDistance,
							(int) midPoint.y - eyeDistance, (int) midPoint.x
									+ eyeDistance, (int) midPoint.y
									+ eyeDistance, drawPaint);
				}
			}
			
			return bitmap565;
		} else return null;
	}
}
