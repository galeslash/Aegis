package com.example.alphacr.theredjournal;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * Created by Revin on 07-Nov-16.
 */

public class ReyclerViewHolder extends RecyclerView.ViewHolder {
    TextView tv1, tv2, tvDate; //deklarasi textview
    ImageView imageView;

    public ReyclerViewHolder(View itemView) {
        super(itemView);
        tv1 = (TextView) itemView.findViewById(R.id.daftar_judul);
        //menampilkan text dari widget CardView pada id daftar_judul
        tv2 = (TextView) itemView.findViewById(R.id.daftar_deskripsi);
        //menampilkan text deskripsi dari widget CardView pada id daftar_deskripsi
        tvDate = (TextView) itemView.findViewById(R.id.date);
        //menampilkan text date dari widget CardView pada id date
        imageView = (ImageView) itemView.findViewById(R.id.daftar_icon);
        //menampilkan gambar atau icon pada widget Cardview pada id daftar_icon
    }
}
