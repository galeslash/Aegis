package com.example.alphacr.theredjournal;


import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import helper.SQLITEHandler;
import helper.SessionManager;

public class HomePage extends AppCompatActivity {

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

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        // Fact Coding
        factHolder = new Facts();
        factBox = (TextView) findViewById(R.id.trivia_content);
        factBox.setText(factHolder.nextFact());

        // Button Listener
        button = (Button) findViewById(R.id.btDonor);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Donor is pressed", Toast.LENGTH_SHORT).show();
            }
        });
        guide = (Button) findViewById(R.id.btGuide);
        guide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "How to Donor is pressed", Toast.LENGTH_SHORT).show();
            }
        });

        // Nanya isaac ini apa
        db = new SQLITEHandler(getApplicationContext());
        session = new SessionManager(getApplicationContext());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
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
            Toast.makeText(getApplicationContext(), "Logout option selected", Toast.LENGTH_LONG).show();
            logOutUser();
            return true;
            // Contact Us
        } else if (id == R.id.action_contact_us) {
            Toast.makeText(getApplicationContext(), "Contact Us option selected", Toast.LENGTH_LONG).show();
            Intent intent = new Intent (HomePage.this, Contact_Us.class);
            startActivity(intent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void logOutUser(){
        session.setLogin(false);
        db.deleteUsers();
        Intent intent = new Intent (HomePage.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}