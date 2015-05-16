package com.animalsabroad.animalsabroad;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Picture;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;


public class HomeActivity extends Activity {

    private SharedPreferences mPrefs;
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    public static final String uriTag = "fileUri";
    private Uri fileUri;
    Button pictureButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPrefs = getApplicationContext().getSharedPreferences("default",MODE_WORLD_READABLE);

        setContentView(R.layout.activity_home);
        pictureButton = (Button)findViewById(R.id.pictureButton);

        //Add the listeners to the buttons of the activities. Buttons need to be initiated first.
        addButtonListeners();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){

        if(requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE){
            switch(resultCode){
                case RESULT_OK:

                    /*
                    The Android API docs tell you to check the data argument for the URI
                    Where the image was saved. Well it's always null. I read that this is
                    intended when you pass a URI for the camera intent to save the image.
                    It seems the docs contradict the actual design.
                    As a solution we store the URI at the same time that we pass it off to
                    the intent and that way we can access the picture.
                    */

                    fileUri = Uri.parse(mPrefs.getString(uriTag,null));
                    try {
                        PictureUtils.rotateBitmap(fileUri, this);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    // Image captured and saved to fileUri specified in the Intent
                    //Toast.makeText(this, "Image saved to:\n" + fileUri, Toast.LENGTH_LONG).show();
                    Log.d("MyCameraApp", "Image saved to: "+fileUri.getPath());

                    Intent intent = new Intent(this, ImageDisplayActivity.class);
                    intent.setData(fileUri);
                    startActivity(intent);
                    break;
                case RESULT_CANCELED:
                    break;
                default:
                    break;
            }
        }
    }

    private void addButtonListeners(){
        pictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE); // create a file to save the image
                Log.d("MyCameraApp","fileUri path: "+ fileUri.getPath());
                mPrefs.edit().putString(uriTag,fileUri.toString()).commit();
                intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name

                startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
            }
        });
    }

    /** Create a file Uri for saving an image or video */
    private static Uri getOutputMediaFileUri(int type){
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /** Create a File for saving an image or video */
    private static File getOutputMediaFile(int type){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.


        Log.d("MyCameraApp", "A1");

        File path = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File mediaStorageDir = new File(path, "MyCameraApp");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.
        Log.d("MyCameraApp", "A2");

        Boolean isSDPresent = android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
        Log.d("MyCameraApp", "isSDPresent: " + isSDPresent);
        Log.d("MyCameraApp", "mediaStorageDir: " + mediaStorageDir.getPath());

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_"+ timeStamp + ".jpg");
        } else if(type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_"+ timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }
}
