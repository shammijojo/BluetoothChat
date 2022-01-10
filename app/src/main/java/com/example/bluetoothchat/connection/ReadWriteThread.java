package com.example.bluetoothchat.connection;

import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.example.bluetoothchat.ChatWindow;
import com.example.bluetoothchat.message.Message;
import com.example.bluetoothchat.message.MessageType;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class ReadWriteThread extends Thread {
    private final BluetoothSocket bluetoothSocket;
    private final InputStream inputStream;
    private final OutputStream outputStream;
    Handler handler;

    public ReadWriteThread(BluetoothSocket socket) {
        this.bluetoothSocket = socket;
        InputStream tmpIn = null;
        OutputStream tmpOut = null;
        handler = new Handler(Looper.getMainLooper());

        try {
            tmpIn = socket.getInputStream();
            tmpOut = socket.getOutputStream();
        } catch (IOException e) {
        }

        inputStream = tmpIn;
        outputStream = tmpOut;
    }

    public void run() {
        byte[] buffer = new byte[1024];
        int bytes;
        Arrays.fill(buffer, (byte) 0);

        // Keep listening to the InputStream
        while (true) {
            // System.out.println("inside");
            try {
                // Read from the InputStream
                // byte[] arr="hello".getBytes(StandardCharsets.UTF_8);

                bytes = inputStream.read(buffer);
                String str = new String(buffer, StandardCharsets.UTF_8).trim();
                System.out.println("jojo" + new String(buffer, StandardCharsets.UTF_8).trim());


                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        Toast toast = Toast.makeText(Connect.getContext(), str, Toast.LENGTH_SHORT);
                        toast.show();
                        ChatWindow.addMsg(new Message(str, MessageType.RECEIVED));
                    }
                });


                // Send the obtained bytes to the UI Activity
                handler.obtainMessage(1, bytes, -1,
                        buffer).sendToTarget();

                Arrays.fill(buffer, (byte) 0);


            } catch (IOException e) {
                //connectionLost();
                // Start the service over to restart listening mode
                this.start();
                break;
            }
        }
    }

    // write to OutputStream
    public void write(byte[] buffer) {
        try {
            outputStream.write(buffer);
            String str = new String(buffer, StandardCharsets.UTF_8).trim();
            System.out.println(new String(buffer, StandardCharsets.UTF_8).trim());
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    Toast toast = Toast.makeText(Connect.getContext(), str, Toast.LENGTH_SHORT);
                    toast.show();
                    ChatWindow.addMsg(new Message(str, MessageType.SENT));
                }
            });
            handler.obtainMessage(2, -1, -1,
                    buffer).sendToTarget();
        } catch (IOException e) {
        }
    }

    public void cancel() {
        try {
            bluetoothSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
