package com.nyx.fineme;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.nyx.fineme.helper.APIUrl;
import com.nyx.fineme.helper.BackgroundServices;
import com.nyx.fineme.helper.PostAction;
import com.nyx.fineme.helper.SharedPrefManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignupActivity extends Activity {
    private final static int PLACE_PICKER_REQUEST = 999;
    String lat="" ,lng="";

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static boolean validate(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(emailStr);
        return matcher.find();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("RESULTED " ,requestCode+" and "+resultCode );

        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {



                case PLACE_PICKER_REQUEST:
//                    Place place = PlacePicker.getPlace(getActivity(), data);
//                    // String placeName = String.format("Place: %s", place.getName());
//                    double latitude = place.getLatLng().latitude;
//                    double longitude = place.getLatLng().longitude;
//                    ((EditText) getView().findViewById(R.id.contact_value)).setText(latitude + " " + longitude);
//

                    if(resultCode == Activity.RESULT_OK){
                        String result=data.getStringExtra("result");
                        lat = (data.getStringExtra("lat"));
                        lng =data.getStringExtra("lng");

                        ((Button)findViewById(R.id.go_loc)).setText("تم الاختيار بنجاح");



                    }
                    if (resultCode == Activity.RESULT_CANCELED) {
                        //Write your code if there's no result
                    }
                    break;


            }
        }



    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        findViewById(R.id.go_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                String username = ((EditText)findViewById(R.id.email)).getText().toString().trim();
                String password = ((EditText)findViewById(R.id.password )).getText().toString().trim();
                String name = ((EditText)findViewById(R.id.name )).getText().toString().trim();


                if(name.equals("")){
                    Toast.makeText(SignupActivity.this, "ادخل اسمك   !", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(username.equals("")){
                    Toast.makeText(SignupActivity.this, "ادخل الايميل   !", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!validate(username)){
                    Toast.makeText(SignupActivity.this, "ادخل الايميل بصيغة  صحيحة   !", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(password.equals("")){
                    Toast.makeText(SignupActivity.this, "ادخل كلمة المرور !", Toast.LENGTH_SHORT).show();
                    return;

                }
                if(password.length()<8){
                    Toast.makeText(SignupActivity.this, "كلمة المرور يجب ان تكون من 8 محارف على الاقل", Toast.LENGTH_SHORT).show();
                    return;

                }
                if(lat.equals("") || lng.equals("")){
                    Toast.makeText(SignupActivity.this, "اختر مكان سكنك !", Toast.LENGTH_SHORT).show();
                    return;

                }
                //------
                v.setEnabled(false);
                findViewById(R.id.loading).setVisibility(View.VISIBLE);
                BackgroundServices.getInstance(SignupActivity.this)
                        .setBaseUrl(APIUrl.SERVER)
                        .addPostParam("service" ,"signup")
                        .addPostParam("id" ,username)
                        .addPostParam("name" ,name)
                        .addPostParam("lat" ,lat)
                        .addPostParam("lng" ,lng)
                        .addPostParam("password" ,password)
                        .CallPost( new PostAction() {
                            @Override
                            public void whenFinished(String s ,String response) throws JSONException {
                                v.setEnabled(true);
                                findViewById(R.id.loading).setVisibility(View.INVISIBLE);

                                JSONObject o = new JSONObject(response);
                                int status = o.getInt("status");
                                Toast.makeText(SignupActivity.this, o.getString("message"), Toast.LENGTH_SHORT).show();
                                if(status!=-1){
                                    SharedPrefManager.getInstance(SignupActivity.this).saveUser(o.getJSONObject("data"));
                                    Intent i=new Intent(SignupActivity.this
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
        findViewById(R.id.go_loc).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(SignupActivity.this ,MyLocationActivity.class) ,
                        PLACE_PICKER_REQUEST);
            }
        });
    }
}
