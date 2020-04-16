package com.nyx.fineme;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.nyx.fineme.helper.APIUrl;
import com.nyx.fineme.helper.BackgroundServices;
import com.nyx.fineme.helper.PostAction;
import com.nyx.fineme.helper.SharedPrefManager;

import org.json.JSONException;
import org.json.JSONObject;

public class ProfileActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        findViewById(R.id.go_about).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://d2.scit.co/findme/admin/index.php"));
                startActivity(browserIntent);
            }
        });
        findViewById(R.id.go_logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPrefManager.getInstance(ProfileActivity.this).logout();
                Intent i = new Intent(ProfileActivity.this ,SplashScreen.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                finish();
            }
        });

        ((EditText)findViewById(R.id.email)).setText(
                SharedPrefManager.getInstance(this).getUserKey(
                        SharedPrefManager.EMAIL
                )
        );
        ((EditText)findViewById(R.id.name)).setText(
                SharedPrefManager.getInstance(this).getUserKey(
                        SharedPrefManager.KEY_NAME
                )
        );
        findViewById(R.id.go_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                String username = ((EditText)findViewById(R.id.email)).getText().toString().trim();
              //  String password = ((EditText)findViewById(R.id.password )).getText().toString().trim();
                String name = ((EditText)findViewById(R.id.name )).getText().toString().trim();


                if(name.equals("")){
                    Toast.makeText(ProfileActivity.this, "ادخل اسمك   !", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(username.equals("")){
                    Toast.makeText(ProfileActivity.this, "ادخل الايميل   !", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!SignupActivity.validate(username)){
                    Toast.makeText(ProfileActivity.this, "ادخل الايميل بصيغة  صحيحة   !", Toast.LENGTH_SHORT).show();
                    return;
                }
                //------
                v.setEnabled(false);
                findViewById(R.id.loading).setVisibility(View.VISIBLE);
                BackgroundServices.getInstance(ProfileActivity.this)
                        .setBaseUrl(APIUrl.SERVER)
                        .addPostParam("service" ,"edit_profile_1")
                        .addPostParam("id" ,SharedPrefManager.getInstance(getApplicationContext()).getUserID())
                        .addPostParam("name" ,name)
                        .addPostParam("email" ,username)
                        .CallPost( new PostAction() {
                            @Override
                            public void whenFinished(String s ,String response) throws JSONException {
                                v.setEnabled(true);
                                findViewById(R.id.loading).setVisibility(View.INVISIBLE);

                                JSONObject o = new JSONObject(response);
                                int status = o.getInt("status");
                                Toast.makeText(ProfileActivity.this, o.getString("message"), Toast.LENGTH_SHORT).show();


                            }
                        });
            }
        });
        findViewById(R.id.go_save_pass).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                String password = ((EditText)findViewById(R.id.password )).getText().toString().trim();

                if(password.equals("")){
                    Toast.makeText(ProfileActivity.this, "ادخل كلمة المرور !", Toast.LENGTH_SHORT).show();
                    return;

                }
                if(password.length()<8){
                    Toast.makeText(ProfileActivity.this, "كلمة المرور يجب ان تكون من 8 محارف على الاقل", Toast.LENGTH_SHORT).show();
                    return;

                }
                v.setEnabled(false);
                findViewById(R.id.loading).setVisibility(View.VISIBLE);
                BackgroundServices.getInstance(ProfileActivity.this)
                        .setBaseUrl(APIUrl.SERVER)
                        .addPostParam("service" ,"edit_profile_2")
                        .addPostParam("id" ,SharedPrefManager.getInstance(getApplicationContext()).getUserID())
                        .addPostParam("password" ,password)
                        .CallPost( new PostAction() {
                            @Override
                            public void whenFinished(String s ,String response) throws JSONException {
                                v.setEnabled(true);
                                findViewById(R.id.loading).setVisibility(View.INVISIBLE);

                                JSONObject o = new JSONObject(response);
                                int status = o.getInt("status");
                                Toast.makeText(ProfileActivity.this, o.getString("message"), Toast.LENGTH_SHORT).show();


                            }
                        });
            }
        });


    }
}
