package com.example.alphacr.theredjournal;

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
    private Button btnLogOut;

    private SQLITEHandler db;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
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