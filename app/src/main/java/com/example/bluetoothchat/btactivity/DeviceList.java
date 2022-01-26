package com.example.bluetoothchat.btactivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bluetoothchat.R;
import com.example.bluetoothchat.adapter.DeviceListAdapter;
import com.example.bluetoothchat.config.Config;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class DeviceList extends AppCompatActivity {

    private List<BluetoothDevice> list = new ArrayList<>();
    private ListView listView;
    private ProgressBar progressBar;
    private static Context context;
    private static Activity activity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_twotone_bluetooth_searching_24);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        setContentView(R.layout.activity_main);
        initialise();
        scanDevice();
    }



    @SuppressLint("MissingPermission")
    private void scanDevice() {
        BluetoothAdapter bluetoothAdapter = Config.getBluetoothAdapter();
        bluetoothAdapter = Config.getBluetoothAdapter();
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();

        // If there are paired devices, add each one to the ArrayAdapter
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                list.add(device);
            }
        } else {
            list.add(null);
        }

    }

    private void initialise(){
        progressBar = findViewById(R.id.progressInDevice);
        context = getApplicationContext();
        activity = DeviceList.this;
        context = getApplicationContext();

        DeviceListAdapter adapter = new DeviceListAdapter(DeviceList.this, list);
        listView = findViewById(R.id.deviceList);
        listView.setAdapter(adapter);
    }

    public static Context getContext() {
        return context;
    }

    public static Activity getActivity() {
        return activity;
    }


}