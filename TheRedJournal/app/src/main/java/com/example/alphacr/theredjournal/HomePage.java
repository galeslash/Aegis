package com.example.alphacr.theredjournal;

<<<<<<< 06f64a2f940a4af7be704e3efc776f71ddbec3a8

import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
=======
<<<<<<< HEAD
<<<<<<< HEAD
>>>>>>> Added rough work
import android.content.Intent;
=======
=======

>>>>>>> 19efcae... Trying to combine with master 2
import android.support.annotation.IdRes;
>>>>>>> 096dfb6... Trivia implemented
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
<<<<<<< HEAD
<<<<<<< HEAD
import android.widget.EditText;

import helper.SQLITEHandler;
import helper.SessionManager;
=======
=======
import android.widget.TextView;
>>>>>>> 096dfb6... Trivia implemented
import android.widget.Toast;
<<<<<<< HEAD
>>>>>>> 3c9ada3... drawable and stuffs

public class HomePage extends AppCompatActivity {

    EditText contactForm;
    private SQLITEHandler db;
    private SessionManager session;
<<<<<<< 06f64a2f940a4af7be704e3efc776f71ddbec3a8
=======
=======
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import helper.SQLITEHandler;
import helper.SessionManager;
>>>>>>> 19efcae... Trying to combine with master 2

public class HomePage extends AppCompatActivity {

    EditText contactForm;
    private SQLITEHandler db;
    private SessionManager session;
>>>>>>> Added rough work
    TextView factBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Facts factHolder;
        Button button;
        Button guide;
        Button btnLogOut;
        Button contactUs;
        Button changePassword;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
<<<<<<< 06f64a2f940a4af7be704e3efc776f71ddbec3a8
=======
<<<<<<< HEAD
        contactForm = (EditText) findViewById(R.id.contactUs);
>>>>>>> Added rough work

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
        btnLogOut = (Button) findViewById(R.id.btnLogOut);
        db = new SQLITEHandler(getApplicationContext());
        session = new SessionManager(getApplicationContext());
        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOutUser();
            }
        });
        contactUs = (Button) findViewById(R.id.contact);
        contactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (HomePage.this, Contact_Us.class);
                startActivity(intent);
                finish();
            }
        });
        changePassword = (Button) findViewById(R.id.changePassword);
        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePage.this, ChangePassword.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void logOutUser(){
        session.setLogin(false);
        db.deleteUsers();
        Intent intent = new Intent (HomePage.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
<<<<<<< 06f64a2f940a4af7be704e3efc776f71ddbec3a8
}
=======
    private  void logOutUser(){
    session.setLogin(false);
    db.deleteUsers();

    Intent intent = new Intent (HomePage.this, LoginActivity.class);
    startActivity(intent);
    finish();
    }
}
=======

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
        btnLogOut = (Button) findViewById(R.id.btnLogOut);
        db = new SQLITEHandler(getApplicationContext());
        session = new SessionManager(getApplicationContext());
        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOutUser();
            }
        });
        contactUs = (Button) findViewById(R.id.contact);
        contactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (HomePage.this, Contact_Us.class);
                startActivity(intent);
                finish();
            }
        });
        changePassword = (Button) findViewById(R.id.changePassword);
        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePage.this, ChangePassword.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void logOutUser(){
        session.setLogin(false);
        db.deleteUsers();
        Intent intent = new Intent (HomePage.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
<<<<<<< HEAD
    private  void logOutUser(){
    session.setLogin(false);
    db.deleteUsers();

    Intent intent = new Intent (HomePage.this, LoginActivity.class);
    startActivity(intent);
    finish();
    }
<<<<<<< HEAD
}
>>>>>>> 3c9ada3... drawable and stuffs
=======
}
>>>>>>> 19efcae... Trying to combine with master 2
=======
}
>>>>>>> cdd4217... Added some button listener changes and strings
>>>>>>> Added rough work
