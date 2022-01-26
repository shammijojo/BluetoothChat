package com.example.bluetoothchat.connection;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import com.example.bluetoothchat.btactivity.DeviceList;
import com.example.bluetoothchat.config.Config;

import java.io.IOException;

public class ConnectThread extends Thread {
    private final BluetoothSocket socket;
    private final BluetoothDevice device;
    private boolean connected;

    @SuppressLint("MissingPermission")
    public ConnectThread(BluetoothDevice device) {
        this.device = device;
        BluetoothSocket tmp = null;
        try {
            tmp = device.createRfcommSocketToServiceRecord(Config.MY_UUID);
        } catch (IOException e) {
            e.printStackTrace();
        }
        socket = tmp;
    }

    public BluetoothSocket getSocket() {
        return socket;
    }

    @SuppressLint("MissingPermission")
    public void run() {
        setName("ConnectThread");

        // Always cancel discovery because it will slow down a connection
        Config.getBluetoothAdapter().cancelDiscovery();

        // Make a connection to the BluetoothSocket
        try {
            socket.connect();
            connected = true;
        } catch (IOException e) {
            try {
                System.out.println(e);
                if (socket != null)
                    socket.close();
            } catch (IOException e2) {
                System.out.println(e2);
            }
            connectionFailed();
            return;
        }

        // Reset the ConnectThread because we're done
        synchronized (this) {
            // connectThread = null;
        }

        // Start the connected thread
        connected(socket, device);
    }

    private void connectionFailed() {
        System.out.println("connection failed");
    }

    private void connected(BluetoothSocket socket, BluetoothDevice device) {
        Config.socket = socket;
        Config.setReadWriteThread(socket).start();
        Config.getDatabaseObject(DeviceList.getContext()).createTables();
    }

    public void cancel() {
        try {
            socket.close();
        } catch (IOException e) {
        }
    }

    public boolean isConnected() {
        return connected;
    }

}