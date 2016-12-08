package com.example.alphacr.theredjournal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.lang.reflect.Array;

public class donation_history extends AppCompatActivity {
    private static final String TAG = donation_history.class.getSimpleName();
    RecyclerView recyclerView;


    String [] name = {"Rumah Sakit Pusat Pertamina", "MENTAL", "pneis"};
    String [] address= {"Jakarta", "Depok", "penis"};
    String [] bloodType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation_history);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        //menampilkan reyclerview yang ada pada file layout dengan id reycler view
        getHistory();

        ReyclerAdapter adapter = new ReyclerAdapter(this);
        adapter.Data(name, address, bloodType);
        //membuat adapter baru untuk reyclerview
        recyclerView.setAdapter(adapter);
        //menset nilai dari adapter
        recyclerView.setHasFixedSize(true);
        //menset setukuran
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //menset layoutmanager dan menampilkan daftar/list
        //dalam bentuk linearlayoutmanager pada class saat ini
    }

    private void getHistory() {
       //buat string request, buat jsonnya dibagi jadi 3 variable array
        // misal : response = {uid : "abidu', user : {name : 'aoids', blood : 'a'}}
        // ditaro di variable uid = {} name = {} blood = {}
    }

    @Override
        public void onBackPressed(){
            startActivity(new Intent(donation_history.this,HomeActivity.class));
            finish();

        }
        @Override
        public boolean onOptionsItemSelected(MenuItem item){
            switch (item.getItemId()) {
                // Respond to the action bar's Up/Home button
                case android.R.id.home:
                    Intent intent = new Intent(this, HomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                    return true;
            }
            return super.onOptionsItemSelected(item);
        }

    }

