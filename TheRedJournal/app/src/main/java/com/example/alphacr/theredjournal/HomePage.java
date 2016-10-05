package com.example.alphacr.theredjournal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class HomePage extends AppCompatActivity {
    EditText contactForm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        contactForm = (EditText) findViewById(R.id.contactUs);
    }

    public void contactUs(View view) {
        startActivity(new Intent(this, Contact_Us.class));
    }
}