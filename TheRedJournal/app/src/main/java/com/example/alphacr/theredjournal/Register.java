package com.example.alphacr.theredjournal;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class Register extends AppCompatActivity {
    EditText fullname, eMail, phoneNumber, password, age, gender, bloodType;
    String str_fullname, str_eMail, str_phoneNumber, str_password, str_age,
            str_gender, str_bloodType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        fullname = (EditText)findViewById(R.id.fullName);
        eMail = (EditText)findViewById(R.id.eMail);
        phoneNumber = (EditText)findViewById(R.id.phoneNumber);
        password = (EditText)findViewById(R.id.password);
        age = (EditText)findViewById(R.id.age);
        gender = (EditText)findViewById(R.id.gender);
        bloodType = (EditText)findViewById(R.id.bloodType);
    }

    public void onReg (View view){
        str_fullname = fullname.getText().toString();
        str_eMail = eMail.getText().toString();
        str_phoneNumber = phoneNumber.getText().toString();
        str_password = password.getText().toString();
        str_age = age.getText().toString();
        str_gender = gender.getText().toString();
        str_bloodType = bloodType.getText().toString();
        String type = "register";
        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        backgroundWorker.execute(type, str_fullname, str_eMail, str_phoneNumber, str_password,
                str_age, str_gender, str_bloodType);
    }
}
