package firebase;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.example.alphacr.theredjournal.AppConfig;
import com.example.alphacr.theredjournal.R;
import com.example.alphacr.theredjournal.notification_detail;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

/**
 */

public class FirebaseMsgService extends FirebaseMessagingService {
    private static final String TAG = FirebaseMsgService.class.getSimpleName();

    private NotificationUtils notificationUtils;
    private JSONObject json;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage){
        Log.e(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage == null)
            return;

        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());

            try {
                JSONObject json = new JSONObject(remoteMessage.getData().toString());
                handleDataMessage(json);
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
            handleNotification(remoteMessage.getNotification().getBody());

            try {
                json = new JSONObject(remoteMessage.getData().toString());
                handleDataMessage(json);
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }
        Log.d(TAG, "FROM:" + remoteMessage.getFrom());
        Log.d(TAG, "Notification Message Body:" + remoteMessage.getNotification().getBody());
    }

    private void handleDataMessage(JSONObject json) {
        Log.e(TAG, "push json: " + json.toString());

        try {
            JSONObject payload = json.getJSONObject("payload");
            String title = "Red Journal";
            String message = payload.getString("message");
            String name = payload.getString("fullName");
            String phoneNumber = payload.getString("phoneNumber");
            String eMail = payload.getString("eMail");
            String image = payload.getString("image");
            boolean isBackground = json.getBoolean("isBackground");

            Log.e(TAG, "title: " + title);
            Log.e(TAG, "message: " + message);
            Log.e(TAG, "isBackground: " + isBackground);
            Log.e(TAG, "payload: " + json.toString());


            if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
                // app is in foreground, broadcast the push message
                //Intent pushNotification = new Intent(AppConfig.PUSH_NOTIFICATION);
                //pushNotification.putExtra("message", message);
                //LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

                Intent resultIntent = new Intent(getApplicationContext(), notification_detail.class);
                resultIntent.putExtra("fullName", name);
                resultIntent.putExtra("phoneNumber", phoneNumber);
                resultIntent.putExtra("eMail", eMail);
                resultIntent.putExtra("image", image);


                notificationUtils = new NotificationUtils(getApplicationContext());
                resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                notificationUtils.showNotificationMessage(title, message, resultIntent);

                // play notification sound
                NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                notificationUtils.playNotificationSound();
            } else {
                // app is in background, show the notification in notification tray
                Intent resultIntent = new Intent(this, notification_detail.class);
                resultIntent.putExtra("fullName", name);
                resultIntent.putExtra("phoneNumber", phoneNumber);
                resultIntent.putExtra("eMail", eMail);
                resultIntent.putExtra("image", image);

                notificationUtils = new NotificationUtils(getApplicationContext());
                resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                notificationUtils.showNotificationMessage(title, message, resultIntent);

                // play notification sound
                NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                notificationUtils.playNotificationSound();
            }
        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }

    private void handleNotification(String message) {
        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {

            Intent resultIntent = new Intent(getApplicationContext(), notification_detail.class);
            resultIntent.putExtra("message", message);

            String title = "Red Journal";

            notificationUtils = new NotificationUtils(getApplicationContext());
            resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            notificationUtils.showNotificationMessage(title, message, resultIntent);

            // play notification sound
            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            notificationUtils.playNotificationSound();
        }else{
            // If the app is in background, firebase itself handles the notification
            Intent resultIntent = new Intent(this, notification_detail.class);
            resultIntent.putExtra("message", message);

            String title = "Red Journal";

            notificationUtils = new NotificationUtils(getApplicationContext());
            resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            notificationUtils.showNotificationMessage(title, message, resultIntent);

            // play notification sound
            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            notificationUtils.playNotificationSound();

        }
    }
}
