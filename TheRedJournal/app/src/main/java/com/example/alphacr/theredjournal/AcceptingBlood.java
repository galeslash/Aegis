package com.example.alphacr.theredjournal;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import helper.SQLITEHandler;

public class AcceptingBlood extends AppCompatActivity {
    private static final String TAG = AcceptingBlood.class.getSimpleName();
    private ProgressDialog progressDialog;
    private SQLITEHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accepting_blood);
        TextView userRequest = (TextView) findViewById(R.id.titleUserRequest);
        TextView requestType = (TextView) findViewById(R.id.requestType);
        TextView requestAmount = (TextView) findViewById(R.id.requestAmount);
        TextView phoneNumber = (TextView) findViewById(R.id.phonenumber);
        CircleImageView requestPicture = (CircleImageView) findViewById(R.id.userProfileView);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        db = new SQLITEHandler(getApplicationContext());
        HashMap<String, String> profile = db.getUserDetails();
        final String donorId = profile.get("uid");
        final String bloodTypeDonor = profile.get("bloodType");

        Bundle bundle = getIntent().getExtras();
        final String reqId = bundle.getString("reqId");
        final String uid = bundle.getString("uid");
        final String user = bundle.getString("name");
        final String bloodType = bundle.getString("bloodType");
        final String amount = bundle.getString("amount");
        final String firebaseId = bundle.getString("firebaseId");
        final String image = bundle.getString("image");
        final String phone = bundle.getString("phoneNumber");

        if(image!=null) {
            Picasso.with(getApplicationContext())
                    .load(image)
                    .placeholder(R.drawable.photo)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .error(R.drawable.photo)
                    .noFade()
                    .into(requestPicture);
        } else{
            Picasso.with(getApplicationContext())
                    .load(R.drawable.photo)
                    .placeholder(R.drawable.photo)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .error(R.drawable.photo)
                    .noFade()
                    .into(requestPicture);
        }

        userRequest.setText(user + " wants to request blood");
        requestType.setText(bloodType);
        requestAmount.setText(amount);
        phoneNumber.setText(phone);

        Button acceptRequest = (Button) findViewById(R.id.acceptRequestButton);
        acceptRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bloodTypeDonor.equals("O-")) {
                    responseRequest(reqId, uid, donorId, user, bloodType, amount, firebaseId);
                } else if (bloodTypeDonor.equals("O+") ) {
                    if (bloodType == "AB+" || bloodType == "A+" || bloodType =="B+"
                            || bloodType =="O+") {
                        responseRequest(reqId, uid, donorId, user, bloodType, amount, firebaseId);
                    }
                } else if (bloodTypeDonor.equals("B-")) {
                    if (bloodType == "AB+" || bloodType =="AB-" || bloodType =="B+"
                            || bloodType =="B-") {
                        responseRequest(reqId, uid, donorId, user, bloodType, amount, firebaseId);
                    }
                } else if (bloodTypeDonor.equals("B+")) {
                    if (bloodType =="AB+" || bloodType =="B+") {
                        responseRequest(reqId, uid, donorId, user, bloodType, amount, firebaseId);
                    }
                } else if (bloodTypeDonor.equals("A-")) {
                    if (bloodType =="AB+" || bloodType =="AB-" || bloodType =="A+"
                            || bloodType =="A-") {
                        responseRequest(reqId, uid, donorId, user, bloodType, amount, firebaseId);
                    }
                } else if (bloodTypeDonor.equals("A+")) {
                    if (bloodType.equals("AB+") || bloodType.equals("A+")) {
                        responseRequest(reqId, uid, donorId, user, bloodType, amount, firebaseId);
                    }
                } else if (bloodTypeDonor.equals("AB-")) {
                    if (bloodType =="AB+" || bloodType =="AB-") {
                        responseRequest(reqId, uid, donorId, user, bloodType, amount, firebaseId);
                    }
                } else if (bloodTypeDonor.equals("AB+")) {
                    if (bloodType == "AB+") {
                        responseRequest(reqId, uid, donorId, user, bloodType, amount, firebaseId);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Your blood is not compatible!",
                            Toast.LENGTH_LONG).show();
                }
            }

        });
    }

    private void responseRequest(final String reqId, final String uid, final String donorId, final String user, final String bloodType,
                                 final String amount, final String firebaseId) {
        String tag_string_req = "accept_blood";
        progressDialog.setMessage("Loading...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_RESPONSE_REQUEST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Request Response" + response);
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {
                        Toast.makeText(getApplicationContext(), "Donor success!",
                                Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(AcceptingBlood.this, MapsActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
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
                Log.e(TAG, "Request Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();
                params.put("reqId", reqId);
                params.put("uid", uid);
                params.put("donorId", donorId);
                params.put("fullName", user);
                params.put("bloodType", bloodType);
                params.put("amount", String.valueOf(amount));
                params.put("firebaseId", firebaseId);
                params.put("status", "Accepted");

                return params;
            }
        };
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
    public void onBackPressed() {
        startActivity(new Intent(AcceptingBlood.this, MapsActivity.class));
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                Intent intent = new Intent(this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}


