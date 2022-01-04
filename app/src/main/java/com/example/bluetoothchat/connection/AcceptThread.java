package com.example.bluetoothchat.connection;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.util.Log;

import com.example.bluetoothchat.ChatWindow;
import com.example.bluetoothchat.config.Config;

import java.io.IOException;

public class AcceptThread extends Thread {
    private final BluetoothServerSocket serverSocket;

    @SuppressLint("MissingPermission")
    public AcceptThread() {
        BluetoothServerSocket tmp = null;
        try {
            tmp = Config.getBluetoothAdapter().listenUsingRfcommWithServiceRecord("bluetoothchat",Config.MY_UUID);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        serverSocket = tmp;
    }

//    public void run() {
//        setName("AcceptThread");
//        BluetoothSocket socket=null;
//        int state = Config.getBluetoothAdapter().getState();
//        System.out.println(state+" "+STATE_CONNECTED);
//        while (state != STATE_CONNECTED) {
//            System.out.println("is it checking");
//            try {
//                socket = serverSocket.accept(10000);
//                System.out.println("lol");
//            } catch (IOException e) {
//                System.out.println(e);
//                // break;
//            }
//
//            // If a connection was accepted
//            if (socket != null) {
//                synchronized (this) {
//                    switch (state) {
//                        case STATE_CONNECTING:
//                            // start the connected thread.
//                            connected(socket, socket.getRemoteDevice());
//                            break;
//                        case STATE_NONE:
//                        case STATE_CONNECTED:
//                            // Either not ready or already connected. Terminate
//                            // new socket.
//                            try {
//                                socket.close();
//                            } catch (IOException e) {
//                            }
//                            break;
//                    }
//                }
//            }
//        }
//    }



    public void run() {
        BluetoothSocket socket = null;
        // Keep listening until exception occurs or a socket is returned.
        while (true) {
            try {
                socket = serverSocket.accept();
            } catch (IOException e) {
               Log.e(TAG, "Socket's accept() method failed", e);
                break;
            }

            if (socket != null) {
                // A connection was accepted. Perform work associated with
                // the connection in a separate thread.
                connected(socket, socket.getRemoteDevice());
               // mmServerSocket.close();
                break;
            }
        }
    }

    private void connected(BluetoothSocket socket, BluetoothDevice remoteDevice) {
        Config.socket=socket;
        System.out.println(socket+" connecting "+remoteDevice);
        Config.getReadWriteThread(socket).start();
        Intent i=new Intent(Connect.getContext(), ChatWindow.class);
        Connect.getContext().startActivity(i);
    }
    }
