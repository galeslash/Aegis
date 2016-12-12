package com.example.alphacr.theredjournal;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.maps.model.LatLng;

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


/**
 * Created by Revin on 07-Nov-16.
 */





public class ReyclerAdapter extends RecyclerView.Adapter<ReyclerViewHolder> {
    private static final String TAG = donation_history.class.getSimpleName();
    private final Context context;
    ArrayList<String> address;
    ArrayList<String> name;
    ArrayList<String> bloodType;
    ArrayList<String> dateOfRequest;
    ArrayList<String> donorId;
    ArrayList<String> reqId;
    private ProgressDialog progressDialog;

    // menampilkan list item dalam bentuk text dengan tipe data string dengan variable name

    LayoutInflater inflater;
    public ReyclerAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }
    public void Data(ArrayList<String> Name, ArrayList<String>  Address,
                     ArrayList<String>  BloodType, ArrayList<String> DateOfRequest,
                     ArrayList<String> DonorId, ArrayList<String> ReqId){
        name = Name;
        address = Address;
        bloodType = BloodType;
        dateOfRequest = DateOfRequest;
        donorId = DonorId;
        reqId = ReqId;
    }
    @Override
    public ReyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v=inflater.inflate(R.layout.item_list, parent, false);

        ReyclerViewHolder viewHolder=new ReyclerViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ReyclerViewHolder holder, int position) {
        holder.tv1.setText(name.get(position));
        holder.tv1.setOnClickListener(clickListener);
        holder.tv2.setText(address.get(position));
        holder.tv2.setOnClickListener(clickListener);
        holder.tvDate.setText(dateOfRequest.get(position));
        holder.imageView.setOnClickListener(clickListener);
        holder.tv1.setTag(holder);
        holder.imageView.setTag(holder);
        holder.tv2.setTag(holder);

    }

    View.OnClickListener clickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //member aksi saat cardview diklik berdasarkan posisi tertentu
            ReyclerViewHolder vholder = (ReyclerViewHolder) v.getTag();

            int position = vholder.getLayoutPosition();
            progressDialog = new ProgressDialog(context);
            progressDialog.setCancelable(false);
            if (!donorId.get(position).isEmpty()){
                getDonor(reqId.get(position),donorId.get(position));
            }

        }
    };

    private void getDonor(final String req_id, final String donor_id){
        String tag_string_req = "req_get_donor";

        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.URL_GET_DONOR,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "Get Donor Response: " + response);
                        try{
                            JSONObject jObj = new JSONObject(response);
                            boolean error = jObj.getBoolean("error");
                            if (!error) {
                                JSONObject donor = jObj.getJSONObject("msg");
                                String fullName = donor.getString("fullName");
                                String phone = donor.getString("phoneNumber");
                                String mail = donor.getString("eMail");
                                String donorPic = donor.getString("image");

                                Intent resultIntent = new Intent(context, notification_detail.class);
                                resultIntent.putExtra("fullName", fullName);
                                resultIntent.putExtra("phoneNumber", phone);
                                resultIntent.putExtra("eMail", mail);
                                resultIntent.putExtra("image", donorPic);
                                context.startActivity(resultIntent);
                                hideDialog();
                            } else {
                                String errorMsg = jObj.getString("error_msg");
                                //Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show();
                                Toast.makeText(context, "There are no donators for this request.", Toast.LENGTH_LONG).show();
                            }
                        }catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(context,"Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
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
                params.put("reqId", req_id);
                params.put("status", "Accepted");
                params.put("donorId", donor_id);

                return params;
            }
        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    @Override
    public int getItemCount() {
        return name.size();
    }

    private void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }

    private void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }
}
