package com.example.alphacr.theredjournal;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

public class HistoryDetails extends AppCompatActivity {
    private TextView title, date, time, serviceText, service, amountText, amount, typeText, type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_details);

        Typeface customFont = Typeface.createFromAsset(getAssets(), "fonts/Lato-Regular.ttf");
        Typeface customFont2 = Typeface.createFromAsset(getAssets(), "fonts/Lato-Bold.ttf");

        title = (TextView) findViewById(R.id.detailsTitle);
        title.setTypeface(customFont);

        date = (TextView) findViewById(R.id.detailsDate);
        date.setTypeface(customFont2);

        time = (TextView) findViewById(R.id.detailsTime);
        time.setTypeface(customFont2);

        serviceText = (TextView) findViewById(R.id.detailsServiceText);
        serviceText.setTypeface(customFont);

        service = (TextView) findViewById(R.id.detailsService);
        service.setTypeface(customFont2);

        amountText = (TextView) findViewById(R.id.detailsAmountText);
        amountText.setTypeface(customFont);

        amount = (TextView) findViewById(R.id.detailsAmount);
        amountText.setTypeface(customFont2);

        typeText = (TextView) findViewById(R.id.detailsTypeText);
        typeText.setTypeface(customFont);

        type = (TextView) findViewById(R.id.detailsType);
        type.setTypeface(customFont2);
    }
}
