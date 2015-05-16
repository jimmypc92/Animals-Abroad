package com.animalsabroad.animalsabroad;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import android.net.Uri;
import android.view.SurfaceView;
import android.widget.ImageView;


public class ImageDisplayActivity extends Activity {

    private CustomDrawableView imageDisplayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_display);

        Intent intent = getIntent();
        Uri path = intent.getData();

        imageDisplayer = (CustomDrawableView)findViewById(R.id.imageDisplayer);
        //If your using imageview rather than surface view you just set the
        //Uri of the image.
        //imageDisplayer.setImageURI(path);
        try {
            imageDisplayer.updateViewWithUri(path, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        imageDisplayer.updateWithAccent();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_image_display, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
