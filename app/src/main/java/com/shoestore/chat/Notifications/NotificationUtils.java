package com.shoestore.chat.Notifications;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.NotificationCompat;
import android.text.Html;
import android.text.TextUtils;
import android.util.Patterns;

import com.shoestore.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Daniel on 08/04/2017.
 */

public class NotificationUtils {

    private static String TAG = NotificationUtils.class.getSimpleName();

    private Context mContext;

    public  NotificationUtils(Context mContext){
        this.mContext=mContext;
    }

    public void showNotificationMessage(String title, String message, String timeStamp, Intent mIntent){
        showNotificationMessage(title,message,timeStamp,mIntent,null);
    }

    public void showNotificationMessage(String title, String message, String timeStamp, Intent mIntent, String imageUrl) {
        if (TextUtils.isEmpty(message)){return;}

        final int icon = R.mipmap.sandals;

        mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        final PendingIntent mPendingIntent = PendingIntent.getActivity(
                mContext,
                0,
                mIntent,
                PendingIntent.FLAG_CANCEL_CURRENT
        );

        final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mContext);

        final Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +"://"+ mContext.getPackageName() + "/raw/demonstrative");

        if (!TextUtils.isEmpty(imageUrl)){
            if (imageUrl != null && imageUrl.length()>4 && Patterns.WEB_URL.matcher(imageUrl).matches()){
                Bitmap mBitmap = getBitmapFromUrl(imageUrl);
                if (mBitmap!=null){
                    showBigNotification(mBitmap,mBuilder,icon,title,message,timeStamp,mPendingIntent,alarmSound);
                } else {
                    showSmallNotification(mBuilder,icon, title,message,timeStamp,mPendingIntent,alarmSound);
                }
            }
        }else{
            showSmallNotification(mBuilder,icon,title,message,timeStamp,mPendingIntent,alarmSound);
            playNotificationSound();
        }
    }

    public void playNotificationSound() {

            try {
                Uri alarmSound =Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                    + "://" + mContext.getPackageName() + "/raw/demonstrative");

                Ringtone r = RingtoneManager.getRingtone(mContext,alarmSound);
                r.play();
            }catch (Exception e){
                e.printStackTrace();
            }
    }

    private void showSmallNotification(NotificationCompat.Builder mBuilder, int icon, String title, String timeStamp, String message, PendingIntent mPendingIntent, Uri alarmSound) {

        NotificationCompat.InboxStyle mInboxStyle =new NotificationCompat.InboxStyle();
        mInboxStyle.addLine(message);

        Notification mNotification;

        mNotification= mBuilder.setSmallIcon(icon).setTicker(title).setWhen(0)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentIntent(mPendingIntent)
                .setSound(alarmSound)
                .setStyle(mInboxStyle)
                .setWhen(getTimeMilliSec(timeStamp))
                .setSmallIcon(R.mipmap.sandals)
                .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(),icon))
                .setContentText(message)
                .build();

        NotificationManager mNONotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        mNONotificationManager.notify(Config.NOTIFICATION_ID,mNotification);

    }

    private void showBigNotification(Bitmap mBitmap, NotificationCompat.Builder mBuilder, int icon, String title, String message, String timeStamp, PendingIntent mPendingIntent, Uri alarmSound) {

        NotificationCompat.BigPictureStyle mBigPictureStyle = new NotificationCompat.BigPictureStyle();
        mBigPictureStyle.setBigContentTitle(title);
        mBigPictureStyle.setSummaryText(Html.fromHtml(message).toString());
        mBigPictureStyle.bigPicture(mBitmap);

        Notification notification;
        notification = mBuilder.setSmallIcon(icon).setTicker(title).setWhen(0)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentIntent(mPendingIntent)
                .setSound(alarmSound)
                .setStyle(mBigPictureStyle)
                .setWhen(getTimeMilliSec(timeStamp))
                .setSmallIcon(R.mipmap.sandals)
                .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(),icon))
                .setContentText(message)
                .build();

        NotificationManager mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(Config.NOTIFICATION_ID_BIG_IMAGE, notification);
    }

    public static long getTimeMilliSec(String timeStamp) {
        SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date mDate = mFormat.parse(timeStamp);
            return  mDate.getTime();
        }catch (ParseException e){

        }
        return 0;
    }

    private Bitmap getBitmapFromUrl(String imageUrl) {

        try {
            URL mUrl = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) mUrl.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream mInputStream = connection.getInputStream();
            Bitmap mBitmap= BitmapFactory.decodeStream(mInputStream);
            return mBitmap;
        }catch (IOException e){
            e.printStackTrace();
            return null;
        }

    }

    public static  boolean isAppBackground(Context mContext){
        boolean isBackground=true;
        ActivityManager mActivityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH){
                List<ActivityManager.RunningAppProcessInfo> mRunningAppProcessInfos = mActivityManager.getRunningAppProcesses();
                    for (ActivityManager.RunningAppProcessInfo mProcessInfo : mRunningAppProcessInfos){
                        if (mProcessInfo.importance ==ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND){
                            for (String mActiveProcess :mProcessInfo.pkgList){
                                if (mActiveProcess.equals(mContext.getPackageName())){

                                    isBackground=false;
                                }
                            }
                        }
                    }
            }else {
                List<ActivityManager.RunningTaskInfo> mTaskInfo = mActivityManager.getRunningTasks(1);
                ComponentName mComponentName = mTaskInfo.get(0).topActivity;
                if (mComponentName.getPackageName().equals(mContext.getPackageName())){
                    isBackground =false;
                }
            }
        return isBackground;
    }

    public static void clearNotifications(Context mContext){
        NotificationManager mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.cancelAll();
    }

}
