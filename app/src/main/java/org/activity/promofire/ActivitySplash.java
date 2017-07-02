package org.activity.promofire;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.activity.promofire.connection.API;
import org.activity.promofire.connection.RestAdapter;
import org.activity.promofire.connection.callbacks.CallBackVersion;
import org.activity.promofire.data.SharedPref;
import org.activity.promofire.entity.AppVersion;
import org.activity.promofire.entity.Info;
import org.activity.promofire.utils.CallbackDialog;
import org.activity.promofire.utils.DialogUtils;
import org.activity.promofire.utils.NetworkCheck;
import org.activity.promofire.utils.PermissionUtil;
import org.activity.promofire.utils.Tools;

import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivitySplash extends AppCompatActivity {

    private SharedPref sharedPref;
    private boolean on_permission_result = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        sharedPref = new SharedPref(this);
        sharedPref.clearInfoData();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // permission checker for android M or higher
        if (Tools.needRequestPermission() && !on_permission_result) {
            String[] permission = PermissionUtil.getDeniedPermission(this);
            if (permission.length != 0) {
                permisos(permission);
            } else {
                startProcess();
            }
        } else {
            startProcess();
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void permisos(String[] permission) {
        requestPermissions(permission, 200);

    }

    private void startProcess() {
        if (!NetworkCheck.isConnect(this)) {
            dialogNoInternet();
        } else {
            requestInfo();
        }
    }

    private void startActivityMainDelay() {
        // Show splash screen for 2 seconds
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Intent i = new Intent(ActivitySplash.this, ActivityMain.class);
                startActivity(i);
                finish(); // kill current activity
            }
        };
        new Timer().schedule(task, 4000);
    }

    private void requestInfo() {
        API api = RestAdapter.createAPI();
        Call<CallBackVersion> callbackCall = api.getVersion(Tools.getVersionCode(this));
        callbackCall.enqueue(new Callback<CallBackVersion>() {
            @Override
            public void onResponse(Call<CallBackVersion> call, Response<CallBackVersion> response) {
                CallBackVersion resp = response.body();
                if (resp != null && resp.status.equals("success") && resp.appVersion != null) {
                    AppVersion info = sharedPref.setAppVersionData(resp.appVersion);
                    checkAppVersion(info);
                } else {
                    dialogServerNotConnect();
                }
            }

            @Override
            public void onFailure(Call<CallBackVersion> call, Throwable t) {
                Log.e("onFailure", t.getMessage());
                dialogServerNotConnect();
            }
        });
    }

    private void checkAppVersion(Info info) {
        if (!info.active) {
            dialogOutDate();
        } else {
            startActivityMainDelay();
        }
    }

    private void checkAppVersion(AppVersion info) {
        if (info.activo != 1) {
            dialogOutDate();
        } else {
            startActivityMainDelay();
        }
    }

    public void dialogServerNotConnect() {
        Dialog dialog = new DialogUtils(this).buildDialogWarning(R.string.title_unable_connect, R.string.msg_unable_connect, R.string.TRY_AGAIN, R.string.CLOSE, R.drawable.img_no_connect, new CallbackDialog() {
            @Override
            public void onPositiveClick(Dialog dialog) {
                dialog.dismiss();
                retryOpenApplication();
            }

            @Override
            public void onNegativeClick(Dialog dialog) {
                finish();
            }
        });
        dialog.show();
    }

    public void dialogOutDate() {
        Dialog dialog = new DialogUtils(this).buildDialogInfo(R.string.title_info, R.string.msg_app_out_date, R.string.UPDATE, R.drawable.img_app_outdate, new CallbackDialog() {
            @Override
            public void onPositiveClick(Dialog dialog) {
                dialog.dismiss();
                Tools.rateAction(ActivitySplash.this);
            }

            @Override
            public void onNegativeClick(Dialog dialog) {
            }
        });
        dialog.show();
    }

    public void dialogNoInternet() {
        Dialog dialog = new DialogUtils(this).buildDialogWarning(R.string.title_no_internet, R.string.msg_no_internet, R.string.TRY_AGAIN, R.string.CLOSE, R.drawable.img_no_internet, new CallbackDialog() {
            @Override
            public void onPositiveClick(Dialog dialog) {
                dialog.dismiss();
                retryOpenApplication();
            }

            @Override
            public void onNegativeClick(Dialog dialog) {
                finish();
            }
        });
        dialog.show();
    }

    // make a delay to start next activity
    private void retryOpenApplication() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startProcess();
            }
        }, 2000);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 200) {
            for (String perm : permissions) {
                boolean rationale = showRequestPermission(perm);
                sharedPref.setNeverAskAgain(perm, !rationale);
            }
            on_permission_result = true;
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public boolean showRequestPermission(String perm) {
        return shouldShowRequestPermissionRationale(perm);
    }


}
