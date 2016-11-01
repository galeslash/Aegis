package com.example.alphacr.theredjournal;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class ChangePassword extends AppCompatActivity {
    private static final String TAG = ChangePassword.class.getSimpleName();
    private EditText currentPsw, newPsw, confirmPsw;
    private String uid;
    private ProgressDialog progressDialog;
    private SessionManager sessionManager;
    private SQLITEHandler db;
    private HashMap<String, String> user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        currentPsw = (EditText) findViewById(R.id.currentPsw);
        newPsw = (EditText) findViewById(R.id.newPsw);
        confirmPsw = (EditText) findViewById(R.id.confirmNewPsw);
        Button btnConfirmPsw = (Button) findViewById(R.id.btnConfirmPsw);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        db = new SQLITEHandler(getApplicationContext());

        sessionManager = new SessionManager(getApplicationContext());

        user = db.getUserDetails();

        uid = user.get("uid");

        btnConfirmPsw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentpsw = currentPsw.getText().toString().trim();
                String psw = newPsw.getText().toString().trim();
                String confirmpsw = confirmPsw.getText().toString().trim();

                if(!currentpsw.isEmpty() && !psw.isEmpty() && !confirmpsw.isEmpty()){
                    if (psw.length()<6){
                        Toast.makeText(getApplicationContext(), "Password's length must be equal or"
                                + " greater than six!", Toast.LENGTH_LONG).show();
                    }
                    else if (!psw.equals(confirmpsw)){
                        Toast.makeText(getApplicationContext(), "First password and second password"
                                + " do not match!", Toast.LENGTH_LONG).show();
                    }
                    else{
                        changePassword(uid, currentpsw, psw);
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(), "Please fill the requirement fields!",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void changePassword(final String uid, final String currentpsw, final String newPsw) {
        // Tag used to cancel request
        String tag_string_req = "req_change_password";

        progressDialog.setMessage("Changing your password..");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_CHANGE_PASSWORD, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Change Password Response:" + response);
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {
                        //system successfully change the password
                        String msg = jObj.getString("msg");
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();

                        //redirect to homepage
                        Intent intent = new Intent(ChangePassword.this, HomePage.class);
                        startActivity(intent);
                        finish();
                    } else {
                        //get error message from server
                        String errormsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(), errormsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Submit Error" + error.getMessage());
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }){
            @Override
            protected Map<String, String> getParams(){
                //posting params
                Map<String, String> params = new HashMap<>();
                params.put("uid", uid);
                params.put("oldPassword", currentpsw);
                params.put("password", newPsw);
                return params;
            }
        };

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
    public void onBackPressed(){
        startActivity(new Intent(ChangePassword.this, HomePage.class));
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            // Respond to the action bar's up/home button
            case android.R.id.home:
                Intent intent  = new Intent(this, HomePage.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
