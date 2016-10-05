package com.example.alphacr.theredjournal;

import android.media.RemoteController;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;


public class Contact_Us extends AppCompatActivity {
    EditText contact;
    String str_contact;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact__us);
        contact= (EditText)findViewById(R.id.contactUs);
    }
    public void onConfirm(View view){
        str_contact = contact.getText().toString();
        String type = "contact";
        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        backgroundWorker.execute(type, str_contact);


    }
}
