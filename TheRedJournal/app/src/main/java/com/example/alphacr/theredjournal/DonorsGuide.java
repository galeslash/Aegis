package com.example.alphacr.theredjournal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;


public class DonorsGuide extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donors_guide);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView htmlTextView = (TextView)findViewById(R.id.textView4);
        String formattedText = getString(R.string.guide);
        htmlTextView.setText(Html.fromHtml(formattedText));
    }
    public void onBackPressed() {
        startActivity(new Intent(DonorsGuide.this, HomePage.class));
        finish();
    }
}
