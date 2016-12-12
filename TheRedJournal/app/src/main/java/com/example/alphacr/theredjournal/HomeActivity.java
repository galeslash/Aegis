package com.example.alphacr.theredjournal;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
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
import android.widget.Toast;

import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import firebase.NotificationUtils;
import helper.SQLITEHandler;
import helper.SessionManager;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private SQLITEHandler db;
    private SessionManager session;

    TextView factBox;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        HomePage fragment = new HomePage();
        android.support.v4.app.FragmentTransaction fragmentTransaction =
                getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_home, fragment);
        fragmentTransaction.commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header=navigationView.getHeaderView(0);

        CircleImageView imageView = (CircleImageView) header.findViewById(R.id.user_profile);

        TextView nav_name = (TextView) header.findViewById(R.id.navbar_name);
        TextView nav_email = (TextView) header.findViewById(R.id.navbar_email);


        // Nanya isaac ini apa
        db = new SQLITEHandler(getApplicationContext());
        session = new SessionManager(getApplicationContext());


        HashMap<String, String> user = db.getUserDetails();
        String uname = user.get("fullName");
        String email = user.get("eMail");
        String imageUrl = user.get("image");

        if (!session.isLoggedIn()) {
            logOutUser();
        }

        if(imageUrl!=null) {
            Picasso.with(this)
                    .load(imageUrl)
                    .placeholder(R.drawable.photo)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .error(R.drawable.photo)
                    .noFade()
                    .into(imageView);
        } else{
            Picasso.with(this)
                    .load(R.drawable.photo)
                    .placeholder(R.drawable.photo)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .error(R.drawable.photo)
                    .noFade()
                    .into(imageView);
        }

        nav_name.setText(uname);
        nav_email.setText(email);

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();

        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(AppConfig.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(AppConfig.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        //NotificationUtils.clearNotifications(getApplicationContext());
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if(getFragmentManager().getBackStackEntryCount() == 0) {
                super.onBackPressed();
            }
            else {
                getFragmentManager().popBackStack();
            }
        }
    }
    

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentManager fragmentManager = getSupportFragmentManager();


        if (id == R.id.user_profile) {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            }
            fragmentManager.beginTransaction()
                    .replace(R.id.content_home, new UserProfile())
                    .addToBackStack(null).commit();
            return true;
        } else if (id == R.id.contact_us) {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            }
            fragmentManager.beginTransaction()
                    .replace(R.id.content_home, new Contact_Us())
                    .addToBackStack(null).commit();
            return true;
        } else if (id == R.id.log_out) {
            logOutUser();
            return true;
        } else if (id == R.id.donation_history) {
            Intent intent = new Intent (HomeActivity.this, donation_history.class);
            startActivity(intent);
            finish();
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
