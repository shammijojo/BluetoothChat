package com.example.bluetoothchat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bluetoothchat.config.Config;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    List<BluetoothDevice> list = new ArrayList<>();
    ListView listView;
    ProgressBar progressBar;
    static Context context;
    static Activity activity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar=findViewById(R.id.progressInDevice);
        context =getApplicationContext();
        activity=MainActivity.this;
        context=getApplicationContext();

        scanDevice();

        DeviceList adapter = new DeviceList(MainActivity.this, list);
        listView = findViewById(R.id.deviceList);
        listView.setAdapter(adapter);

//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//
//                BluetoothDevice bluetoothDevice = (BluetoothDevice) listView.getItemAtPosition(i);
//                Config.getConnectThread(bluetoothDevice).start();
//
//                if (android.os.Build.VERSION.SDK_INT > 25) {
//                    Toast.makeText(view.getContext(), "Connecting...", Toast.LENGTH_SHORT).show();
//                }
//                    progressBar.setVisibility(View.VISIBLE);
//
//
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//
//                        int count=0;
//                        while(!Config.getConnectThread(bluetoothDevice).isConnected()){
//                            try {
//                                Thread.sleep(3000);
//                                count++;
//                                if(count==3){
//                                    Config.getConnectThread().interrupt();
//                                    Config.setConnectThreadAsNull();
//
//                                    runOnUiThread(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            if (android.os.Build.VERSION.SDK_INT > 25)
//                                            Toast.makeText(view.getContext(), "Unable to Connect. Try again later", Toast.LENGTH_SHORT).show();
//                                          //  progressBar.setVisibility(View.INVISIBLE);
//
//                                            Intent i = new Intent(MainActivity.this, MainActivity.class);
//                                            finish();
//                                            overridePendingTransition(0, 0);
//                                            startActivity(i);
//                                            overridePendingTransition(0, 0);
//
//                                        }
//                                    });
//
//                                    return;
//                                }
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
//                        }
//
//                        Toast.makeText(view.getContext(), "Connected Successfully", Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(getApplicationContext(), ChatWindow.class);
//                        startActivity(intent);
//
//
//                        runOnUiThread(new Runnable() {
//                            public void run() {
//                                if (android.os.Build.VERSION.SDK_INT > 25)
//                                progressBar.setVisibility(View.VISIBLE);
//
//                                view.setAlpha(.1f);
//                            }
//                        });
//                    }
//                }).start();
//
//            }
//
//        });




    }

    final BroadcastReceiver discoveryFinishReceiver = new BroadcastReceiver() {
        @SuppressLint("MissingPermission")
        @Override
        public void onReceive(Context context, Intent intent) {
            BluetoothAdapter bt = Config.getBluetoothAdapter();
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
        //Toast.makeText(this.getApplicationContext(), "start Scanning", Toast.LENGTH_SHORT).show();
        bluetoothAdapter.startDiscovery();

      //  Toast.makeText(this.getApplicationContext(), "Scanning", Toast.LENGTH_SHORT).show();

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

    public static Context getContext(){
        return context;
    }

    public static Activity getActivity(){
        return activity;
    }



}