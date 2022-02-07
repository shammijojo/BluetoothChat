package com.example.bluetoothchat.connection;

import static android.content.ContentValues.TAG;

import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import com.example.bluetoothchat.btactivity.ChatWindow;
import com.example.bluetoothchat.btactivity.DeviceList;
import com.example.bluetoothchat.config.Config;
import com.example.bluetoothchat.constants.AppConstants;
import com.example.bluetoothchat.constants.DialogBoxMessage;
import com.example.bluetoothchat.constants.MessageType;
import com.example.bluetoothchat.model.Message;
import com.example.bluetoothchat.utils.CommonUtil;
import com.example.bluetoothchat.utils.DialogBoxUtil;
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
               Log.e(TAG, "Error while getting I/O stream from socket");
               CommonUtil.callExitFromLooper(DialogBoxMessage.EXIT_APP);
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
               try {
                    // Read from the InputStream
                    bytes = inputStream.read(buffer);
                    String str = new String(buffer, StandardCharsets.UTF_8).trim();

                    if (str.equals(AppConstants.DISCONNECTING)) {
                         DialogBoxUtil.errorDialogBox(DialogBoxMessage.CONNECTION_LOST, 1);
                         break;
                    }

                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                         @Override
                         public void run() {
                              Message message = new Message(str, MessageType.RECEIVED,
                                CommonUtil.getCurrentTime());
                              ChatWindow.addMsg(message);
                              Config.getDatabaseObject(DeviceList.getContext())
                                .insertIntoTable(message);
                         }
                    });

                    // Send the obtained bytes to the UI Activity
                    handler.obtainMessage(1, bytes, -1,
                      buffer).sendToTarget();

                    Arrays.fill(buffer, (byte) 0);


               } catch (IOException e) {
                    DialogBoxUtil.errorDialogBox(DialogBoxMessage.ERROR_OCCURRED, 0);
                    break;
               }
          }
     }

     // write to OutputStream
     public void write(byte[] buffer) {
          try {
               outputStream.write(buffer);
               String str = new String(buffer, StandardCharsets.UTF_8).trim();
               if (str.equals(AppConstants.DISCONNECTING)) {
                    CommonUtil.disconnect();
                    return;
               }

               new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                         Message message = new Message(str, MessageType.SENT,
                           CommonUtil.getCurrentTime());
                         ChatWindow.addMsg(message);
                         Config.getDatabaseObject(DeviceList.getContext()).insertIntoTable(message);
                    }
               });
               handler.obtainMessage(2, -1, -1,
                 buffer).sendToTarget();
          } catch (IOException e) {
               DialogBoxUtil.errorDialogBox(DialogBoxMessage.ERROR_OCCURRED, 0);
          }
     }
}
