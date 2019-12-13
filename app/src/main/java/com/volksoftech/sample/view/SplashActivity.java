package com.volksoftech.sample.view;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.volksoftech.sample.MenuActivity;
import com.volksoftech.sample.R;

import java.util.List;

public class SplashActivity extends AppCompatActivity {
    TextView txtBottomMsg;
    PermissionListener permissionlistener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            Intent mainIntent = new Intent(SplashActivity.this, MenuActivity.class);
            startActivity(mainIntent);
            finish();
        }

        @Override
        public void onPermissionDenied(List<String> deniedPermissions) {
            Toast.makeText(SplashActivity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
        }


    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        hideStatusBar();
        scheduleSplashScreen();
        txtBottomMsg = findViewById(R.id.bottom_text);
        txtBottomMsg.setText((new StringBuilder().append("Made with ").append(new String(Character.toChars(0x2764))).append(" by ").append(getResources().getString(R.string.dev_name))));
    }

    private void scheduleSplashScreen() {
        new Handler().postDelayed(() -> {
            /* Create an Intent that will start the SplashActivity. */
            TedPermission.with(SplashActivity.this)
                    .setPermissionListener(permissionlistener)
                    .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                    .setPermissions(Manifest.permission.READ_CONTACTS,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.CAMERA,
                            Manifest.permission.READ_PHONE_STATE,
                            Manifest.permission.ACCESS_NETWORK_STATE)
                    .check();
        }, 6000);
    }

    private void hideStatusBar() {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

    }
}
