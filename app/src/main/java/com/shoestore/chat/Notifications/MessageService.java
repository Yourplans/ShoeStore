package com.shoestore.chat.Notifications;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.shoestore.chat.ChatActivity;
import com.shoestore.chat.UserDetails;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Daniel on 28/04/2017.
 */

public class MessageService extends FirebaseMessagingService {

    private  static  final  String TAG =MessageService.class.getSimpleName();

    private NotificationUtils mNotificationUtils;


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Log.e(TAG, "From "+ remoteMessage.getFrom());

        if (remoteMessage ==null){
            return;
        }

        if (remoteMessage.getNotification()!=null){
            Log.e(TAG, "Notification Body:  " + remoteMessage.getNotification().getBody());
            handleNotification (remoteMessage.getNotification().getBody());
        }

        if (remoteMessage.getData().size() >0){
            Log.e(TAG,"Data Payload: "+ remoteMessage.getData().toString());

            try {
                JSONObject mJsonObject = new JSONObject(remoteMessage.getData().toString());
                handleDataMessage(mJsonObject);
            }catch (JSONException e){
                Log.e(TAG,"Exception : "+ e.getMessage());
            }
        }

    }

    private void handleDataMessage(JSONObject mJsonObject) {
        Log.e(TAG, "push json : "+ mJsonObject.toString());

        try{

            JSONObject mJsonData  = mJsonObject.getJSONObject("data");

            String title  = mJsonData.getString("title");
            String message = mJsonData.getString("message");
            boolean isBackground= mJsonData.getBoolean("is_background");
            String imageUrl = mJsonData.getString("image");
            String timeStamp = mJsonData.getString("timestamp");
            JSONObject payload = mJsonData.getJSONObject("payload");

            UserDetails.userChat=title;

            if (!NotificationUtils.isAppBackground(getApplicationContext())){
                Log.e("Not Is Back","******");
                Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
                pushNotification.putExtra("message",message);
                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

                NotificationUtils mNONotificationUtils = new NotificationUtils(getApplicationContext());
                mNONotificationUtils.playNotificationSound();

            }else{
                Log.e("Is Back","#####");
                Intent mIntent = new Intent(getApplicationContext(), ChatActivity.class);
                mIntent.putExtra("message", message);

                if (TextUtils.isEmpty(imageUrl)){
                    showNotificationMessage(getApplicationContext(), title, message, timeStamp, mIntent);
                }else {

                    showNotificationMessageBigImage(getApplicationContext(),title,message,timeStamp,mIntent,imageUrl);

                }
            }

        }catch (Exception e){

        }

    }

    private void showNotificationMessageBigImage(Context applicationContext, String title, String message, String timeStamp, Intent mIntent, String imageUrl) {

        mNotificationUtils = new NotificationUtils(applicationContext);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        mNotificationUtils.showNotificationMessage(title,message,timeStamp,mIntent,imageUrl);

    }

    private void showNotificationMessage(Context applicationContext, String title, String message, String timeStamp, Intent mIntent) {

        mNotificationUtils = new NotificationUtils(applicationContext);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
        mNotificationUtils.showNotificationMessage(title,message,timeStamp,mIntent);

    }

    private void handleNotification(String message) {

        if (!NotificationUtils.isAppBackground(getApplicationContext())){
            Intent push = new Intent(Config.PUSH_NOTIFICATION);
            push.putExtra("message",message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(push);

            NotificationUtils mNotificationUtils = new NotificationUtils(getApplicationContext());
            mNotificationUtils.playNotificationSound();
        }else {

        }

    }
}
