package com.example.alphacr.theredjournal;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Text;

public class request_blood extends AppCompatActivity {
    Spinner spinner, spinner2;
    ArrayAdapter<CharSequence> adapter, adapter2;
    private TextView title,blood, amount, info;
    private Button submit;
    private EditText editfield;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_blood);
        spinner = (Spinner) findViewById(R.id.bloodTypeSpinner);
        adapter = ArrayAdapter.createFromResource(this,R.array.blood_types,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        Typeface customFont = Typeface.createFromAsset(getAssets(), "fonts/Lato-Regular.ttf");
        Typeface customFont2 = Typeface.createFromAsset(getAssets(), "fonts/OSP-DIN.ttf");

        title = (TextView) findViewById(R.id.requestText);
        title.setTypeface(customFont2);

        blood = (TextView) findViewById(R.id.typeText);
        blood.setTypeface(customFont);

        amount = (TextView) findViewById(R.id.amountText);
        amount.setTypeface(customFont);

        editfield = (EditText) findViewById(R.id.amountField);
        editfield.setTypeface(customFont);

        info = (TextView) findViewById(R.id.amountInfo);
        info.setTypeface(customFont);

        submit = (Button) findViewById(R.id.requestButton);
        submit.setTypeface(customFont);

    }
}
