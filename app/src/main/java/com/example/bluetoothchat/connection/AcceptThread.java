package com.example.bluetoothchat.connection;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.util.Log;
import com.example.bluetoothchat.btactivity.ChatWindow;
import com.example.bluetoothchat.btactivity.Connect;
import com.example.bluetoothchat.config.Config;
import com.example.bluetoothchat.constants.AppConstants;
import com.example.bluetoothchat.constants.DialogBoxMessage;
import com.example.bluetoothchat.utils.CommonUtil;
import java.io.IOException;

public class AcceptThread extends Thread {

     private final BluetoothServerSocket serverSocket;

     @SuppressLint("MissingPermission")
     public AcceptThread() {
          BluetoothServerSocket tmp = null;
          try {
               if (CommonUtil.isBluetoothEnabled()) {
                    tmp = Config.getBluetoothAdapter()
                      .listenUsingRfcommWithServiceRecord("BTchat", AppConstants.APP_UUID);
               } else {
                    tmp = null;
               }

          } catch (IOException ex) {
               Log.e(TAG, "Error occurred while creating accept thread");
               CommonUtil.callExitFromLooper(DialogBoxMessage.UNABLE_TO_ACCEPT);
          }
          serverSocket = tmp;
     }


     public void run() {
          BluetoothSocket socket = null;
          // Keep listening until exception occurs or a socket is returned.
          while (true) {
               try {
                    socket = serverSocket.accept(10000);
               } catch (Exception e) {
                    Log.e(TAG, "Socket's accept() method failed", e);
                    CommonUtil.callExitFromLooper(DialogBoxMessage.UNABLE_TO_ACCEPT);
                    try {
                         if (serverSocket != null) {
                              serverSocket.close();
                         }
                    } catch (IOException ioException) {
                         CommonUtil.callExitFromLooper(DialogBoxMessage.UNABLE_TO_ACCEPT);
                    }
                    return;
               }

               if (socket != null) {
                    // A connection was accepted. Perform work associated with
                    // the connection in a separate thread.
                    connected(socket);
                    try {
                         serverSocket.close();
                    } catch (IOException e) {
                         Log.e(TAG, "Error occurred while closing server socket");
                         CommonUtil.callExitFromLooper(DialogBoxMessage.UNABLE_TO_ACCEPT);
                    }
                    break;
               }
          }
     }

     private void connected(BluetoothSocket socket) {
          Config.socket = socket;
          Config.setReadWriteThread(socket).start();
          Connect.getActivity().finish();
          Intent i = new Intent(Connect.getActivity(), ChatWindow.class);
          i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
          Connect.getActivity().startActivity(i);
     }

}
