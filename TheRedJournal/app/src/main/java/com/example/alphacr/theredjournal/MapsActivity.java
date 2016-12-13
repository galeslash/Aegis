package com.example.alphacr.theredjournal;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.provider.SyncStateContract;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.content.ContextCompat;
import android.Manifest;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.example.alphacr.theredjournal.R.id.map;
import static com.example.alphacr.theredjournal.R.id.start;

public class MapsActivity extends AppCompatActivity implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        GoogleMap.OnMapClickListener,
        GoogleMap.InfoWindowAdapter
        {

    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;

    private  static final String TAG = MapsActivity.class.getSimpleName();
    LatLng latLng;
    GoogleMap mGoogleMap;
    SupportMapFragment mFragment;
    Marker currLocationMarker;
    Marker request_form = null;
    SwipeRefreshLayout refreshLayout;
    public Double latitude;
    public Double longitude;
    public HashMap<String, HashMap> markerInfo;

    // May need to delete this line
    // http://android-er.blogspot.co.id/2016/04/requesting-permissions-of.html
    static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        getLocation();
        markerInfo = new HashMap<>();
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(true);
                getLocation();
                refreshLayout.setRefreshing(false);
            }


        });
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mFragment.getMapAsync(this);
    }

    private void getLocation() {
        String tag_string_req = "req_get_location";
        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.URL_GET_LOCATION,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "Get Location Response: " + response);

                        try{
                            JSONObject jObj = new JSONObject(response);
                            boolean error = jObj.getBoolean("error");
                            if(!error){
                                JSONArray jsonArray = jObj.getJSONArray("msg");
                                for(int i=0;i<jsonArray.length();i++){
                                    JSONObject jLocation =jsonArray.getJSONObject(i);
                                    String reqId = jLocation.getString("reqId");
                                    String uid = jLocation.getString("uid");
                                    JSONObject user = jLocation.getJSONObject("user");
                                    String name = user.getString("name");
                                    Double lat = user.getDouble("latitude");
                                    Double lng = user.getDouble("longitude");
                                    String bloodType = user.getString("bloodType");
                                    String amount = user.getString("amount");
                                    String phoneNumber = user.getString("phoneNumber");
                                    String firebaseId = user.getString("firebaseId");
                                    String image = user.getString("image");

                                    HashMap<String, String> data = new HashMap<>();
                                    data.put("reqId", reqId);
                                    data.put("uid", uid);
                                    data.put("name", name);
                                    data.put("bloodType", bloodType);
                                    data.put("amount", amount);
                                    data.put("phoneNumber", phoneNumber);
                                    data.put("firebaseId", firebaseId);
                                    data.put("image", image);

                                    latLng = new LatLng(lat, lng);
                                    MarkerOptions markerOptions = new MarkerOptions();
                                    markerOptions.position(latLng);
                                    markerOptions.title("donor");
                                    markerOptions.snippet("Name = " + name + "\n" +
                                            "Blood Type = " + bloodType + "\n" + "Amount = " + amount);
                                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
                                    Marker marker = mGoogleMap.addMarker(markerOptions);
                                    markerInfo.put(marker.getId(), data);
                                }
                            } else{
                                String errorMsg = jObj.getString("error_msg");
                                Toast.makeText(getApplicationContext(),
                                        errorMsg, Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Location Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onMapReady(GoogleMap gMap) {
        mGoogleMap = gMap;
        mGoogleMap.setMinZoomPreference(13.0f);
        mGoogleMap.setMaxZoomPreference(20.0f);
        mGoogleMap.setOnMapClickListener(this);
        mGoogleMap.setInfoWindowAdapter(this);
        mGoogleMap.setOnInfoWindowClickListener(MyOnInfoWindowClickListener);
        // Check if GPS Permission is enabled
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mGoogleMap.setMyLocationEnabled(true);
        } else {
            Log.e(TAG, "Location Unavailable");
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            mGoogleMap.setMyLocationEnabled(true);
            Toast.makeText(getApplicationContext(), "Location Unavailable",
                    Toast.LENGTH_LONG).show();
        }

        buildGoogleApiClient();
        mGoogleApiClient.connect();
    }

    protected synchronized void buildGoogleApiClient() {
        Log.d(TAG,"buildGoogleApiClient");
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d(TAG,"onConnected!");
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(5000); //5 seconds
        mLocationRequest.setFastestInterval(3000); //3 seconds
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        //mLocationRequest.setSmallestDisplacement(0.1F); //1/10 meter

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);


    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG,"onConnectionSuspended");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG,"onConnectionFailed");
    }

    @Override
    public void onLocationChanged(Location location) {
        // While keep track of user's current location and move in the map into current position
        latLng = new LatLng(location.getLatitude(), location.getLongitude());
        Log.d(TAG, "Location Changed");
        //zoom to current position:
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng).zoom(14).build();

        mGoogleMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));

        //If you only need one location, unregister the listener
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);

    }


    // Search Function
    public void onSearch(View view) {
        EditText location_tf = (EditText) findViewById(R.id.TFaddress);
        String location = location_tf.getText().toString();
        List<Address> addressList = null;

        if (location.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please fill the search bar",
                    Toast.LENGTH_LONG).show();
        } else if (location != null || !location.equals("")) {
            Geocoder geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(location, 1);
                Address address = addressList.get(0);
                LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                mGoogleMap.addMarker(new MarkerOptions().position(latLng).title(location.toUpperCase()));
                mGoogleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Location you search cannot be found.", Toast.LENGTH_SHORT).show();
            }


        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(MapsActivity.this, HomeActivity.class));
        finish();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                Intent intent = new Intent(this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapClick(LatLng latLng) {
        if (request_form != null) {
            request_form.remove();
        }
        request_form = mGoogleMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title("Hello Ricky")
                .snippet("Soup ma bitch")
                .draggable(true));
        request_form.showInfoWindow();
        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
        //return prepareInfoView(marker);
    }

    // Custom InfoMaker
    @Override
    public View getInfoContents(Marker marker) {
        //return null;
        return prepareInfoView(marker);

    }

    private View prepareInfoView(Marker marker) {
        //prepare InfoView programmatically
        LinearLayout infoView = new LinearLayout(MapsActivity.this);
        LinearLayout.LayoutParams infoViewParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        infoView.setOrientation(LinearLayout.HORIZONTAL);
        infoView.setLayoutParams(infoViewParams);

        ImageView infoImageView = new ImageView(MapsActivity.this);
        Drawable drawable = getResources().getDrawable(R.mipmap.ic_launcher);

        //Drawable drawable = getResources().getDrawable(R.drawable.tearred);
        infoImageView.setImageDrawable(drawable);
        infoView.addView(infoImageView);

        LinearLayout subInfoView = new LinearLayout(MapsActivity.this);
        LinearLayout.LayoutParams subInfoViewParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        subInfoView.setOrientation(LinearLayout.VERTICAL);
        subInfoView.setLayoutParams(subInfoViewParams);


        String title = marker.getTitle();

        TextView donateInfo = new TextView(MapsActivity.this);
        TextView streetName = new TextView(MapsActivity.this);
        TextView donorInfo = new TextView(MapsActivity.this);

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(marker.getPosition().latitude, marker.getPosition().longitude, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder();
                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append(" ");
                }
                streetName.setText(strReturnedAddress.toString());
            } else {
                streetName.setText("No Address returned!");
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            streetName.setText("Cannot get Address!");
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            Log.d(TAG, "IndexOutOfBound");
        }



        if(title.equals("donor")){
            donateInfo.setText("DONOR REQUEST");
            subInfoView.addView(donateInfo);
            donorInfo.setText(marker.getSnippet());
            subInfoView.addView(donorInfo);
            infoView.addView(subInfoView);
        } else {
            donateInfo.setText("CREATE A BLOOD REQUEST");
            subInfoView.addView(donateInfo);
            subInfoView.addView(streetName);
            infoView.addView(subInfoView);
        }
        return infoView;
    }


    // May need to delete this.
    // http://android-er.blogspot.co.id/2016/04/requesting-permissions-of.html
    private void getMyLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.


            //------------------------------------------------------------------------------
            ActivityCompat.requestPermissions(MapsActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

            return;
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(MapsActivity.this,
                            "permission was granted, :)",
                            Toast.LENGTH_LONG).show();
                    getMyLocation();

                } else {
                    Toast.makeText(MapsActivity.this,
                            "permission denied, ...:(",
                            Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }



    // "Button" for Request Blood
    GoogleMap.OnInfoWindowClickListener MyOnInfoWindowClickListener
            = new GoogleMap.OnInfoWindowClickListener() {
        @Override
        public void onInfoWindowClick(Marker marker) {
            // Isaac change this shit
            String title = marker.getTitle();
            if (title.equals("donor")){
                HashMap<String, String> markerData = markerInfo.get(marker.getId());
                String reqId = markerData.get("reqId");
                String uid = markerData.get("uid");
                String name = markerData.get("name");
                String bloodType = markerData.get("bloodType");
                String amount = markerData.get("amount");
                String phoneNumber = markerData.get("phoneNumber");
                String firebaseId = markerData.get("firebaseId");
                String image = markerData.get("image");
                Intent intent = new Intent (MapsActivity.this, AcceptingBlood.class); // fake class
                intent.putExtra("reqId", reqId);
                intent.putExtra("uid", uid);
                intent.putExtra("name", name);
                intent.putExtra("bloodType", bloodType);
                intent.putExtra("amount", amount);
                intent.putExtra("phoneNumber", phoneNumber);
                intent.putExtra("firebaseId", firebaseId);
                intent.putExtra("image", image);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            } else {
                latitude = marker.getPosition().latitude;
                longitude = marker.getPosition().longitude;
                Intent intent = new Intent(MapsActivity.this, request_blood.class);
                intent.putExtra("latitude", latitude);
                intent.putExtra("longitude", longitude);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }


        }
    };


}
