package com.example.alphacr.theredjournal;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.HashMap;

import helper.SQLITEHandler;
import helper.SessionManager;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    EditText contactForm;
    private SQLITEHandler db;
    private SessionManager session;
    TextView factBox;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Facts factHolder;
        Button button;
        Button guide;
        Button btnLogOut;
        Button contactUs;
        Button requestBlood;
        Button userProfile;
        
        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        
        contactForm = (EditText) findViewById(R.id.contactUs);

        // Fact Coding
        factHolder = new Facts();
        factBox = (TextView) findViewById(R.id.trivia_content);
        factBox.setText(factHolder.nextFact());

        // Button Listener
        button = (Button) findViewById(R.id.btDonor);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (HomeActivity.this, MapsActivity.class);
                startActivity(intent);
                finish();
            }
        });
        guide = (Button) findViewById(R.id.btGuide);
        guide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (HomeActivity.this, DonorsGuide.class);
                startActivity(intent);
                finish();
            }
        });

        // Nanya isaac ini apa
        db = new SQLITEHandler(getApplicationContext());
        session = new SessionManager(getApplicationContext());

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.user_profile) {
            Intent intent = new Intent(HomeActivity.this, UserProfile.class);
            startActivity(intent);
            finish();
            return true;
        } else if (id == R.id.blood_request) {
            Intent intent = new Intent (HomeActivity.this, request_blood.class);
            startActivity(intent);
            finish();
            return true;
        } else if (id == R.id.contact_us) {
            Intent intent = new Intent (HomeActivity.this, Contact_Us.class);
            startActivity(intent);
            finish();
            return true;
        } else if (id == R.id.log_out) {
            logOutUser();
            return true;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logOutUser(){
        HashMap<String, String> user = db.getUserDetails();
        String imageUrl = user.get("image");
        if(imageUrl != null) {
            Picasso.with(getApplicationContext()).invalidate(imageUrl);
        }

        session.setLogin(false);
        db.deleteUsers();

        Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
