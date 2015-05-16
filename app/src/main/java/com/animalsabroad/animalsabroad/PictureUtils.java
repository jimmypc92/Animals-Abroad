package com.animalsabroad.animalsabroad;

/**
 * Created by Jimmy on 5/16/2015.
 */

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class PictureUtils {

    public static Uri JPGtoPNGFormatCopy(Uri jpgImage, Activity act) throws Exception {

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
}
