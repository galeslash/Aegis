package com.example.alphacr.theredjournal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import helper.SQLITEHandler;
import helper.SessionManager;

public class UserProfile extends AppCompatActivity {

    private SQLITEHandler db;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        TextView txtName = (TextView) findViewById(R.id.name);
        TextView txtEmail = (TextView) findViewById(R.id.Email);
        TextView txtDateOfBirth = (TextView) findViewById(R.id.dob);
        TextView txtGender = (TextView) findViewById(R.id.Gender);
        TextView txtPhone = (TextView) findViewById(R.id.phone);
        TextView txtBloodType = (TextView) findViewById(R.id.blood);
        CircleImageView imageView = (CircleImageView) findViewById(R.id.userImage);

        Button editProfile;
        Button changePassword;

        db = new SQLITEHandler(getApplicationContext());
        sessionManager = new SessionManager(getApplicationContext());

        if (!sessionManager.isLoggedIn()) {
            logoutUser();
        }

        HashMap<String, String> user = db.getUserDetails();
        String name = user.get("fullName");
        String eMail = user.get("eMail");
        String dateOfBirth = user.get("dateOfBirth");
        String phone = user.get("phoneNumber");
        String gender = user.get("gender");
        String bloodType = user.get("bloodType");
        String imageUrl = user.get("image");

        txtName.setText(name);
        txtEmail.setText(eMail);
        txtDateOfBirth.setText(dateOfBirth);
        txtPhone.setText(phone);
        txtGender.setText(gender);
        txtBloodType.setText(bloodType);

        if(imageUrl!=null) {
            Picasso.with(this)
                    .load(imageUrl)
                    .placeholder(R.drawable.photo)
                    .error(R.drawable.photo)
                    .noFade()
                    .into(imageView);
        } else{
            Picasso.with(this)
                    .load(R.drawable.photo)
                    .placeholder(R.drawable.photo)
                    .error(R.drawable.photo)
                    .noFade()
                    .into(imageView);
        }


        editProfile = (Button) findViewById(R.id.profileButton);
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserProfile.this, EditProfile.class);
                startActivity(intent);
                finish();
            }
        });
        changePassword = (Button) findViewById(R.id.passButton);
        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserProfile.this, ChangePassword.class);
                startActivity(intent);
                finish();
            }
        });
    }
    private void logoutUser() {
        sessionManager.setLogin(false);

        db.deleteUsers();

        // Launching the login activity
        Intent intent = new Intent(UserProfile.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(UserProfile.this, HomePage.class));
        finish();
    }
}
