package com.example.bluetoothchat.connection;

import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.example.bluetoothchat.btactivity.ChatWindow;
import com.example.bluetoothchat.btactivity.Connect;
import com.example.bluetoothchat.btactivity.DeviceList;
import com.example.bluetoothchat.config.Config;
import com.example.bluetoothchat.enums.MessageType;
import com.example.bluetoothchat.model.Message;
import com.example.bluetoothchat.utils.CommonUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class ReadWriteThread extends Thread {
    private final BluetoothSocket bluetoothSocket;
    private final InputStream inputStream;
    private final OutputStream outputStream;
    private static boolean disconnect;
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
        Config.setDeviceName(socket.getRemoteDevice().getAddress().replace(":", "_"));
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

                if (str.equals("<--DISCONNECTING-->")) {
                    CommonUtil.errorDialogBox("Connection Lost!!", 1);
                    break;
                }

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        Toast toast = Toast.makeText(Connect.getContext(), str, Toast.LENGTH_SHORT);
                        toast.show();
                        Message message = new Message(str, MessageType.RECEIVED, CommonUtil.getCurrentTime());
                        ChatWindow.addMsg(message);
                        Config.getDatabaseObject(DeviceList.getContext()).insertIntoTable(message);
                    }
                });


                // Send the obtained bytes to the UI Activity
                handler.obtainMessage(1, bytes, -1,
                        buffer).sendToTarget();

                Arrays.fill(buffer, (byte) 0);


            } catch (IOException e) {
                CommonUtil.errorDialogBox("Some error occurred!! Try again later", 0);
                break;
            }
        }
    }

    // write to OutputStream
    public void write(byte[] buffer) {
        try {
            outputStream.write(buffer);
            String str = new String(buffer, StandardCharsets.UTF_8).trim();
            if (str.equals("<--DISCONNECTING-->")) {
                CommonUtil.disconnect();
                return;
            }

            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    Toast toast = Toast.makeText(Connect.getContext(), str, Toast.LENGTH_SHORT);
                    toast.show();
                    Message message = new Message(str, MessageType.SENT, CommonUtil.getCurrentTime());
                    ChatWindow.addMsg(message);
                    Config.getDatabaseObject(DeviceList.getContext()).insertIntoTable(message);
                }
            });
            handler.obtainMessage(2, -1, -1,
                    buffer).sendToTarget();
        } catch (IOException e) {
            CommonUtil.errorDialogBox("Some error occurred!! Try again later", 0);
        }
    }

    public void cancel() {
        try {
            bluetoothSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public BluetoothSocket getSocket() {
        return bluetoothSocket;
    }

}
