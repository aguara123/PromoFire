package org.activity.promofire;

import android.app.Application;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import org.activity.promofire.base.ImageLoader;
import org.activity.promofire.connection.API;
import org.activity.promofire.connection.RestAdapter;
import org.activity.promofire.connection.callbacks.CallbackDevice;
import org.activity.promofire.data.SharedPref;
import org.activity.promofire.entity.DeviceInfo;
import org.activity.promofire.lib.GlideImageLoader;
import org.activity.promofire.utils.NetworkCheck;
import org.activity.promofire.utils.Tools;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by DESARROLLO on 07/01/17.
 */

public class PromoFireApp extends Application {

    private static PromoFireApp mInstance;
    private SharedPref sharedPref;
    private FirebaseAnalytics mFirebaseAnalytics;
    private ImageLoader imageLoader;

    @Override
    public void onCreate() {
        super.onCreate();
        setupImageLoader();
        mInstance = this;
        sharedPref = new SharedPref(this);

        // registering device to server
        sendRegistrationToServer();

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

    }

    public static synchronized PromoFireApp getInstance() {
        return mInstance;
    }

    private void sendRegistrationToServer() {
        //FirebaseApp.initializeApp(this);
        FirebaseMessaging.getInstance().subscribeToTopic("productos");
        String token = "";
        for (int i = 0; i < 20; i++) {
            token = FirebaseInstanceId.getInstance().getToken();
            if (!TextUtils.isEmpty(token)) break;
        }
        Log.d("FCM_TOKEN", token + "");

        sharedPref.setTokenFirebase(token);

        if (NetworkCheck.isConnect(this) && !TextUtils.isEmpty(token) && sharedPref.isOpenAppCounterReach()) {
            DeviceInfo deviceInfo = new DeviceInfo();
            deviceInfo.device = Tools.getDeviceName();
            deviceInfo.os_version = Tools.getAndroidVersion();
            deviceInfo.app_version = Tools.getVersionCode(this) + " (" + Tools.getVersionNamePlain(this) + ")";
            deviceInfo.serial = Build.SERIAL;
            deviceInfo.regid = token;

            API api = RestAdapter.createAPI();
            Call<CallbackDevice> callbackCall = api.registerDevice(deviceInfo);
            callbackCall.enqueue(new Callback<CallbackDevice>() {
                @Override
                public void onResponse(Call<CallbackDevice> call, Response<CallbackDevice> response) {
                    CallbackDevice resp = response.body();
                    if (resp != null && resp.status.equals("success")) {
                        sharedPref.setOpenAppCounter(0);
                    }
                }

                @Override
                public void onFailure(Call<CallbackDevice> call, Throwable t) {
                    Log.e("onFailure", t.getMessage());
                }
            });
        }
    }

    public void saveLogEvent(long id, String name, String type) {
        Bundle bundle = new Bundle();
        bundle.putLong(FirebaseAnalytics.Param.ITEM_ID, id);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, name);
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, type);
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }

    public void saveCustomLogEvent(String event, String key, String value) {
        Bundle params = new Bundle();
        params.putString(key, value);
        mFirebaseAnalytics.logEvent(event, params);
    }

    private void setupImageLoader() {
        imageLoader = new GlideImageLoader(this);
    }

    public ImageLoader getImageLoader() {
        return imageLoader;
    }

    private void setupFirebase() {

    }


}
