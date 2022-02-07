package com.example.bluetoothchat.connection;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;
import com.example.bluetoothchat.btactivity.DeviceList;
import com.example.bluetoothchat.config.Config;
import com.example.bluetoothchat.constants.AppConstants;
import com.example.bluetoothchat.constants.DialogBoxMessage;
import com.example.bluetoothchat.utils.CommonUtil;
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
               tmp = device.createRfcommSocketToServiceRecord(AppConstants.APP_UUID);
          } catch (IOException e) {
               Log.e(TAG, "Error occurred while creating connect thread");
               CommonUtil.callExitFromLooper(DialogBoxMessage.UNABLE_TO_REQUEST);
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
                    Log.e(TAG, "Error occurred while starting accept thread");
                    if (socket != null) {
                         socket.close();
                    }
               } catch (IOException e2) {
                    Log.e(TAG, "Error occurred while closing socket");
               }
               connectionFailed();
               return;
          }

          // Start the connected thread
          connected(socket);
     }

     private void connectionFailed() {
          Log.e(TAG, "Connection failed");
          CommonUtil.callExitFromLooper(DialogBoxMessage.UNABLE_TO_REQUEST);
     }

     private void connected(BluetoothSocket socket) {
          Config.socket = socket;
          Config.setReadWriteThread(socket).start();
          Config.getDatabaseObject(DeviceList.getContext()).createTables();
     }

     public boolean isConnected() {
          return connected;
     }

}