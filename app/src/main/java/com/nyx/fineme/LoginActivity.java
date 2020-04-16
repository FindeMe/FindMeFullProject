package com.nyx.fineme;

import android.app.Activity;
import android.content.Intent;
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

public class LoginActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findViewById(R.id.go_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                String username = ((EditText)findViewById(R.id.email)).getText().toString().trim();
                String password = ((EditText)findViewById(R.id.password )).getText().toString().trim();


                if(username.equals("")){
                    Toast.makeText(LoginActivity.this, "ادخل الايميل   !", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(password.equals("")){
                    Toast.makeText(LoginActivity.this, "ادخل كلمة المرور !", Toast.LENGTH_SHORT).show();
                    return;

                }
                //------
                v.setEnabled(false);
                findViewById(R.id.loading).setVisibility(View.VISIBLE);
                BackgroundServices.getInstance(LoginActivity.this)
                        .setBaseUrl(APIUrl.SERVER)
                        .addPostParam("service" ,"login")
                        .addPostParam("id" ,username)
                        .addPostParam("password" ,password)
                        .CallPost( new PostAction() {
                            @Override
                            public void whenFinished(String s ,String response) throws JSONException {
                                v.setEnabled(true);
                                findViewById(R.id.loading).setVisibility(View.INVISIBLE);

                                JSONObject o = new JSONObject(response);
                                int status = o.getInt("status");
                                Toast.makeText(LoginActivity.this, o.getString("message"), Toast.LENGTH_SHORT).show();
                                if(status!=-1){
                                    SharedPrefManager.getInstance(LoginActivity.this).saveUser(o.getJSONObject("data"));

                                   Intent i=new Intent(LoginActivity.this
                                           , MyDevices.class);
                                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(i);


                                }

                            }
                        });
            }
        });
        findViewById(R.id.go_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
