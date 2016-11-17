package com.example.alphacr.theredjournal;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.vision.text.Line;


public class DonorsGuide extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final FragmentActivity faActivity  = (FragmentActivity)    super.getActivity();
        // Replace LinearLayout by the type of the root element of the layout you're trying to load
        FrameLayout frameLayout    = (FrameLayout)    inflater.inflate(R.layout.activity_donors_guide, container, false);
        // Of course you will want to faActivity and llLayout in the class and not this method to access them in the rest of
        // the class, just initialize them here

        // Content of previous onCreate() here
        // ...

        // Don't use this method, it's handled by inflater.inflate() above :
        // setContentView(R.layout.activity_layout);

        TextView htmlTextView = (TextView) frameLayout.findViewById(R.id.textView4);
        String formattedText = getString(R.string.guide);
        htmlTextView.setText(Html.fromHtml(formattedText));

        // The FragmentActivity doesn't contain the layout directly so we must use our instance of     LinearLayout :
        // Instead of :
        // findViewById(R.id.someGuiElement);
        return frameLayout; // We must return the loaded Layout
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item){
//        switch (item.getItemId()){
//            // Respond to the action bar's up/home button
 //           case android.R.id.home:
//                getActivity().getSupportFragmentManager().popBackStack();
//                return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
}
