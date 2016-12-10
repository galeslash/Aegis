package com.example.alphacr.theredjournal;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import helper.SQLITEHandler;
import helper.SessionManager;



public class UserProfile extends Fragment {

    private SQLITEHandler db;
    private SessionManager sessionManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final FragmentActivity faActivity  = (FragmentActivity)    super.getActivity();
        // Replace LinearLayout by the type of the root element of the layout you're trying to load
        LinearLayout llLayout    = (LinearLayout)    inflater.inflate(R.layout.activity_user_profile, container, false);
        // Of course you will want to faActivity and llLayout in the class and not this method to access them in the rest of
        // the class, just initialize them here

        // Content of previous onCreate() here
        // ...
        TextView txtName = (TextView) llLayout.findViewById(R.id.name);
        TextView txtEmail = (TextView) llLayout.findViewById(R.id.Email);
        TextView txtDateOfBirth = (TextView) llLayout.findViewById(R.id.dob);
        TextView txtGender = (TextView) llLayout.findViewById(R.id.Gender);
        TextView txtPhone = (TextView) llLayout.findViewById(R.id.phone);
        TextView txtBloodType = (TextView) llLayout.findViewById(R.id.blood);
        CircleImageView imageView = (CircleImageView) llLayout.findViewById(R.id.userImage);

        Button editProfile;
        Button changePassword;

        db = new SQLITEHandler(super.getActivity().getApplicationContext());
        sessionManager = new SessionManager(super.getActivity().getApplicationContext());


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
            Picasso.with(super.getContext())
                    .load(imageUrl)
                    .placeholder(R.drawable.photo)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .error(R.drawable.photo)
                    .noFade()
                    .into(imageView);
        } else{
            Picasso.with(super.getContext())
                    .load(R.drawable.photo)
                    .placeholder(R.drawable.photo)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .error(R.drawable.photo)
                    .noFade()
                    .into(imageView);
        }


        editProfile = (Button) llLayout.findViewById(R.id.profileButton);
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserProfile.super.getActivity(), EditProfile.class);
                startActivity(intent);
                UserProfile.super.getActivity().finish();
            }
        });
        changePassword = (Button) llLayout.findViewById(R.id.passButton);
        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserProfile.super.getActivity(), ChangePassword.class);
                startActivity(intent);
                UserProfile.super.getActivity().finish();
            }
        });
        // Don't use this method, it's handled by inflater.inflate() above :
        // setContentView(R.layout.activity_layout);

        // The FragmentActivity doesn't contain the layout directly so we must use our instance of     LinearLayout :
        // Instead of :
        // findViewById(R.id.someGuiElement);
        return llLayout; // We must return the loaded Layout
    }
}
