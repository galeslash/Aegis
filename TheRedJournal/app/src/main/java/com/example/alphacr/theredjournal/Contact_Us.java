package com.example.alphacr.theredjournal;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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


public class Contact_Us extends Fragment {
    private static final String TAG = Register.class.getSimpleName();
    EditText contactUs;
    private String uid;
    private Button contact;
    private SQLITEHandler db;
    ProgressDialog progressDialog;
    final FragmentActivity    faActivity  = (FragmentActivity)    super.getActivity();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final FragmentActivity    faActivity  = (FragmentActivity)    super.getActivity();
        // Replace LinearLayout by the type of the root element of the layout you're trying to load
        LinearLayout        llLayout    = (LinearLayout)    inflater.inflate(R.layout.activity_contact__us, container, false);
        // Of course you will want to faActivity and llLayout in the class and not this method to access them in the rest of
        // the class, just initialize them here

        // Content of previous onCreate() here
        // ...
        contactUs= (EditText)llLayout.findViewById(R.id.contactUs);
        contact = (Button) llLayout.findViewById(R.id.contact);

        db = new SQLITEHandler(faActivity.getApplicationContext());

        progressDialog = new ProgressDialog(faActivity);
        progressDialog.setCancelable(false);

        HashMap<String, String> user = db.getUserDetails();
        uid = user.get("uid");

        contact.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                String contact = contactUs.getText().toString().trim();

                InputMethodManager inputManager = (InputMethodManager) faActivity.getSystemService(faActivity.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(faActivity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);


                if(!contact.isEmpty()){
                    storeContact(contact, uid);
                }
                else{
                    Toast.makeText(faActivity.getApplicationContext(), "Please fill your comment!",
                            Toast.LENGTH_LONG).show();
                }
            }
        } );
        // Don't use this method, it's handled by inflater.inflate() above :
        // setContentView(R.layout.activity_layout);

        // The FragmentActivity doesn't contain the layout directly so we must use our instance of     LinearLayout :

        // Instead of :
        // findViewById(R.id.someGuiElement);
        return llLayout; // We must return the loaded Layout

    }
    private void storeContact(final String contact,final String uid){
        String tag_string_req = "req_contact";

        progressDialog.setMessage("Submitting..");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_CONTACT_US, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Contact Response:" + response);
                hideDialog();
                try{
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error){
                        String msg = jObj.getString("msg");
                        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
                        FragmentManager fragmentManager = getFragmentManager();
                        fragmentManager.beginTransaction()
                                .replace(R.id.content_home, new HomePage()).commit();
                    } else {
                        String errormsg = jObj.getString("error_msg");
                        Toast.makeText(getActivity(), errormsg, Toast.LENGTH_LONG).show();
                    }
                }
                catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Submit Error:" + error.getMessage());
                Toast.makeText(faActivity.getApplicationContext(), error.getMessage(),
                        Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }){
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("uid", uid);
                params.put("contact", contact);
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog() {
        if (!progressDialog.isShowing()){
            progressDialog.show();
        }
    }

    private void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }


//    @Override
//    public boolean onOptionsItemSelected(MenuItem item){
//        switch (item.getItemId()) {
//            // Respond to the action bar's Up/Home button
//            case android.R.id.home:
//                Intent intent = new Intent(this, HomeActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
//                finish();
//                return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }


}
