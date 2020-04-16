package com.nyx.fineme;

import android.app.Application;
import android.content.Context;

import com.google.firebase.FirebaseApp;


//
//@AcraToast(resText=R.string.cora_txt,
//        length = Toast.LENGTH_LONG)
//@AcraCore(reportFormat= StringFormat.KEY_VALUE_LIST)
//@AcraHttpSender(uri = "https://e3lani.co/dev/u/crashes/save_log.php",
//        httpMethod = HttpSender.Method.POST)


public class App extends Application {
    private static Context context;

    public static Context getAppContext() {
        return App.context;
    }





    @Override
    public void onCreate() {
        super.onCreate();
        App.context = getApplicationContext();
        FirebaseApp.initializeApp(this);
    }

}
