package com.example.alphacr.theredjournal;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingDeque;

import helper.SQLITEHandler;
import helper.SessionManager;

public class Register extends AppCompatActivity {
    private static final String TAG = Register.class.getSimpleName();
    private Button register;
    private EditText fullName, eMail, password, age, phoneNumber;
    private RadioGroup gender, bloodType;
    private RadioButton selectGender, selectBlood;
    private ProgressDialog progressDialog;
    private SessionManager session;
    private SQLITEHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        fullName = (EditText) findViewById(R.id.fullName);
        eMail = (EditText) findViewById(R.id.eMail);
        password = (EditText) findViewById(R.id.password);
        age = (EditText) findViewById(R.id.age);
        phoneNumber = (EditText) findViewById(R.id.phoneNumber);
        gender = (RadioGroup) findViewById(R.id.gender);
        bloodType = (RadioGroup) findViewById(R.id.bloodType);
        register = (Button) findViewById(R.id.register);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        session = new SessionManager(getApplicationContext());

        db = new SQLITEHandler(getApplicationContext());

        if(session.isLoggedIn()){
            Intent intent = new Intent(Register.this, HomePage.class);
            startActivity(intent);
            finish();
        }
        register.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view){
                int selectedIdGender = gender.getCheckedRadioButtonId();
                int selectedIdBlood = bloodType.getCheckedRadioButtonId();
                selectBlood = (RadioButton) findViewById(selectedIdBlood);
                selectGender = (RadioButton) findViewById(selectedIdGender);
                String name = fullName.getText().toString().trim();
                String email = eMail.getText().toString().trim();
                String psw = password.getText().toString().trim();
                String phonenumber = phoneNumber.getText().toString().trim();
                String Age = age.getText().toString().trim();
                String Gender = selectGender.getText().toString().trim();
                String Blood = selectBlood.getText().toString().trim();

                if (!name.isEmpty() && !email.isEmpty() && !psw.isEmpty() &&
                        !phonenumber.isEmpty() && !Age.isEmpty() && !Gender.isEmpty() &&
                        !Blood.isEmpty()) {
                    registerUser(name, email, psw, phonenumber, Gender, Blood, Age);
                }
                else{
                    Toast.makeText(getApplicationContext(), "Please fill in your details!",
                    Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void registerUser(final String name, final String email,
                              final String password, final String phonenumber, final String Gender, final String Blood, final String Age) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        progressDialog.setMessage("Registering ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_REGISTER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response);
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite
                        String uid = jObj.getString("uid");

                        JSONObject user = jObj.getJSONObject("user");
                        String name = user.getString("fullName");
                        String email = user.getString("eMail");
                        int age = user.getInt("age");
                        int phonenumber = user.getInt("phoneNumber");
                        String bloodType = user.getString("bloodType");
                        String Gender = user.getString("gender");


                        // Inserting row in users table
                        db.addUser(name, email, uid, age, bloodType, phonenumber, Gender );

                        Toast.makeText(getApplicationContext(), "User successfully registered. Try login now!", Toast.LENGTH_LONG).show();

                        // Launch login activity
                        Intent intent = new Intent(
                                Register.this,
                                LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("fullName", name);
                params.put("eMail", email);
                params.put("password", password);
                params.put("age", Age);
                params.put("phoneNumber", phonenumber);
                params.put("gender", Gender);
                params.put("bloodType", Blood);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    private void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }
}