package com.example.alphacr.theredjournal;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;


/**
 * Created by Revin on 07-Nov-16.
 */





public class ReyclerAdapter extends RecyclerView.Adapter<ReyclerViewHolder> {
    private final Context context;

    //String [] name={"Rumah Sakit Pusat Pertamina", "penis", "pneis"};
    ArrayList<String> address;
    ArrayList<String> name;
    ArrayList<String> bloodType;
    ArrayList<String> dateOfRequest;

    // menampilkan list item dalam bentuk text dengan tipe data string dengan variable name

    LayoutInflater inflater;
    public ReyclerAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }
    public void Data(ArrayList<String> Name, ArrayList<String>  Address,
                     ArrayList<String>  BloodType, ArrayList<String> DateOfRequest){
        name = Name;
        address = Address;
        bloodType = BloodType;
        dateOfRequest = DateOfRequest;
    }
    @Override
    public ReyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v=inflater.inflate(R.layout.item_list, parent, false);

        ReyclerViewHolder viewHolder=new ReyclerViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ReyclerViewHolder holder, int position) {
        holder.tv1.setText(name.get(position));
        holder.tv1.setOnClickListener(clickListener);
        holder.tv2.setText(address.get(position));
        holder.tv2.setOnClickListener(clickListener);
        holder.tvDate.setText(dateOfRequest.get(position));
        holder.imageView.setOnClickListener(clickListener);
        holder.tv1.setTag(holder);
        holder.imageView.setTag(holder);
        holder.tv2.setTag(holder);

    }

    View.OnClickListener clickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
//member aksi saat cardview diklik berdasarkan posisi tertentu
            ReyclerViewHolder vholder = (ReyclerViewHolder) v.getTag();

            int position = vholder.getLayoutPosition();

            Toast.makeText(context, "Menu ini berada di posisi " + position, Toast.LENGTH_LONG).show();


        }
    };

    @Override
    public int getItemCount() {
        return name.size();
    }
}
