package com.example.alphacr.theredjournal;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Ucer on 10/4/2016.
 */

public class BackgroundWorker extends AsyncTask<String, Void, String>    {
    Context context;
    AlertDialog alertDialog;
    BackgroundWorker(Context ctx){
        context = ctx;
    }
    @Override
    protected String doInBackground(String... params) {
        String type = params[0];
        String contact_url = "http://redjournal.hol.es/Contact_form.php";
        String register_url = "http://redjournal.hol.es/register.php";
        URL url = null;
        try {
            if(type.equals("contact")){
                url = new URL(contact_url);
            }
            else if(type.equals("register")) {
                url = new URL(register_url);
            }
            try {
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                if(type.equals("contact")){
                    String contact = params[1];
                    String post_data = URLEncoder.encode("contact","UTF-8")+"="+URLEncoder.encode(contact,"UTF-8");
                    bufferedWriter.write(post_data);
                }
                else if(type.equals("register")){
                    String fullName = params[1];
                    String eMail = params[2];
                    String phoneNumber = params[3];
                    String password = params[4];
                    String age = params[5];
                    String gender = params[6];
                    String bloodType = params[7];
                    String post_data = URLEncoder.encode("fullName","UTF-8")+"="+URLEncoder.encode(fullName,"UTF-8")+"&"
                            + URLEncoder.encode("eMail","UTF-8")+"="+URLEncoder.encode(eMail,"UTF-8")+"&"
                            + URLEncoder.encode("phoneNumber","UTF-8")+"="+URLEncoder.encode(phoneNumber,"UTF-8")+"&"
                            + URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(password,"UTF-8")+"&"
                            + URLEncoder.encode("age","UTF-8")+"="+URLEncoder.encode(age,"UTF-8")+"&"
                            + URLEncoder.encode("gender","UTF-8")+"="+URLEncoder.encode(gender,"UTF-8")+"&"
                            + URLEncoder.encode("bloodType","UTF-8")+"="+URLEncoder.encode(bloodType,"UTF-8");
                    bufferedWriter.write(post_data);
                }
                bufferedWriter.flush();
                bufferedWriter.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String result="";
                String line="";
                while((line= bufferedReader.readLine())!=null){
                    result += line;
                }
                inputStream.close();
                httpURLConnection.disconnect();
                return result;
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }



        return null;
    }

    @Override
    protected void onPreExecute() {
        alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("test");
    }

    @Override
    protected void onPostExecute(String result) {
        alertDialog.setMessage(result);
        alertDialog.show();
    }

    @Override
    protected void onProgressUpdate(Void... values) {

        super.onProgressUpdate(values);
    }
}
