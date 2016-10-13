package com.example.alphacr.theredjournal;


import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

        contactForm = (EditText) findViewById(R.id.contactUs);

        btnLogOut = (Button) findViewById(R.id.btnLogOut);

        db = new SQLITEHandler(getApplicationContext());

        session = new SessionManager(getApplicationContext());

        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOutUser();
            }
        });


    }

    public void contactUs(View view) {

        startActivity(new Intent(this, Contact_Us.class));
        finish();
    }
    private  void logOutUser(){
    session.setLogin(false);
    db.deleteUsers();

    Intent intent = new Intent (HomePage.this, LoginActivity.class);
    startActivity(intent);
    finish();
    }
}