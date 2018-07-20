package kavasam.makkal.com.mkkalkavasam;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import static android.app.Activity.RESULT_OK;

public class PublishActivity extends Fragment implements ActivityCompat.OnRequestPermissionsResultCallback,View.OnClickListener{

    public PublishActivity() {
    }
    private static final int SELECT_VIDEO = 3;
    private String selectedPath;
    private Button buttonChoose;
    private TextView textView;
    ImageView imageview_micro;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.publish, container, false);

        buttonChoose = (Button) rootView.findViewById(R.id.buttonChoose);

        textView = (TextView) rootView.findViewById(R.id.textView);
         imageview_micro = (ImageView)rootView.findViewById(R.id.img);

        buttonChoose.setOnClickListener(this);


        isStoragePermissionGranted();


        return rootView;
    }

    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (getActivity().checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("___________","Permission is granted");
                return true;
            } else {

                Log.v("_________","Permission is revoked");
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v("_________","Permission is granted");
            return true;
        }
    }
    private void chooseVideo() {


        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select a Video "), SELECT_VIDEO);




    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
            Log.v("_____","Permission: "+permissions[0]+ "was "+grantResults[0]);
            //resume tasks needing this permission
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_VIDEO) {
                System.out.println("SELECT_VIDEO");
                Uri selectedImageUri = data.getData();
               selectedPath = getPath(selectedImageUri);
               textView.setText(selectedPath);
               path(selectedPath);
            }
        }
    }


    public void path(String path){
        String filePath = path; //change the location of your file!

        //ImageView imageview_mini = (ImageView)findViewById(R.id.thumbnail_mini);

        Bitmap bmThumbnail;

//MICRO_KIND, size: 96 x 96 thumbnail
        bmThumbnail = ThumbnailUtils.createVideoThumbnail(filePath, MediaStore.Images.Thumbnails.MICRO_KIND);
        imageview_micro.setImageBitmap(bmThumbnail);

// MINI_KIND, size: 512 x 384 thumbnail
      //  bmThumbnail = ThumbnailUtils.createVideoThumbnail(filePath, Thumbnails.MINI_KIND);
      //  imageview_mini.setImageBitmap(bmThumbnail);
    }

    public String getPath(Uri uri) {

        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getActivity().getContentResolver().query(
                android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
        cursor.close();

        return path;
    }
    @Override
    public void onClick(View v) {
        if (v == buttonChoose) {
            chooseVideo();
        }

    }
}
