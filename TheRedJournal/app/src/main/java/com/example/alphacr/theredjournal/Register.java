package com.example.alphacr.theredjournal;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import helper.SQLITEHandler;
import helper.SessionManager;

public class Register extends AppCompatActivity {
    private static final String TAG = Register.class.getSimpleName();
    private Button register;
    private EditText fullName, eMail, password, age, phoneNumber, confirmPassword;
    private RadioGroup gender, bloodType, rhesus;
    private RadioButton selectGender, selectBlood, selectRhesus;
    private ProgressDialog progressDialog;
    private SessionManager session;
    private SQLITEHandler db;
    private TextView title, Gender, Bloodtype, Rhesus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Typeface customFont = Typeface.createFromAsset(getAssets(), "fonts/Lato-Regular.ttf");
        title = (TextView) findViewById(R.id.titleText);
        title.setTypeface(customFont);

        fullName = (EditText) findViewById(R.id.fullName);
        fullName.setTypeface(customFont);

        eMail = (EditText) findViewById(R.id.eMail);
        eMail.setTypeface(customFont);

        password = (EditText) findViewById(R.id.password);
        password.setTypeface(customFont);

        age = (EditText) findViewById(R.id.age);
        age.setTypeface(customFont);

        phoneNumber = (EditText) findViewById(R.id.phoneNumber);
        phoneNumber.setTypeface(customFont);

        Gender = (TextView) findViewById(R.id.text);
        Gender.setTypeface(customFont);
        gender = (RadioGroup) findViewById(R.id.gender);

        bloodType = (RadioGroup) findViewById(R.id.bloodType);
        Bloodtype = (TextView) findViewById(R.id.text3);
        Bloodtype.setTypeface(customFont);

        rhesus = (RadioGroup) findViewById(R.id.rhesus);
        Rhesus = (TextView) findViewById(R.id.rhesustext);
        Rhesus.setTypeface(customFont);

        register = (Button) findViewById(R.id.register);
        register.setTypeface(customFont);

        confirmPassword = (EditText) findViewById(R.id.rePassword);
        confirmPassword.setTypeface(customFont);

        int selectedIdGender = gender.getCheckedRadioButtonId();
        int selectedIdBlood = bloodType.getCheckedRadioButtonId();
        int selectedRhesus = rhesus.getCheckedRadioButtonId();
        selectBlood = (RadioButton) findViewById(selectedIdBlood);
        selectGender = (RadioButton) findViewById(selectedIdGender);
        selectRhesus = (RadioButton) findViewById(selectedRhesus);

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
                String name = fullName.getText().toString().trim();
                String email = eMail.getText().toString().trim();
                String psw = password.getText().toString().trim();
                String phonenumber = phoneNumber.getText().toString().trim();
                String Age = age.getText().toString().trim();
                String Gender = selectGender.getText().toString().trim();
                String blood = selectBlood.getText().toString().trim();
                String confirmPsw = confirmPassword.getText().toString().trim();
                String rhesus = selectRhesus.getText().toString().trim();

                if (rhesus.equals("+ (Positive)")){
                    rhesus = "+";
                }else {
                    rhesus = "-";
                }

                String Blood = blood + rhesus;

                if (!name.isEmpty() && !email.isEmpty() && !psw.isEmpty() &&
                        !phonenumber.isEmpty() && !Age.isEmpty() && !Gender.isEmpty() &&
                        !Blood.isEmpty()) {
                    if (psw.length()<6){
                        Toast.makeText(getApplicationContext(), "Password's length must be equal or"
                                + " greater than six!", Toast.LENGTH_LONG).show();
                    }
                    else if (!psw.equals(confirmPsw)){
                        Toast.makeText(getApplicationContext(), "First password and second password"
                                + " do not match!", Toast.LENGTH_LONG).show();
                    }
                    else {
                        registerUser(name, email, psw, phonenumber, Gender, Blood, Age);
                    }
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
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
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
                Map<String, String> params = new HashMap<>();
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

    @Override
    public void onBackPressed() {
        startActivity(new Intent(Register.this, LoginActivity.class));
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