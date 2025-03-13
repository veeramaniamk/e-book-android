package com.saveetha.e_book.openingscreens;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.appupdate.AppUpdateOptions;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.saveetha.e_book.Constant;
import com.saveetha.e_book.MainActivity;
import com.saveetha.e_book.R;
import com.saveetha.e_book.SF;
import com.saveetha.e_book.adminscreens.AdminDashboardActivity;
import com.saveetha.e_book.reviewerscrees.ReviewerDashboardActivity;
import com.saveetha.e_book.userscreens.UserDashboardActivity;

@SuppressLint("CustomSplashScreen")
public class SplashScreenActivity extends AppCompatActivity {

    private static final int SPLASH_TIME_OUT = 1000; // 2 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sf = SF.getSignInSF(SplashScreenActivity.this);
                if (sf.getInt(Constant.USER_TYPE_SI_SF, 0) != 0) {
                    if (sf.getInt(Constant.USER_TYPE_SI_SF, 0) == 111) {
                        startActivity(new Intent(SplashScreenActivity.this, AdminDashboardActivity.class));
                    } else if (sf.getInt(Constant.USER_TYPE_SI_SF, 0) == 110) {
                        startActivity(new Intent(SplashScreenActivity.this, ReviewerDashboardActivity.class));
                    } else if (sf.getInt(Constant.USER_TYPE_SI_SF, 0) == 100) {
                        startActivity(new Intent(SplashScreenActivity.this, UserDashboardActivity.class));
                    }
                } else {
                    Intent intent = new Intent(SplashScreenActivity.this, SignInActivity.class);
                    startActivity(intent);
                }

                finish();
            }
        }, SPLASH_TIME_OUT);
        checkAppUpdate(this);
    }
    private void checkAppUpdate(Context context) {

        ActivityResultLauncher<IntentSenderRequest> activityResultLauncher=registerForActivityResult(
                new ActivityResultContracts.StartIntentSenderForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        // handle callback
                        if (result.getResultCode() != RESULT_OK) {
                            Toast.makeText(context, "Update Failed", Toast.LENGTH_SHORT).show();
                            // If the update is canceled or fails,
                            // you can request to start the update again.
                        } else {
                            Toast.makeText(context, "Updated Successfully", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        AppUpdateManager appUpdateManager = AppUpdateManagerFactory.create(context);

        // Returns an intent object that you use to check for an update.
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

        // Checks that the platform will allow the specified type of update.
        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    // This example applies an immediate update. To apply a flexible update
                    // instead, pass in AppUpdateType.FLEXIBLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {
                // Request the update.
                Toast.makeText(context, "Update Available", Toast.LENGTH_SHORT).show();
                appUpdateManager.startUpdateFlowForResult(
                        // Pass the intent that is returned by 'getAppUpdateInfo()'.
                        appUpdateInfo,
                        // an activity result launcher registered via registerForActivityResult
                        activityResultLauncher,
                        // Or pass 'AppUpdateType.FLEXIBLE' to newBuilder() for
                        // flexible updates.
                        AppUpdateOptions.newBuilder(AppUpdateType.FLEXIBLE).build());
            } else {
                Toast.makeText(context, "No Update Available", Toast.LENGTH_SHORT).show();
            }
        });
        appUpdateInfoTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
                Toast.makeText(context, "update Exception", Toast.LENGTH_SHORT).show();
            }
        });

    }

}