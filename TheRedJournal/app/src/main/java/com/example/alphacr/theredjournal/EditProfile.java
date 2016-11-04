package com.example.alphacr.theredjournal;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import helper.SQLITEHandler;
import helper.SessionManager;

public class EditProfile extends AppCompatActivity {
    private static final String TAG = EditProfile.class.getSimpleName();
    private RadioButton selectBlood, selectRhesus;
    private RadioGroup editBloodType, editRhesus;
    private ProgressDialog progressDialog;
    private SessionManager sessionManager;
    private SQLITEHandler db;
    private DatePickerDialog datePickerDialog;
    private int PICK_IMAGE_REQUEST = 1;
    private CircleImageView imageView;
    private static final int CAPTURE_IMAGE_REQUEST = 2;
    public static final int MEDIA_TYPE_IMAGE = 1;
    private Bitmap bitmap;
    private String imageUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        final EditText editName = (EditText) findViewById(R.id.editName);
        final EditText editEmail = (EditText) findViewById(R.id.editEmail);
        final EditText editPhone = (EditText) findViewById(R.id.editPhone);
        final EditText editDateOfBirth = (EditText) findViewById(R.id.editDateOfBirth);
        editBloodType = (RadioGroup) findViewById(R.id.editBloodType);
        editRhesus = (RadioGroup) findViewById(R.id.editRhesus);
        imageView = (CircleImageView) findViewById(R.id.changeImage);

        Button ConfirmChange = (Button) findViewById(R.id.confirmChange);

        db = new SQLITEHandler(getApplicationContext());
        sessionManager = new SessionManager(getApplicationContext());

        HashMap<String, String> user = db.getUserDetails();
        final String uid = user.get("uid");
        final String name = user.get("fullName");
        final String eMail = user.get("eMail");
        final String dateOfBirth = user.get("dateOfBirth");
        final String phone = user.get("phoneNumber");
        final String bloodType = user.get("bloodType");
        imageUrl = user.get("image");

        if (bloodType.equals("A+")){
            editBloodType.check(R.id.A);
            editRhesus.check(R.id.plus);
        } else if (bloodType.equals("A-")) {
            editBloodType.check(R.id.A);
            editRhesus.check(R.id.minus);
        } else if (bloodType.equals("B+")) {
            editBloodType.check(R.id.B);
            editRhesus.check(R.id.plus);
        } else if (bloodType.equals("B-")) {
            editBloodType.check(R.id.B);
            editRhesus.check(R.id.minus);
        } else if (bloodType.equals("AB+")) {
            editBloodType.check(R.id.AB);
            editRhesus.check(R.id.plus);
        } else if (bloodType.equals("AB-")) {
            editBloodType.check(R.id.AB);
            editRhesus.check(R.id.minus);
        } else if (bloodType.equals("O+")) {
            editBloodType.check(R.id.O);
            editRhesus.check(R.id.plus);
        } else {
            editBloodType.check(R.id.O);
            editRhesus.check(R.id.minus);
        }

        editName.setText(name);
        editEmail.setText(eMail);
        editDateOfBirth.setText(dateOfBirth);
        editPhone.setText(phone);

        Picasso.with(this)
                    .load(imageUrl)
                    .placeholder(R.drawable.photo)
                    .error(R.drawable.photo)
                    .noFade()
                    .into(imageView);


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(EditProfile.this);
                alertDialogBuilder.setMessage("How do you want to change your picture?");

                alertDialogBuilder.setPositiveButton("Gallery", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
                    }
                });

                alertDialogBuilder.setNegativeButton("Take Photo", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, CAPTURE_IMAGE_REQUEST);
                    }

                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });

        editDateOfBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String parts[] = editDateOfBirth.getText().toString().split("-");
                int day = Integer.parseInt(parts[2]);
                int month = Integer.parseInt(parts[1]) - 1;
                int year = Integer.parseInt(parts[0]);

                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, day);

                datePickerDialog = new DatePickerDialog(EditProfile.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        editDateOfBirth.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
                    }
                }, year, month, day);
                datePickerDialog.show();
                    }
        });

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        ConfirmChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedIdBlood = editBloodType.getCheckedRadioButtonId();
                int selectedRhesus = editRhesus.getCheckedRadioButtonId();
                selectBlood = (RadioButton) findViewById(selectedIdBlood);
                selectRhesus = (RadioButton) findViewById(selectedRhesus);

                String name = editName.getText().toString().trim();
                String email = editEmail.getText().toString().trim();
                String phonenumber = editPhone.getText().toString().trim();
                String dateOfBirth = editDateOfBirth.getText().toString().trim();
                String blood = selectBlood.getText().toString().trim();
                String rhesus = selectRhesus.getText().toString().trim();
                String image = getStringImage(bitmap);

                if (rhesus.equals("+ (Positive)")){
                    rhesus = "+";
                }else {
                    rhesus = "-";
                }

                String Blood = blood + rhesus;

                if (!name.isEmpty() && !email.isEmpty()  &&
                        !phonenumber.isEmpty() && !dateOfBirth.isEmpty()) {
                    editUser(uid, name, email, eMail, phonenumber, dateOfBirth, Blood, image);
                }
                else{
                    Toast.makeText(getApplicationContext(), "Please fill in your details!",
                            Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 90, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK &&
                data != null && data.getData() != null){
            Uri filePath = data.getData();
            try{
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e){
                e.printStackTrace();
            }
        }else if (requestCode == CAPTURE_IMAGE_REQUEST  && resultCode == RESULT_OK){
            bitmap = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(bitmap);
        }
    }

    private void editUser(final String uid, final String name, final String email, final String oldemail, final String phonenumber, final String dateOfBirth, final String bloodType, final String image) {

        String tag_string_req = "req_edit";

        progressDialog.setMessage("Changing Your Details...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_EDIT_PROFILE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Edit Response: " + response);
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {
                        String uid = jObj.getString("uid");

                        JSONObject user = jObj.getJSONObject("user");
                        String name = user.getString("fullName");
                        String email = user.getString("eMail");
                        String dateOfBirth = user.getString("dateOfBirth");
                        String phonenumber = user.getString("phoneNumber");
                        String bloodType = user.getString("bloodType");
                        String image = user.getString("image");

                        Picasso.with(getApplicationContext()).invalidate(imageUrl);

                        //Update user's info
                        db.updateUser(uid, name, email, dateOfBirth, phonenumber, bloodType, image);

                        Toast.makeText(getApplicationContext(), "User's profile successfully updated!", Toast.LENGTH_LONG).show();

                        //Launch User Profile
                        Intent intent = new Intent(EditProfile.this, UserProfile.class);
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
                Log.e(TAG, "Update Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<>();

                params.put("uid", uid);
                params.put("fullName", name);
                params.put("eMail", email);
                params.put("oldEmail", oldemail);
                params.put("dateOfBirth", dateOfBirth);
                params.put("phoneNumber", phonenumber);
                params.put("bloodType", bloodType);
                params.put("image", image);

                return params;
            }
        };

        // Adding request to request queue
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
        startActivity(new Intent(EditProfile.this, UserProfile.class));
        finish();
    }
}
