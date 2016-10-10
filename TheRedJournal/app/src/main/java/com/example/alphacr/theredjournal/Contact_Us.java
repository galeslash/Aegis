package com.example.alphacr.theredjournal;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.RemoteController;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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


public class Contact_Us extends AppCompatActivity {
    private static final String TAG = Register.class.getSimpleName();
    EditText contactUs;
    private Button contact;
    ProgressDialog progressDialog;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact__us);
        contactUs= (EditText)findViewById(R.id.contactUs);
        contact = (Button) findViewById(R.id.contact);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        contact.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                String contact = contactUs.getText().toString().trim();

                if(!contact.isEmpty()){
                    storeContact(contact);
                }
                else{
                    Toast.makeText(getApplicationContext(), "Please fill your comment!", 
                            Toast.LENGTH_LONG).show();
                }
            }
        } );



    }
    private void storeContact(final String contact){
        String tag_string_req = "req_contact";
        
        progressDialog.setMessage("Submitting..");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_CONTACT_US, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Contact Response:" + response.toString());
                hideDialog();
                try{
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error){
                        String msg = jObj.getString("msg");
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(Contact_Us.this, HomePage.class);
                        startActivity(intent);
                        finish();
                    } else {
                        String errormsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(), errormsg, Toast.LENGTH_LONG).show();
                    }
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Submit Error:" + error.getMessage());
                Toast.makeText(getApplicationContext(), error.getMessage(),
                        Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }){
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<String, String>();
                params.put("contact", contact);
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void hideDialog() {
        if (!progressDialog.isShowing()){
            progressDialog.show();
        }
    }

    private void showDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }

}
