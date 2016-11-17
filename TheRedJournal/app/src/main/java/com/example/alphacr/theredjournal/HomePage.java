package com.example.alphacr.theredjournal;


import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.EditText;

import com.squareup.picasso.Picasso;

import java.util.HashMap;

import helper.SQLITEHandler;
import helper.SessionManager;

import static android.R.attr.id;

public class HomePage extends Fragment {

    private SQLITEHandler db;
    private SessionManager session;
    TextView factBox;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final FragmentActivity    faActivity  = (FragmentActivity)    super.getActivity();
        // Replace LinearLayout by the type of the root element of the layout you're trying to load
        LinearLayout llLayout    = (LinearLayout)    inflater.inflate(R.layout.activity_home_page, container, false);
        // Of course you will want to faActivity and llLayout in the class and not this method to access them in the rest of
        // the class, just initialize them here


        Facts factHolder;
        Button button;
        Button guide;
        Button btnLogOut;
        Button contactUs;
        Button requestBlood;
        Button userProfile;
        Button donationHistory;

        // Content of previous onCreate() here
        // ...

        // Fact Coding
        factHolder = new Facts();
        factBox = (TextView) llLayout.findViewById(R.id.trivia_content);
        factBox.setText(factHolder.nextFact());

        // Button Listener
        button = (Button) llLayout.findViewById(R.id.btDonor);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (faActivity, MapsActivity.class);
                startActivity(intent);
                faActivity.finish();
            }
        });
        guide = (Button) llLayout.findViewById(R.id.btGuide);
        guide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.content_home, new DonorsGuide())
                        .addToBackStack(null).commit();
            }
        });

        // Don't use this method, it's handled by inflater.inflate() above :
        // setContentView(R.layout.activity_layout);

        // The FragmentActivity doesn't contain the layout directly so we must use our instance of     LinearLayout :
        // Instead of :
        // findViewById(R.id.someGuiElement);
        return llLayout; // We must return the loaded Layout
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml
        int id = item.getItemId();

        // noinspection SimplifiableIfStatement
        // Logout
        if (id == R.id.action_logout){
            logOutUser();
            return true;
            // Contact Us
        } else if (id == R.id.action_contact_us) {
            Intent intent = new Intent (super.getActivity(), Contact_Us.class);
            startActivity(intent);
            super.getActivity().finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Logout Function
    private void logOutUser(){
        HashMap<String, String> user = db.getUserDetails();
        String imageUrl = user.get("image");
        if(imageUrl != null) {
            Picasso.with(super.getActivity().getApplicationContext()).invalidate(imageUrl);
        }

        session.setLogin(false);
        db.deleteUsers();

        Intent intent = new Intent(super.getActivity(), LoginActivity.class);
        startActivity(intent);
        super.getActivity().finish();
    }
}