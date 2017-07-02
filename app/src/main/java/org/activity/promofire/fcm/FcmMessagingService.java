package org.activity.promofire.fcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

import org.activity.promofire.ActivityDialogNotification;
import org.activity.promofire.R;
import org.activity.promofire.data.Constant;
import org.activity.promofire.data.DatabaseHandler;
import org.activity.promofire.data.SharedPref;
import org.activity.promofire.entity.Notification;
import org.activity.promofire.utils.CallbackImageNotif;

import java.util.Map;

public class FcmMessagingService extends FirebaseMessagingService {

    private static int VIBRATION_TIME = 500; // in millisecond
    private SharedPref sharedPref;
    private DatabaseHandler db;
    private int retry_count = 0;
    private String string;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        sharedPref = new SharedPref(this);
        db = new DatabaseHandler(this);
        retry_count = 0;
        if (sharedPref.getNotification()) {
            if (remoteMessage.getData().size() <= 0) return;
            //Object obj = remoteMessage.getData();
            Map<String, String> data = remoteMessage.getData();
            String myCustomKey = data.get("notification");
            Gson gson = new GsonBuilder()
                    .disableHtmlEscaping()
                    .create();
            //String data = gson.toJson(myCustomKey);
            //Notification notification = new Gson().fromJson(data, Notification.class);
            JsonElement json = gson.fromJson(myCustomKey, JsonElement.class);
           // String dataAux = data.replace("\\", "");
            Notification notification = gson.fromJson(myCustomKey, Notification.class);
            notification.id = System.currentTimeMillis();
            notification.created_at = System.currentTimeMillis();
            notification.read = false;

            // display notification
            prepareImageNotification(notification);

            // save notification to relam db
            saveNotification(notification);
        }
    }

    private void prepareImageNotification(final Notification notif) {
        String image_url = null;
        if (notif.type.equals("PRODUCT")) {
            //image_url = Constant.getURLimgProduct(notif.image);
            image_url = notif.image;
        } else if (notif.type.equals("NEWS_INFO")) {
           // image_url = Constant.getURLimgNews(notif.image);
            image_url = notif.image;
        }
        if (image_url != null) {
            glideLoadImageFromUrl(this, image_url, new CallbackImageNotif() {
                @Override
                public void onSuccess(Bitmap bitmap) {
                    showNotification(notif, bitmap);
                }

                @Override
                public void onFailed(String string) {
                    Log.e("onFailed", string);
                    if (retry_count <= Constant.LOAD_IMAGE_NOTIF_RETRY) {
                        retry_count++;
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                prepareImageNotification(notif);
                            }
                        }, 1000);
                    } else {
                        showNotification(notif, null);
                    }
                }
            });
        } else {
            showNotification(notif, null);
        }
    }

    private void showNotification(Notification notif, Bitmap bitmap) {
        Intent intent = ActivityDialogNotification.navigateBase(this, notif, true);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentTitle(notif.title);
        builder.setContentText(notif.content);
        builder.setSmallIcon(R.drawable.ic_notification);
        builder.setDefaults(android.app.Notification.DEFAULT_LIGHTS);
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            builder.setPriority(android.app.Notification.PRIORITY_HIGH);
        }
        builder.setStyle(new NotificationCompat.BigTextStyle().bigText(notif.content));
        if (bitmap != null) {
            builder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bitmap).setSummaryText(notif.content));
        }

        // display push notif
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        int unique_id = (int) System.currentTimeMillis();
        notificationManager.notify(unique_id, builder.build());

        vibrationAndPlaySound();
    }

    private void vibrationAndPlaySound() {
        // play vibration
        if (sharedPref.getVibration()) {
            ((Vibrator) getSystemService(Context.VIBRATOR_SERVICE)).vibrate(VIBRATION_TIME);
        }
        // play tone
        RingtoneManager.getRingtone(this, Uri.parse(sharedPref.getRingtone())).play();
    }


    // load image with callback
    private void glideLoadImageFromUrl(final Context ctx, final String url, final CallbackImageNotif callback) {
        Handler mainHandler = new Handler(Looper.getMainLooper());
        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                Glide.with(ctx).load(url).asBitmap().into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        callback.onSuccess(resource);
                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        callback.onFailed(e.getMessage());
                        super.onLoadFailed(e, errorDrawable);
                    }
                });
            }
        };
        mainHandler.post(myRunnable);
    }

    private void saveNotification(Notification notification) {
        db.saveNotification(notification);
    }

}
