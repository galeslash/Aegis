package com.example.alphacr.theredjournal;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
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
import helper.SQLITEHandler;
import helper.SessionManager;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private SQLITEHandler db;
    private SessionManager session;
    
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
        FragmentManager fragmentManager = getSupportFragmentManager();

        if (id == R.id.user_profile) {
            Intent intent = new Intent(HomeActivity.this, UserProfile.class);
            startActivity(intent);
            finish();
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
            Intent intent = new Intent(HomeActivity.this, donation_history.class);
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
