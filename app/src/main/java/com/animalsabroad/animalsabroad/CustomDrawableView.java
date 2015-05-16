package com.animalsabroad.animalsabroad;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Picture;
import android.graphics.Rect;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.view.View;

import java.io.IOException;

/**
 * Created by Jimmy on 5/16/2015.
 */
public class CustomDrawableView extends View {
    private boolean imageHasBeenSet = false;
    private boolean shouldAddAccent = false;
    private Uri imageUri;
    private String accent;
    private Bitmap bmp;

    public CustomDrawableView(Context context) {
        super(context);
    }

    public CustomDrawableView(Context C, AttributeSet attribs){
        super(C, attribs);

        // Other setup code you want here
    }

    public CustomDrawableView(Context C, AttributeSet attribs, int defStyle){
        super(C, attribs, defStyle);

        // Other setup code you want here
    }

    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);

        Paint textPaint = new Paint();
        if(imageHasBeenSet) {
            Rect srcRect = new Rect(0,0,bmp.getWidth(),bmp.getHeight());
            canvas.drawBitmap(bmp, srcRect, PictureUtils.getReasonableDestRect(srcRect, new Rect(0,0, canvas.getWidth(), canvas.getHeight())), null);
        }
        textPaint.setColor(Color.WHITE);

        //For debugging, draws a line from top left to bottom right in white.
        //canvas.drawLine(0, 0, canvas.getWidth(), canvas.getHeight(), textPaint);

        // Other drawing functions here!!!

        if(shouldAddAccent){
            drawAccent(canvas, textPaint);
        }
    }

    private void drawAccent(Canvas canvas, Paint p){
        p.setTextSize(p.getTextSize()*15);
        int textWidth = (int)p.measureText(accent);
        int startingX = (canvas.getWidth()/2) - (textWidth/2);
        canvas.drawText(accent, startingX, PictureUtils.measureTextHeight(accent, p), p);
    }

    public void updateViewWithUri(Uri image, Activity act) throws Exception {
        imageUri = image;
        bmp = null;

        try {
            bmp = MediaStore.Images.Media.getBitmap(act.getContentResolver(), image);
        } catch (IOException e) {
            throw e;
        } if(bmp==null){
            throw new Exception("could not load bitmap.");
        }

        imageHasBeenSet = true;
        //this.invalidate();
    }

    public void updateWithAccent(){
        shouldAddAccent = true;
        accent = AccentDictionary.getRandomAccent();
    }
}
