package com.nyx.fineme.helper;


import android.content.Context;
import android.content.SharedPreferences;
import org.json.JSONException;
import org.json.JSONObject;

public class SharedPrefManager {

    private static SharedPrefManager mInstance;
    private static Context mCtx;
    private static final String SHARED_PREF_NAME = "ww-rep2738219";
    public static final String KEY_ID = "q1";
    public static final String KEY_NAME= "q2";
    public static final String EMAIL= "q3";
    public static final String KEY_TOKEN= "q4";
    public static final String KEY_FB= "q5";

    private SharedPrefManager(Context context) {
        mCtx = context;
    }
    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    public boolean saveData(String data ,String key)  {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, data);
        editor.apply();
        return true;
    }
    public boolean saveUser(JSONObject j) throws JSONException {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_ID, j.getString("id"));
        editor.putString(EMAIL, j.getString("email"));
        editor.putString(KEY_NAME, j.getString("name"));
        editor.putString(KEY_TOKEN, j.getString("token"));


        editor.apply();
        return true;
    }
    public boolean isLoggedIn() {
        return !getUserID().equals("");
    }


    public void logout(){
        saveData(""  ,KEY_ID);
    }
    public String getUserID() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String o = sharedPreferences.getString(KEY_ID, null);
        return (o != null && !o.trim().equals(""))?o:"";
    }
    public String getUserKey(String key) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String o = sharedPreferences.getString(key, null);
        return (o != null && !o.trim().equals(""))?o:"";
    }


}