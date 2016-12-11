package com.example.alphacr.theredjournal;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

public class notification_detail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_detail);

        Bundle bundle = getIntent().getExtras();
        String name = bundle.getString("fullName");
        String phone = bundle.getString("phoneNumber");
        String mail = bundle.getString("eMail");
        String donorPic = bundle.getString("image");

        TextView donorName = (TextView) findViewById(R.id.nameHolder);
        donorName.setText(name);

        TextView phoneNumber = (TextView) findViewById(R.id.phoneHolder);
        phoneNumber.setText(phone);

        TextView eMail = (TextView) findViewById(R.id.emailHolder);
        eMail.setText(mail);

        CircleImageView circleImageView = (CircleImageView) findViewById(R.id.donorPic);

        if(donorPic!=null) {
            Picasso.with(getApplicationContext())
                    .load(donorPic)
                    .placeholder(R.drawable.photo)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .error(R.drawable.photo)
                    .noFade()
                    .into(circleImageView);
        } else{
            Picasso.with(getApplicationContext())
                    .load(R.drawable.photo)
                    .placeholder(R.drawable.photo)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .error(R.drawable.photo)
                    .noFade()
                    .into(circleImageView);
        }
    }
}
