package com.w3engineers.core.util;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.w3engineers.core.util.helper.PreferenceKey;
import com.w3engineers.core.util.helper.SharedPref;
import com.w3engineers.core.videon.ui.home.HomeActivity;

import org.json.JSONException;
import org.json.JSONObject;




public class VideonFirebaseInstanceIDService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMsgService";

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages
        // are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data
        // messages are the type
        // traditionally used with GCM. Notification messages are only received here in
        // onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated
        // notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages
        // containing both notification
        // and data payloads are treated as notification messages. The Firebase console always
        // sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getData().get("message"));
        String message=remoteMessage.getData().get("message");
        try {
            JSONObject jsonObject=new JSONObject(message);
            boolean notifiaction_status=true;
            try{
                notifiaction_status= SharedPref.readBoolean(PreferenceKey.NOTIFICATION_STATUS);
            }catch (Exception e)
            {
                SharedPref.write(PreferenceKey.NOTIFICATION_STATUS,true);
            }
            if(notifiaction_status) {
                sendNotification(jsonObject.getString("title"), jsonObject.getString("desc"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Check if message contains a data payload.
//        if (remoteMessage.getData().size() > 0) {
//            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
//
//            if (/* Check if data needs to be processed by long running job */ true) {
//                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
//               // scheduleJob();
//                sendNotification(remoteMessage.getNotification().getTitle(),remoteMessage.getNotification().getBody());
//            } else {
//                // Handle message within 10 seconds
//                handleNow();
//                Log.d("handle","Auto Handled");
//            }
//
//        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }
    // [END receive_message]
    /**
     * Handle time allotted to BroadcastReceivers.
     */
    private void handleNow() {
        Log.d(TAG, "Short lived task is done.");
    }
    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.
    }
    // [END on_new_token]

    /**
     * Schedule a job using FirebaseJobDispatcher.
     */
    private void scheduleJob() {
        // [START dispatch_job]

      //  FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(this));
     //   Job myJob = dispatcher.newJobBuilder().setService(MyJobService.class).setTag("my-job-tag").build();
        //dispatcher.schedule(myJob);
        // [END dispatch_job]
    }
    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private void sendNotification(String messageBody, String title) {
        Intent intent = new Intent(this,HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId ="1111";
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
                        //.setContentTitle(getString(R.string.fcm_message))
                        .setContentTitle(title)
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

}
