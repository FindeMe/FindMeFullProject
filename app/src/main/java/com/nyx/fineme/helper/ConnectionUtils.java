package com.nyx.fineme.helper;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ConnectionUtils {

    public static  boolean isNetworkAvailable(Context a) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) a.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }














    public static String[] sendPostRequestWithFile(String requestURL,
                                                 HashMap<String, String> params ,  String token , File f
            ,String fname) {

        Log.d("okhttp_url"  , requestURL);

        String response = "",header_status="-1";;
        Request request = null;
        OkHttpClient client = new OkHttpClient();

        {
            MultipartBody.Builder mb =  new MultipartBody.Builder()
                    .setType(MultipartBody.FORM);
            for (Map.Entry<String, String> entry : params.entrySet()) {
                mb.addFormDataPart(entry.getKey(), entry.getValue());
            }
            mb.addFormDataPart("time", System.currentTimeMillis()+"");
            if(f!=null)
                mb .addFormDataPart(fname, f.getName(),
                        RequestBody.create(MediaType.parse("*/*"), f));
            request = new Request.Builder()
                    .url(requestURL)
                    .post(mb.build())
                    .addHeader("Content-Type", "application/json")
                    .addHeader("X-Requested-With", "XMLHttpRequest")
                    .addHeader("Authorization","Bearer "+ token)
                    .build();}

        Response responses = null;

        try {
            responses = client.newCall(request).execute();

            response = responses.body().string();
            header_status =  responses.code()+"";
            Log.d("OKHTTP3 : "  ,response);

        }catch (IOException e){
            //  Log.d("RET2 ERR "  , e.getMessage());
        }
        return new String[]{header_status ,response};
    }



    public static String[] sendPostRequest(String requestURL,
                                  HashMap<String, String> params , String type  ,String token) {

        Log.d("okhttp_url->"+type  , requestURL);

        String response = "" ,header_status="-1";

        OkHttpClient client = new OkHttpClient();
        Request.Builder rb =
       new Request.Builder()
                .url(requestURL)
                .addHeader("Content-Type", "application/json")
                .addHeader("X-Requested-With", "XMLHttpRequest");
              //  .addHeader("X-localization", lang)
                //.build();
        if(type.equals("post")){
            FormBody.Builder mb = new FormBody.Builder();
            for (Map.Entry<String, String> entry : params.entrySet()) {
                mb.add(entry.getKey(), entry.getValue());
            }
            rb.addHeader("Authorization","Bearer "+ token);
            rb.post(mb.build());
        }
        else {
            rb.get();
        }
        Response responses = null;
        Request request = rb.build();
        try {
            responses = client.newCall(request).execute();
            response = responses.body().string();
           header_status =  responses.code()+"";

            Log.d("OKHTTP3 : "  ,header_status + " : // : "+response);

        }catch (IOException e){    Log.d("RET2 ERR "  , e.getMessage());}
   return new String[]{header_status ,response};
    }


}
