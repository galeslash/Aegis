package com.example.alphacr.theredjournal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Map;
import helper.SQLITEHandler;

public class donation_history extends AppCompatActivity {
    private static final String TAG = donation_history.class.getSimpleName();
    RecyclerView recyclerView;
    private SQLITEHandler db;


    //String [] name = {"Rumah Sakit Pusat Pertamina", "MENTAL", "pneis"};
    String [] name = {};
    //String [] address= {"Jakarta", "Depok", "penis"};
    String [] address= {};
    String [] bloodType;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation_history);

        db = new SQLITEHandler(getApplicationContext());
        HashMap<String, String> user = db.getUserDetails();
        final String unique_id = user.get("uid");

        getHistory(unique_id);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        //menampilkan reyclerview yang ada pada file layout dengan id reycler view

        ReyclerAdapter adapter = new ReyclerAdapter(this);
        adapter.Data(name, address, bloodType);
        //membuat adapter baru untuk reyclerview
        recyclerView.setAdapter(adapter);
        //menset nilai dari adapter
        recyclerView.setHasFixedSize(true);
        //menset setukuran
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //menset layoutmanager dan menampilkan daftar/list
        //dalam bentuk linearlayoutmanager pada class saat ini
    }

    private void getHistory(final String unique_id) {
       //buat string request, buat jsonnya dibagi jadi 3 variable array
        // misal : response = {uid : "abidu', user : {name : 'aoids', blood : 'a'}}
        // ditaro di variable uid = {} name = {} blood = {}
        String tag_string_req = "req_get_history";
        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.URL_GET_HISTORY,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "Get Location Response: " + response);
                        /**
                        try {
                            JSONObject jObj = new JSONObject(response);
                            boolean error = jObj.getBoolean("error");
                            if (!error) {
                                Log.d(TAG, "hello");
                                JSONArray jsonArray = jObj.getJSONArray("history");

                                for(int i=0;i<jsonArray.length();i++){
                                    JSONObject jHistory =jsonArray.getJSONObject(i);
                                    String name = jHistory.getString("longitude");
                                    Log.d(TAG, name);

                                }

                            } else {
                                String errorMsg = jObj.getString("error_msg");
                                Toast.makeText(getApplicationContext(),
                                        errorMsg, Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        } **/

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Update Error: " + error.getMessage());
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

