package com.nyx.fineme;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nyx.fineme.adapters.DevicesAdapter;
import com.nyx.fineme.helper.APIUrl;
import com.nyx.fineme.helper.BackgroundServices;
import com.nyx.fineme.helper.PostAction;
import com.nyx.fineme.helper.SharedPrefManager;
import com.nyx.fineme.models.DeviceRow;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    void get_location(){
        BackgroundServices.getInstance(this)
                .setBaseUrl(APIUrl.SERVER)
                .addPostParam("service" ,"get_coors")
                .addPostParam("id" , getIntent().getStringExtra("id"))
                .addPostParam("token" , SharedPrefManager.getInstance(this).getUserKey(SharedPrefManager.KEY_TOKEN))
                .CallPost(new PostAction() {
                    @Override
                    public void whenFinished(String status, String response) throws JSONException {
                        findViewById(R.id.loading).setVisibility(View.GONE);
                        if(new JSONObject(response).getInt("status")==1) {
                            JSONObject obj = new JSONObject(response).getJSONObject("data");

                            LatLng sydney = new LatLng(obj.getDouble("lat"), obj.getDouble("lng"));
                            int height = 250;
                            int width = 250;
                            BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.bikes_marker);
                            Bitmap b=bitmapdraw.getBitmap();
                            Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
                            Marker marker =  mMap.addMarker(new MarkerOptions().position(sydney)
                                    .title(obj.getString("address"))  .icon(BitmapDescriptorFactory.fromBitmap(smallMarker)));
                            marker.setTag("s");

                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 17));
                            ((TextView)findViewById(R.id.name)).setText(obj.getString("address"));
                            ((TextView)findViewById(R.id.date)).setText( "آخر تحديث : " + obj.getString("last_updated"));

                        }else{
                            Toast.makeText(MapsActivity.this, new JSONObject(response).getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    }
                });



    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
get_location();
        // Add a marker in Sydney and move the camera

    }
}
