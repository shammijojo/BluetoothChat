package com.example.bluetoothchat.config;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import com.example.bluetoothchat.connection.AcceptThread;
import com.example.bluetoothchat.connection.ConnectThread;
import com.example.bluetoothchat.connection.ReadWriteThread;

import java.util.UUID;

public class Config {

    static BluetoothAdapter bluetoothAdapter;
    static ConnectThread connectThread;
    static AcceptThread acceptThread;
    static ReadWriteThread readWriteThread;

    public static final UUID MY_UUID = UUID.fromString("00000000-0000-1000-8000-00805F9B34FB");
    public static BluetoothSocket socket;


    public static BluetoothAdapter getBluetoothAdapter(){
        try{
            if(bluetoothAdapter==null){
                BluetoothAdapter bt=BluetoothAdapter.getDefaultAdapter();
                bluetoothAdapter=bt;
            }
        }
        catch (Exception e){
            System.out.println("error");
        }

        return bluetoothAdapter;
    }

    public static ConnectThread getConnectThread(BluetoothDevice bluetoothDevice){
        if(connectThread==null){
            connectThread=new ConnectThread(bluetoothDevice);
        }
        return connectThread;
    }

    public static ConnectThread getConnectThread(){
        return connectThread;
    }

    public static AcceptThread getAcceptThread(){
        if(acceptThread==null){
            acceptThread=new AcceptThread();
        }
        return acceptThread;
    }

    public static ReadWriteThread getReadWriteThread(BluetoothSocket bluetoothSocket){
        if(readWriteThread==null){
            readWriteThread=new ReadWriteThread(bluetoothSocket);
        }
        return readWriteThread;
    }

}
