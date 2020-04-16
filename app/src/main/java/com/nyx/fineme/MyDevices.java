package com.nyx.fineme;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.nyx.fineme.R;
import com.nyx.fineme.adapters.DevicesAdapter;
import com.nyx.fineme.helper.APIUrl;
import com.nyx.fineme.helper.BackgroundServices;
import com.nyx.fineme.helper.PostAction;
import com.nyx.fineme.helper.SharedPrefManager;
import com.nyx.fineme.models.DeviceRow;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MyDevices extends Activity {


    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }
    String pref_index="";
    ArrayList<DeviceRow> d;
    DevicesAdapter a;
    void refresh(){
        if(d!=null) {
            d.clear();
            a.notifyDataSetChanged();
            ;
        }
    findViewById(R.id.loading).setVisibility(View.VISIBLE);
    findViewById(R.id.no_items).setVisibility(View.GONE);

    BackgroundServices.getInstance(this)
            .setBaseUrl(APIUrl.SERVER)
            .addPostParam("service" ,"my_devices")
            .addPostParam("id" , SharedPrefManager.getInstance(this).getUserKey(SharedPrefManager.KEY_ID))
            .addPostParam("token" , SharedPrefManager.getInstance(this).getUserKey(SharedPrefManager.KEY_TOKEN))
            .CallPost(new PostAction() {
                @Override
                public void whenFinished(String status, String response) throws JSONException {
                    if(new JSONObject(response).getInt("status")==1) {
                        JSONArray arr = new JSONObject(response).getJSONArray("data");
                        if(arr.length()==0)
                            findViewById(R.id.no_items).setVisibility(View.VISIBLE);
                        else {
                           d = new ArrayList<>();
                            for (int i = 0; i < arr.length(); i++)
                                d.add(new DeviceRow(arr.getJSONObject(i).getString("id"),
                                        arr.getJSONObject(i).getString("address"),
                                        arr.getJSONObject(i).getString("info"),
                                        arr.getJSONObject(i).getInt("min_dist"),
                                        arr.getJSONObject(i).getString("pic") ,
                                        arr.getJSONObject(i).getString("family")));
                            RecyclerView rec = (RecyclerView) findViewById(R.id.list);
                            rec.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            a = new DevicesAdapter(d ,MyDevices.this) {
                                @Override
                                public void editPic(int position) {

pref_index = d.get(position).id;
                                    onProfileImageClick();



                                }
                            };
                            rec.setAdapter(a);

                        }
                        findViewById(R.id.loading).setVisibility(View.GONE);
                    }else{
                        Toast.makeText(MyDevices.this, new JSONObject(response).getString("message"), Toast.LENGTH_SHORT).show();
                    }
                }
            });
}
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_devices);
        if(FirebaseMessaging.getInstance()!=null)
        FirebaseMessaging.getInstance().subscribeToTopic("user_"+
                SharedPrefManager.getInstance(MyDevices.this).getUserID());
//        findViewById(R.id.go_logout).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                SharedPrefManager.getInstance(MyDevices.this).logout();
//                Intent i = new Intent(MyDevices.this ,SplashScreen.class);
//                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                startActivity(i);
//                finish();
//            }
//        });
        findViewById(R.id.go_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MyDevices.this ,MainActivity.class);
                startActivity(i);
            }
        });    findViewById(R.id.go_profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MyDevices.this ,ProfileActivity.class);
                startActivity(i);
            }
        });
        findViewById(R.id.go_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(d!=null) {
                    d.clear();
                    a.notifyDataSetChanged();
                    ;
                }
                findViewById(R.id.loading).setVisibility(View.VISIBLE);
                findViewById(R.id.no_items).setVisibility(View.GONE);

                BackgroundServices.getInstance(MyDevices.this)
                        .setBaseUrl(APIUrl.SERVER)
                        .addPostParam("service" ,"search")
                        .addPostParam("id" , SharedPrefManager.getInstance(getApplicationContext()).getUserKey(SharedPrefManager.KEY_ID))
                        .addPostParam("query" , ((EditText)findViewById(R.id.search_input)).getText().toString().trim())
                        .addPostParam("token" , SharedPrefManager.getInstance(getApplicationContext()).getUserKey(SharedPrefManager.KEY_TOKEN))
                        .CallPost(new PostAction() {
                            @Override
                            public void whenFinished(String status, String response) throws JSONException {
                                if(new JSONObject(response).getInt("status")==1) {
                                    JSONArray arr = new JSONObject(response).getJSONArray("data");
                                    if(arr.length()==0)
                                        findViewById(R.id.no_items).setVisibility(View.VISIBLE);
                                    else {
                                        d = new ArrayList<>();
                                        for (int i = 0; i < arr.length(); i++)
                                            d.add(new DeviceRow(arr.getJSONObject(i).getString("id"),
                                                    arr.getJSONObject(i).getString("address"),
                                                    arr.getJSONObject(i).getString("info"),
                                                    arr.getJSONObject(i).getInt("min_dist"),
                                                    arr.getJSONObject(i).getString("pic") ,
                                                    arr.getJSONObject(i).getString("family")));
                                        RecyclerView rec = (RecyclerView) findViewById(R.id.list);
                                        rec.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                                        a = new DevicesAdapter(d ,MyDevices.this) {
                                            @Override
                                            public void editPic(int position) {

                                                pref_index = d.get(position).id;
                                                onProfileImageClick();



                                            }
                                        };
                                        rec.setAdapter(a);

                                    }
                                    findViewById(R.id.loading).setVisibility(View.GONE);
                                }else{
                                    Toast.makeText(MyDevices.this, new JSONObject(response).getString("message"), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

    }


    public static final int REQUEST_IMAGE = 100;



    // my button click function
    void onProfileImageClick() {
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE
                        , Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            showImagePickerOptions();
                        } else {
                            // TODO - handle permission denied case
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    private void showImagePickerOptions() {
        ImagePickerActivity.showImagePickerOptions(this, new ImagePickerActivity.PickerOptionListener() {
            @Override
            public void onTakeCameraSelected() {
                launchCameraIntent();
            }

            @Override
            public void onChooseGallerySelected() {
                launchGalleryIntent();
            }
        });
    }

    private void launchCameraIntent() {
        Intent intent = new Intent(MyDevices.this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_IMAGE_CAPTURE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);

        // setting maximum bitmap width and height
        intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 1000);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 1000);

        startActivityForResult(intent, REQUEST_IMAGE);
    }

    private void launchGalleryIntent() {
        Intent intent = new Intent(MyDevices.this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);
        startActivityForResult(intent, REQUEST_IMAGE);
    }
    String imagePath = "";
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = data.getParcelableExtra("path");
                Toast.makeText(this, "تم التعديل بنجاح", Toast.LENGTH_SHORT).show();
                    imagePath = uri.getPath();
//                    d.get(pref_index).pic = imagePath;
//                    a.notifyDataSetChanged();
                BackgroundServices.getInstance(MyDevices.this)
                        .setBaseUrl(APIUrl.SERVER)
                        .addPostParam("id" , pref_index)
                        .addPostParam("service" ,"edit_pic")
                        .setFile("pic" ,new File(imagePath))
                        .CallPost(new PostAction() {
                            @Override
                            public void whenFinished(String status, String response) throws JSONException {
                                Log.d("kkkkk" ,response);
                             }
                        });

            }
        }
    }
}
