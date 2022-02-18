package com.example.bluetoothchat.btactivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.widget.ListView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.bluetoothchat.R;
import com.example.bluetoothchat.adapter.DeviceListAdapter;
import com.example.bluetoothchat.config.Config;
import com.example.bluetoothchat.constants.AppConstants;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class DeviceList extends AppCompatActivity {

     private static Context context;
     private static Activity activity;
     private final List<BluetoothDevice> list = new ArrayList<>();
     private ListView listView;

     public static Context getContext() {
          return context;
     }

     public static Activity getActivity() {
          return activity;
     }

     @Override
     protected void onCreate(Bundle savedInstanceState) {
          super.onCreate(savedInstanceState);
          Config.setCurrentActivity(this);
          getSupportActionBar().setDisplayShowHomeEnabled(true);
          getSupportActionBar().setLogo(R.drawable.ic_twotone_bluetooth_searching_24);
          getSupportActionBar().setDisplayUseLogoEnabled(true);
          setContentView(R.layout.activity_main);
          initialise();
          scanDevice();

          Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {
               @Override
               public void uncaughtException(@NonNull Thread thread, @NonNull Throwable throwable) {
                    new Thread() {
                         @Override
                         public void run() {
                              Looper.prepare();
                              Toast
                                .makeText(Config.getCurrentActivity(), AppConstants.EXIT_MSG,
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
          finish();
          Intent i = new Intent(getApplicationContext(), Connect.class);
          startActivity(i);
     }

     @SuppressLint("MissingPermission")
     private void scanDevice() {
          BluetoothAdapter bluetoothAdapter = Config.getBluetoothAdapter();
          Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();

          if (pairedDevices.size() > 0) {
               for (BluetoothDevice device : pairedDevices) {
                    list.add(device);
               }
          } else {
               list.add(null);
          }

     }

     private void initialise() {
          context = getApplicationContext();
          activity = this;

          DeviceListAdapter adapter = new DeviceListAdapter(DeviceList.this, list);
          listView = findViewById(R.id.deviceList);
          listView.setAdapter(adapter);
     }

     @Override
     protected void onDestroy() {
          super.onDestroy();
          context = null;
          activity = null;
     }


}