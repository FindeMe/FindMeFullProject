package com.nyx.fineme;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.nyx.fineme.helper.SharedPrefManager;

public class SplashScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(SharedPrefManager.getInstance(this).isLoggedIn()){

            startActivity(new Intent(SplashScreen.this ,MyDevices.class));
            Toast.makeText(SplashScreen.this,"مرحبا من جديد " + SharedPrefManager.getInstance(this)
                    .getUserKey("q2"), Toast.LENGTH_SHORT).show();

            finish();
            return;

        }
        setContentView(R.layout.activity_splash_screen);
        findViewById(R.id.go_signup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SplashScreen.this ,SignupActivity.class));
            }
        });
        findViewById(R.id.go_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SplashScreen.this ,LoginActivity.class));
            }
        });
    }
}
