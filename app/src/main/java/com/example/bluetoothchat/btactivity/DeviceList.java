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
        setContentView(R.layout.activity_main);
        initialise();
        scanDevice();
    }

//    final BroadcastReceiver discoveryFinishReceiver = new BroadcastReceiver() {
//        @SuppressLint("MissingPermission")
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            BluetoothAdapter bt = Config.getBluetoothAdapter();
//            String action = intent.getAction();
//
//            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
//                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
//                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
//                    // list.add(device.getName() + "\n" + device.getAddress());
//                }
//            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
//                if (list.size() == 0) {
//                    //list.add("new item");
//                }
//            }
//        }
//    };


    @SuppressLint("MissingPermission")
    private void scanDevice() {
        BluetoothAdapter bluetoothAdapter = Config.getBluetoothAdapter();
//        if (bluetoothAdapter == null) {
//            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        if (bluetoothAdapter.isDiscovering()) {
//            bluetoothAdapter.cancelDiscovery();
//        }
//        bluetoothAdapter.startDiscovery();
//
//        // Register for broadcasts when a device is discovered
//        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
//        registerReceiver(discoveryFinishReceiver, filter);
//
//        // Register for broadcasts when discovery has finished
//        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
//        registerReceiver(discoveryFinishReceiver, filter);

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