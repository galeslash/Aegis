package firebase;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.example.alphacr.theredjournal.AppConfig;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by ameli_000 on 10-Dec-16.
 */

public class FirebaseId extends FirebaseInstanceIdService {
    private static final String TAG = FirebaseId.class.getSimpleName();

    @Override
    public void onTokenRefresh(){
        super.onTokenRefresh();
        String refreshToken = FirebaseInstanceId.getInstance().getToken();

        storeRegIdInPref(refreshToken);

        Log.d(TAG, "Refreshed Token: " + refreshToken);

        Intent registrationComplete = new Intent(AppConfig.REGISTRATION_COMPLETE);
        registrationComplete.putExtra("token", refreshToken);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);

    }

    private void storeRegIdInPref(String refreshToken) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(
                AppConfig.SHARED_PREF,0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("regId", refreshToken);
        editor.apply();
    }

}
