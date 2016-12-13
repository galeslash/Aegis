package com.example.alphacr.theredjournal;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import firebase.FirebaseId;
import helper.SQLITEHandler;

public class request_blood extends AppCompatActivity {
    private static final String TAG = request_blood.class.getSimpleName();
    Spinner spinner, spinner2;
    ArrayAdapter<CharSequence> adapter, adapter2;
    private TextView title,blood, amount, info;
    private Button submit;
    private EditText editfield;
    private ProgressDialog progressDialog;
    private SQLITEHandler db;
    private String uid;
    private FirebaseId firebaseId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_blood);
        spinner = (Spinner) findViewById(R.id.bloodTypeSpinner);
        adapter = ArrayAdapter.createFromResource(this,R.array.blood_types,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        db = new SQLITEHandler(getApplicationContext());
        HashMap<String, String> user = db.getUserDetails();
        uid = user.get("uid");

        Typeface customFont = Typeface.createFromAsset(getAssets(), "fonts/Lato-Regular.ttf");
        Typeface customFont2 = Typeface.createFromAsset(getAssets(), "fonts/OSP-DIN.ttf");

        title = (TextView) findViewById(R.id.requestText);
        title.setTypeface(customFont2);

        blood = (TextView) findViewById(R.id.typeText);
        blood.setTypeface(customFont);

        amount = (TextView) findViewById(R.id.amountText);
        amount.setTypeface(customFont);

        editfield = (EditText) findViewById(R.id.amountField);
        editfield.setTypeface(customFont);

        info = (TextView) findViewById(R.id.amountInfo);
        info.setTypeface(customFont);

        SharedPreferences pref = getApplicationContext().getSharedPreferences(AppConfig.SHARED_PREF, 0);
        final String fId = pref.getString("regId", null);

        submit = (Button) findViewById(R.id.requestButton);
        submit.setTypeface(customFont);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = getIntent().getExtras();
                String bloodType = spinner.getSelectedItem().toString().trim();
                int amount = Integer.parseInt(editfield.getText().toString().trim());
                Double latitude = bundle.getDouble("latitude");
                Double longitude = bundle.getDouble("longitude");

                if(!bloodType.isEmpty() && amount != 0){
                    requestingBlood(uid, bloodType, amount, latitude, longitude, fId);
                } else{
                    Toast.makeText(getApplicationContext(), "All fields must not empty!"
                    , Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void requestingBlood(final String uid, final String bloodType, final int amount,
                                 final Double latitude, final Double longitude, final String firebaseId) {
        String tag_string_req = "req_blood";
        progressDialog.setMessage("Submitting...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_REQUEST_BLOOD, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Request Response" + response);
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {
                        Toast.makeText(getApplicationContext(), "request success!",
                                Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(request_blood.this, MapsActivity.class);
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
                params.put("uid", uid);
                params.put("bloodType", bloodType);
                params.put("amount", String.valueOf(amount));
                params.put("latitude", String.valueOf(latitude));
                params.put("longitude", String.valueOf(longitude));
                params.put("firebaseId", firebaseId);
                params.put("status", "On Request");

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
        startActivity(new Intent(request_blood.this, MapsActivity.class));
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
