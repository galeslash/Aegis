package com.example.alphacr.theredjournal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
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

    @Override
    public void onBackPressed() {
        startActivity(new Intent(DonorsGuide.this, HomeActivity.class));
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            // Respond to the action bar's up/home button
            case android.R.id.home:
                Intent intent  = new Intent(this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
