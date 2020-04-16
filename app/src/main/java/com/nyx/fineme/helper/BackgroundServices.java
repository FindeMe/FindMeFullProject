package com.nyx.fineme.helper;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Toast;



public class BackgroundServices {

    private static BackgroundServices mInstance;
    private static Activity mCtx;
   private HashMap keys;
   private String file_name;
   private File f;
   static String url;
    private BackgroundServices(Activity context) {
        mCtx = context;
    }
    public static synchronized BackgroundServices getInstance(Activity context) {
        if (mInstance == null) {
            mInstance = new BackgroundServices(context);
        }
        mInstance.keys = new HashMap();
        url=APIUrl.SERVER;
        return mInstance;
    }
    public BackgroundServices setBaseUrl(String u){
        url=u;
        return mInstance;
    }
    public BackgroundServices setFile(String fname ,File f){
        this.f=f;
        this.file_name=fname;
        return mInstance;
    }
    public BackgroundServices addPostParam(String key ,String value){
        keys.put(key ,value);
        return mInstance;
    }
    public void CallGet(final PostAction pa ) {
        CallInternal( url ,  null , pa,0 ,"get");
    }
    public void CallPost(final PostAction pa ) {
        Log.d("OKHHTP_PARAMS" ,this.keys.toString());
        CallInternal( url ,  this.keys , pa,0 ,"post");
    }
    private String status="-1";
    private void CallInternal(final String url , final HashMap<String, String> args , final PostAction pa, final int try_
     ,final String method) {


        class CostumTask extends AsyncTask<String, Void, String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if(s.equals("")) {
                    if(try_<2){
                      CallInternal(url ,args ,pa ,try_+1 ,method);
                    }else{
                    Toast.makeText(mCtx, "Netwrok error", Toast.LENGTH_SHORT).show();
                    try {
                        pa.whenFinished(status ,s);
                    } catch (JSONException e) {
                   Log.d("JSONPARSE" ,e.getMessage());
                    }}

                }else {
                    try {
                        pa.whenFinished(status ,s);
                    } catch (JSONException e) {
                        Log.d("JSONPARSE" ,e.getMessage());
                    }
                }
            }
            @Override
            protected String doInBackground(String... params) {
                String[] result=null;
                try {
                    if(f==null)
                    result =
                            ConnectionUtils.sendPostRequest(url,
                                    args, method ,
                                   "");
                    else
                        result =   ConnectionUtils.sendPostRequestWithFile(url,
                                keys,"" ,f ,file_name
                                );

                  status = result[0] ;
                  return result[1];

                } catch (Exception e) {
                    Log.d("ERROR IN CALLBACK "+url+" : "  , e.getMessage());
                }

                return "";
            }
        }
        CostumTask ru = new CostumTask();
        ru.execute();
    }

}
