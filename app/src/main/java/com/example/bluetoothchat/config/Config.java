package com.example.bluetoothchat.config;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import com.example.bluetoothchat.connection.AcceptThread;
import com.example.bluetoothchat.connection.ConnectThread;
import com.example.bluetoothchat.connection.ReadWriteThread;

import java.util.UUID;

public class Config {

    private static BluetoothAdapter bluetoothAdapter;
    private static ConnectThread connectThread;
    private static AcceptThread acceptThread;
    private static ReadWriteThread readWriteThread;

    public static final UUID MY_UUID = UUID.fromString("00000000-0000-1000-8000-00805F9B34FB");
    public static final int CONNECT_COUNT=3;
    public static BluetoothSocket socket;


    public static BluetoothAdapter getBluetoothAdapter() {
        try {
            if (bluetoothAdapter == null) {
                BluetoothAdapter bt = BluetoothAdapter.getDefaultAdapter();
                bluetoothAdapter = bt;
            }
        } catch (Exception e) {
            System.out.println("error");
        }

        return bluetoothAdapter;
    }

    public static ConnectThread getConnectThread(BluetoothDevice bluetoothDevice) {
        if (connectThread == null) {
            connectThread = new ConnectThread(bluetoothDevice);
        }
        return connectThread;
    }

    public static ConnectThread getConnectThread() {
        return connectThread;
    }

    public static void setConnectThreadAsNull() {
        if (connectThread != null && connectThread.isAlive()) {
            connectThread.interrupt();
        }
        connectThread = null;
    }


    public static AcceptThread setAcceptThread() {
        if (acceptThread == null) {
            acceptThread = new AcceptThread();
        }
        return acceptThread;
    }

    public static void setAcceptThreadAsNull() {
        if (acceptThread != null && acceptThread.isAlive()) {
            acceptThread.interrupt();
        }
        acceptThread = null;
    }

    public static AcceptThread getAcceptThread() {
        return acceptThread;
    }


    public static ReadWriteThread setReadWriteThread(BluetoothSocket bluetoothSocket) {
        if (readWriteThread == null) {
            readWriteThread = new ReadWriteThread(bluetoothSocket);
        }
        return readWriteThread;
    }

    public static ReadWriteThread getReadWriteThread() {
        return readWriteThread;
    }

    public static void setReadWriteThreadAsNull() {
        if (readWriteThread != null && readWriteThread.isAlive()) {
            readWriteThread.interrupt();
        }
        readWriteThread = null;
    }


}
