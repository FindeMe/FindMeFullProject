package com.nyx.fineme;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.nyx.fineme.helper.APIUrl;
import com.nyx.fineme.helper.BackgroundServices;
import com.nyx.fineme.helper.PostAction;
import com.nyx.fineme.helper.SharedPrefManager;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends Activity {

    int LAUNCH_SECOND_ACTIVITY = 1;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == LAUNCH_SECOND_ACTIVITY) {
            if(resultCode == Activity.RESULT_OK){
                findViewById(R.id.yes).setEnabled(false);
                findViewById(R.id.buy).setEnabled(false);
                findViewById(R.id.loading).setVisibility(View.VISIBLE);
                String code=data.getStringExtra("code");
                String password=data.getStringExtra("password");
                BackgroundServices.getInstance(this)
                        .setBaseUrl(APIUrl.SERVER)
                        .addPostParam("service" ,"reg_device")
                        .addPostParam("code" ,code)
                        .addPostParam("password" ,password)
                        .addPostParam("user_id" ,
                                SharedPrefManager.getInstance(this).getUserKey(
                                        SharedPrefManager.KEY_ID
                                ))
                        .CallPost(new PostAction() {
                            @Override
                            public void whenFinished(String status, String response) throws JSONException {
                                findViewById(R.id.yes).setEnabled(true);
                                findViewById(R.id.buy).setEnabled(true);
                                findViewById(R.id.loading).setVisibility(View.GONE);

                                Toast.makeText(MainActivity.this, new JSONObject(response).getString("message"), Toast.LENGTH_SHORT).show();

                                if(new JSONObject(response).getInt("status")==1) {

                                  finish();

                                }
                            }
                        });
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }//onActivityResult
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.no).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.yes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(MainActivity.this, ReadQRActivity.class);
                startActivityForResult(i, LAUNCH_SECOND_ACTIVITY);
            }
        });



    }
}
