package com.example.bluetoothchat;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.bluetoothchat.ChatWindow;
import com.example.bluetoothchat.config.Config;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    List<BluetoothDevice> list=new ArrayList<>();
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scanDevice();

        DeviceList adapter=new DeviceList(MainActivity.this,list);
        listView=findViewById(R.id.deviceList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                BluetoothDevice bluetoothDevice = (BluetoothDevice) listView.getItemAtPosition(i);
                System.out.println("working");
                Config.getConnectThread(bluetoothDevice).start();
                Intent intent=new Intent(getApplicationContext(), ChatWindow.class);
                startActivity(intent);
            }
        });
    }

    final BroadcastReceiver discoveryFinishReceiver = new BroadcastReceiver() {
        @SuppressLint("MissingPermission")
        @Override
        public void onReceive(Context context, Intent intent) {
            BluetoothAdapter bt=Config.getBluetoothAdapter();
            String action = intent.getAction();

            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                   // list.add(device.getName() + "\n" + device.getAddress());
                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                if (list.size() == 0) {
                    //list.add("new item");
                }
            }
        }
    };



    @SuppressLint("MissingPermission")
    private void scanDevice() {
        System.out.println("hello");
        BluetoothAdapter bluetoothAdapter = Config.getBluetoothAdapter();
        if (bluetoothAdapter == null) {
            System.out.println("finish");
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_SHORT).show();
            finish();
        }


        if (bluetoothAdapter.isDiscovering()) {
            bluetoothAdapter.cancelDiscovery();
        }
        Toast.makeText(this.getApplicationContext(), "start Scanning", Toast.LENGTH_SHORT).show();
        bluetoothAdapter.startDiscovery();

        Toast.makeText(this.getApplicationContext(), "Scanning", Toast.LENGTH_SHORT).show();

        // Register for broadcasts when a device is discovered
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(discoveryFinishReceiver, filter);

        // Register for broadcasts when discovery has finished
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(discoveryFinishReceiver, filter);

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
}