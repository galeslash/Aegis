package com.example.alphacr.theredjournal;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.widget.TextView;
import android.support.v4.app.Fragment;


public class DonorsGuide extends AppCompatActivity {
    private TextView title, heading1, body1, heading2, body2, heading3, body3;
    private String titleStr, heading1Str, body1Str, heading2Str, body2Str, heading3Str, body3Str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donors_guide);
        Typeface customFont = Typeface.createFromAsset(getAssets(), "fonts/Lato-Regular.ttf");
        Typeface customFont2 = Typeface.createFromAsset(getAssets(), "fonts/Questrial-Regular.ttf");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        title = (TextView)findViewById(R.id.guideTitle);
        titleStr = getString(R.string.guide_title);
        title.setText(Html.fromHtml(titleStr));
        title.setTypeface(customFont2);

        heading1 = (TextView)findViewById(R.id.guideHeading1);
        heading1Str = getString(R.string.guide_heading1);
        heading1.setText(Html.fromHtml(heading1Str));
        heading1.setTypeface(customFont);

        body1 = (TextView)findViewById(R.id.guideBody1);
        body1Str = getString(R.string.guide_body1);
        body1.setText(Html.fromHtml(body1Str));
        body1.setTypeface(customFont);

        heading2 = (TextView)findViewById(R.id.guideHeading2);
        heading2Str = getString(R.string.guide_heading2);
        heading2.setText(Html.fromHtml(heading2Str));
        heading2.setTypeface(customFont);

        body2 = (TextView)findViewById(R.id.guideBody2);
        body2Str = getString(R.string.guide_body2);
        body2.setText(Html.fromHtml(body2Str));
        body2.setTypeface(customFont);

        heading3 = (TextView)findViewById(R.id.guideHeading3);
        heading3Str = getString(R.string.guide_heading3);
        heading3.setText(Html.fromHtml(heading3Str));
        heading3.setTypeface(customFont);

        body3 = (TextView)findViewById(R.id.guideBody3);
        body3Str = getString(R.string.guide_body3);
        body3.setText(Html.fromHtml(body3Str));
        body3.setTypeface(customFont);


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
