package com.example.alphacr.theredjournal;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

public class AcceptingBlood extends AppCompatActivity {
    private static final String TAG = AcceptingBlood.class.getSimpleName();
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accepting_blood);
        TextView userRequest = (TextView) findViewById(R.id.titleUserRequest);
        TextView requestType = (TextView) findViewById(R.id.requestType);
        TextView requestAmount = (TextView) findViewById(R.id.requestAmount);
        CircleImageView requestPicture = (CircleImageView) findViewById(R.id.userProfileView);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        Bundle bundle = getIntent().getExtras();
        final String user = bundle.getString("name");
        final String bloodType = bundle.getString("bloodType");
        final String amount = bundle.getString("amount");

        userRequest.setText(user + "wants to request blood");
        requestType.setText(bloodType);
        requestAmount.setText(amount);

        Button acceptRequest = (Button) findViewById(R.id.acceptRequestButton);
        acceptRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                responseRequest(user, bloodType, amount);
            }
        });

    }

    private void responseRequest(String user, String bloodType, String amount) {

    }
}


