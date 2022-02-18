package com.example.bluetoothchat.btactivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.bluetoothchat.R;
import com.example.bluetoothchat.config.Config;
import com.example.bluetoothchat.constants.AppConstants;
import com.example.bluetoothchat.constants.DialogBoxMessage;
import com.example.bluetoothchat.constants.ToastMessage;
import com.example.bluetoothchat.utils.CommonUtil;
import com.example.bluetoothchat.utils.DialogBoxUtil;
import java.lang.Thread.UncaughtExceptionHandler;

public class Connect extends AppCompatActivity {

     private static Context context;
     private static Activity activity;
     private Button accept, request;
     private ProgressBar progressBar;

     public static Context getContext() {
          return context;
     }

     public static Activity getActivity() {
          return activity;
     }

     @SuppressLint("MissingPermission")
     @Override
     protected void onCreate(Bundle savedInstanceState) {
          super.onCreate(savedInstanceState);
          setContentView(R.layout.activity_connect);
          getSupportActionBar().setDisplayShowHomeEnabled(true);
          getSupportActionBar().setLogo(R.drawable.ic_twotone_bluetooth_searching_24);
          getSupportActionBar().setDisplayUseLogoEnabled(true);

          Config.setCurrentActivity(Connect.this);
          initialise();
          checkIfBluetoothEnabled();

          accept.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.setAlpha(1f);
                    accept.setClickable(false);
                    request.setClickable(false);
                    accept.setAlpha(.25f);

                    Config.setAcceptThread().start();
                    if (CommonUtil.isBluetoothEnabled()) {
                         Toast.makeText(view.getContext(),
                           ToastMessage.WAITING_FOR_DEVICES.getMessage(),
                           Toast.LENGTH_LONG).show();
                    }
               }
          });

          request.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                    if (!CommonUtil.isBluetoothEnabled()) {
                         DialogBoxUtil.exitAppOnError(DialogBoxMessage.UNABLE_TO_REQUEST);
                         return;
                    } else {
                         finish();
                         Intent i = new Intent(getApplicationContext(), DeviceList.class);
                         startActivity(i);
                    }
               }
          });

          Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {
               @Override
               public void uncaughtException(@NonNull Thread thread, @NonNull Throwable throwable) {
                    new Thread() {
                         @Override
                         public void run() {
                              Looper.prepare();
                              Toast.makeText(Config.getCurrentActivity(), AppConstants.EXIT_MSG,
                                Toast.LENGTH_SHORT).show();
                              Looper.loop();
                         }
                    }.start();
                    try {
                         Thread.sleep(2000); // Let the Toast display before app will get shutdown
                    } catch (InterruptedException e) {
                    }

                    System.exit(2);
               }
          });

     }

     @Override
     public void onBackPressed() {
          DialogBoxUtil.confirmAppExit(getActivity());
     }

     private void initialise() {
          context = getContext();
          accept = findViewById(R.id.accept);
          request = findViewById(R.id.request);
          progressBar = findViewById(R.id.progress);
          activity = this;
     }

     private void checkIfBluetoothEnabled() {
          if (!CommonUtil.isBluetoothEnabled()) {
               overridePendingTransition(0, 0);
               DialogBoxUtil.confirmBluetoothEnable();
          }
     }

}