package com.example.bluetoothchat.utils;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.bluetoothchat.R;
import com.example.bluetoothchat.btactivity.ChatWindow;
import com.example.bluetoothchat.btactivity.Connect;
import com.example.bluetoothchat.config.Config;
import com.example.bluetoothchat.constants.DeviceType;

import java.util.Calendar;

public class CommonUtil {

    public static String getCurrentTime() {
        Calendar calendar = Calendar.getInstance();
        String hour = String.valueOf(calendar.getTime().getHours());
        String minute = String.valueOf(calendar.getTime().getMinutes());

        String month = String.valueOf(calendar.getTime().getMonth() + 1);
        String day = String.valueOf(calendar.getTime().getDate());

        String noon = "AM";

        if (Integer.valueOf(hour) > 12)
            noon = "PM";

        if (Integer.valueOf(hour) > 12)
            hour = String.valueOf(Integer.parseInt(hour) - 12);

        if (hour.equals("0"))
            hour = "12";

        if (Integer.valueOf(minute) < 10)
            minute = "0" + minute;

        return day + "/" + month + " " + hour + ":" + minute + " " + noon;

    }

    @SuppressLint("MissingPermission")
    public static void getDeviceType(View view, BluetoothDevice bluetoothDevice) {
        Integer bluetoothClass = bluetoothDevice.getBluetoothClass().getDeviceClass();
        DeviceType deviceType = DeviceType.OTHERS;

        if (bluetoothClass >= 512 && bluetoothClass <= 532)
            deviceType = DeviceType.SMARTPHONE;
        else if (bluetoothClass >= 256 && bluetoothClass <= 280)
            deviceType = DeviceType.LAPTOP;
        else if (bluetoothClass >= 1024 && bluetoothClass <= 1096)
            deviceType = DeviceType.SPEAKER;

        switch (deviceType) {
            case SMARTPHONE:
                ((ImageView) view.findViewById(R.id.deviceIcon)).setImageResource(R.drawable.phone);
                break;
            case LAPTOP:
                ((ImageView) view.findViewById(R.id.deviceIcon)).setImageResource(R.drawable.laptop);
                break;
            case SPEAKER:
                ((ImageView) view.findViewById(R.id.deviceIcon)).setImageResource(R.drawable.audio);
                break;
            case OTHERS:
                ((ImageView) view.findViewById(R.id.deviceIcon)).setImageResource(R.drawable.others);
        }
    }


    public static void disconnect() {
        try {
            Activity activity = ChatWindow.getActivity();
            Intent intent = new Intent(ChatWindow.getContext(), Connect.class);
            activity.finish();
            //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            activity.startActivity(intent);

            if (Config.getConnectThread() != null) {
                Config.getConnectThread().interrupt();
            }

            if (Config.getAcceptThread() != null) {
                Config.getAcceptThread().interrupt();
            }

            Config.getReadWriteThread().interrupt();
            Config.setReadWriteThreadAsNull();
            Config.setAcceptThreadAsNull();
            Config.setConnectThreadAsNull();
            Config.socket.close();
        } catch (Exception ex) {
            Log.e(TAG, "Error occurred while disconnecting");
            System.exit(0);
        }
    }

    public static boolean isBluetoothEnabled() {
        BluetoothAdapter bluetoothAdapter = Config.getBluetoothAdapter();
        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
            return false;
        }
        return true;
    }
}
