package com.animalsabroad.animalsabroad;

/**
 * Created by Jimmy on 5/16/2015.
 */

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class PictureUtils {

    static Uri JPGtoPNGFormatCopy(Uri jpgImage, Activity act) throws Exception {

        String pngImage = jpgImage.getPath().replace(".jpg",".png");

        Bitmap bmp = null;
        try {
            bmp = MediaStore.Images.Media.getBitmap(act.getContentResolver(), jpgImage);
        } catch (IOException e) {
            throw e;
        }

        if(bmp==null){
            throw new Exception("could not load bitmap.");
        }

        try {
            FileOutputStream out = new FileOutputStream(new File(pngImage));
            bmp.compress(Bitmap.CompressFormat.PNG, 100, out); //100-best quality
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


        Log.d("PictureUtils", "pngImageURI being returned is : " + pngImage);
        return Uri.parse(pngImage);
    }

    public static Rect getReasonableDestRect(Rect src, Rect maxDest){
        double widthRatio = (float)maxDest.width()/(float)src.width();
        double heightRatio = (float)maxDest.height()/(float)src.height();

        if(widthRatio < heightRatio){
            return new Rect(0,0, (int)(src.width()*widthRatio), (int)(src.height()*widthRatio));
        }
        else{
            return new Rect(0,0, (int)(src.width()*heightRatio), (int)(src.height()*heightRatio));
        }
    }

    //This implementation rotates the bitmap clockwise 90 degrees.
    public static void rotateBitmap(Uri picturePath, Activity act) throws Exception {

        Bitmap bmp = null;
        try {
            bmp = MediaStore.Images.Media.getBitmap(act.getContentResolver(), picturePath);
        } catch (IOException e) {
            throw e;
        }
        new File(picturePath.getPath()).delete();
        if(bmp==null){
            throw new Exception("could not load bitmap.");
        }

        Matrix m = new Matrix();
        int neededRotation = 90;
        m.postRotate(neededRotation);

        bmp = Bitmap.createBitmap(bmp,
                0, 0, bmp.getWidth(), bmp.getHeight(),
                m, true);

        try {
            FileOutputStream out = new FileOutputStream(new File(picturePath.getPath()));
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, out); //100-best quality
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return;
    }

    public static int neededRotation(File ff)
    {
        try
        {

            ExifInterface exif = new ExifInterface(ff.getAbsolutePath());
            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

            if (orientation == ExifInterface.ORIENTATION_ROTATE_270)
            { return 270; }
            if (orientation == ExifInterface.ORIENTATION_ROTATE_180)
            { return 180; }
            if (orientation == ExifInterface.ORIENTATION_ROTATE_90)
            { return 90; }
            return 90;

        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return 0;
    }

    public static int measureTextHeight(String text, Paint p){
        Rect measureRect = new Rect();
        p.getTextBounds(text,0,text.length(),measureRect);
        return measureRect.height();
    }
}
