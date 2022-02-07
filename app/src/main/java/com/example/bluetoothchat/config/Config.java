package com.example.bluetoothchat.config;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.util.Log;
import com.example.bluetoothchat.connection.AcceptThread;
import com.example.bluetoothchat.connection.ConnectThread;
import com.example.bluetoothchat.connection.ReadWriteThread;
import com.example.bluetoothchat.constants.DialogBoxMessage;
import com.example.bluetoothchat.dao.Database;
import com.example.bluetoothchat.utils.CommonUtil;

public class Config {

    private static BluetoothAdapter bluetoothAdapter;
    private static ConnectThread connectThread;
    private static AcceptThread acceptThread;
    private static ReadWriteThread readWriteThread;
    public static BluetoothSocket socket;

    private static Database database;
    private static String deviceName;

    private static Activity currentActivity;

    public static Activity getCurrentActivity() {
        return currentActivity;
    }

    public static void setCurrentActivity(Activity currentActivity) {
        Config.currentActivity = currentActivity;
    }

    public static BluetoothAdapter getBluetoothAdapter() {
        try {
            if (bluetoothAdapter == null) {
                BluetoothAdapter bt = BluetoothAdapter.getDefaultAdapter();
                bluetoothAdapter = bt;
            }
        } catch (Exception e) {
            Log.e(TAG, "Error occurred while getting Bluetooth adapter");
            CommonUtil.callExitFromLooper(DialogBoxMessage.EXIT_APP);
        }

        return bluetoothAdapter;
    }

    public static void setBluetoothAdapterAsNull() {
        bluetoothAdapter = null;
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

    public static String getDeviceName() {
        return deviceName;
    }

    public static void setDeviceName(String deviceName) {
        Config.deviceName = deviceName;
    }

    public static Database getDatabaseObject(Context context) {
        if (database == null) {
            database = new Database(context);
        }
        return database;
    }
}
