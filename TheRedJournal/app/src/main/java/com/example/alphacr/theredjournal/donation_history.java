package com.example.alphacr.theredjournal;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.identity.intents.Address;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import helper.SQLITEHandler;


public class donation_history extends AppCompatActivity {
    private static final String TAG = donation_history.class.getSimpleName();
    RecyclerView recyclerView;
    private SQLITEHandler db;
    private ArrayList<String> name, bloodType, address, dateOfRequest, donorId, reqId;
    private ProgressDialog progressDialog;
    private ReyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation_history);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        db = new SQLITEHandler(getApplicationContext());
        HashMap<String, String> user = db.getUserDetails();
        final String unique_id = user.get("uid");

        name = new ArrayList<String>();
        address = new ArrayList<String>();
        bloodType = new ArrayList<String>();
        dateOfRequest = new ArrayList<String>();
        donorId = new ArrayList<String>();
        reqId = new ArrayList<String>();


        getHistory(unique_id);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        //menampilkan reyclerview yang ada pada file layout dengan id reycler view
        adapter = new ReyclerAdapter(this);

    }



    private void getHistory(final String unique_id) {
        //buat string request, buat jsonnya dibagi jadi 3 variable array
        // misal : response = {uid : "abidu', user : {name : 'aoids', blood : 'a'}}
        // ditaro di variable uid = {} name = {} blood = {}
        String tag_string_req = "req_get_history";

        progressDialog.setMessage("Loading ...");
        showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.URL_GET_HISTORY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "Get Location Response: " + response);
                        try {
                            JSONObject jObj = new JSONObject(response);
                            boolean error = jObj.getBoolean("error");
                            if (!error) {
                                Log.d(TAG, "hello");
                                JSONArray jsonArray = jObj.getJSONArray("history");

                                for(int i=0;i<jsonArray.length();i++){
                                    JSONObject jHistory =jsonArray.getJSONObject(i);

                                    String longitude = jHistory.getString("longitude");
                                    String latitude = jHistory.getString("latitude");
                                    String blood = jHistory.getString("required_type");
                                    String status = jHistory.getString("status");
                                    String date_of_request = jHistory.getString("date_of_request");
                                    String donor_id = jHistory.getString("donor_id");
                                    String req_id = jHistory.getString("req_id");

                                    Geocoder geocoder;
                                    List<android.location.Address> addresses;
                                    LatLng latLng = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
                                    geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                                    addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                                    String streetName = addresses.get(0).getAddressLine(0);

                                    address.add("Status: " + status);
                                    name.add(streetName);
                                    bloodType.add(blood);
                                    dateOfRequest.add(date_of_request);
                                    donorId.add(donor_id);
                                    reqId.add(req_id);


                                }

                            } else {
                                String errorMsg = jObj.getString("error_msg");
                                Toast.makeText(getApplicationContext(),
                                        errorMsg, Toast.LENGTH_LONG).show();
                            }

                            //membuat adapter baru untuk reyclerview
                            adapter.Data(name, address, bloodType, dateOfRequest, donorId, reqId);
                            recyclerView.setAdapter(adapter);
                            //menset nilai dari adapter
                            recyclerView.setHasFixedSize(true);
                            //menset setukuran
                            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            //menset layoutmanager dan menampilkan daftar/list
                            //dalam bentuk linearlayoutmanager pada class saat ini

                            hideDialog();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Geocoder error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Update Error: " + error.getMessage());
                        hideDialog();
                    }
                }) {
                @Override
                protected Map<String, String> getParams() {
                    // Posting params to register url
                    Map<String, String> params = new HashMap<>();
                    params.put("unique_id", unique_id);
                    return params;
                }
        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }


    private void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }

    private void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    @Override
    public void onBackPressed(){
        startActivity(new Intent(donation_history.this,HomeActivity.class));
        finish();

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
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

    }

