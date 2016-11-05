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

import helper.SQLITEHandler;
import helper.SessionManager;

public class ForgotPassword extends AppCompatActivity {
    private static final String TAG = ForgotPassword.class.getSimpleName();
    private TextView PswText, instruction;
    private EditText emailField;
    private Button Send;
    private ProgressDialog progressDialog;
    private SessionManager sessionManager;
    private SQLITEHandler db;
    private HashMap<String, String> user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        Typeface customFont = Typeface.createFromAsset(getAssets(), "fonts/Lato-Regular.ttf");

        PswText = (TextView) findViewById(R.id.lostPswText);
        PswText.setTypeface(customFont);

        instruction = (TextView) findViewById(R.id.instructionText);
        instruction.setTypeface(customFont);

        emailField = (EditText) findViewById(R.id.emailField);
        emailField.setTypeface(customFont);

        Send = (Button) findViewById(R.id.btnSend);
        Send.setTypeface(customFont);

        Button btnSend = (Button) findViewById(R.id.btnSend);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        sessionManager = new SessionManager(getApplicationContext());

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailField.getText().toString().trim();

                //Check for empty  data in email field
                if(!email.isEmpty()){
                    checkEmail(email);
                }else{
                    Toast.makeText(getApplicationContext(),
                            "Please enter your email!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    private void checkEmail(final String email){
        String taq_string_req = "req_forgot_psw";

        progressDialog.setMessage("Sending your temporary email...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_FORGOT_PASSWORD, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "ForgotPassword Response: " + response);
                hideDialog();

                try{
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    //Check for error
                    if(!error){
                        //system successfully sent temp. password
                        String msg = jObj.getString("msg");
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();

                        // redirect to login page
                        Intent intent = new Intent(ForgotPassword.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else{
                        // get error message from server
                        String errormsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(), errormsg, Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Submit Error" + error.getMessage());
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                hideDialog();
            }
        }){
            @Override
            protected Map<String, String> getParams(){
                // posting params
                Map<String, String> params = new HashMap<>();
                params.put("eMail", email);
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(strReq, taq_string_req);
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
    public void onBackPressed(){
        startActivity(new Intent(ForgotPassword.this, LoginActivity.class));
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            // Respond to the action bar's up/home button
            case android.R.id.home:
                Intent intent  = new Intent(this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
