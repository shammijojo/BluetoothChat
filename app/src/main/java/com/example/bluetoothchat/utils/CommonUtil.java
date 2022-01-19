package com.example.bluetoothchat.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.example.bluetoothchat.R;
import com.example.bluetoothchat.btactivity.ChatWindow;
import com.example.bluetoothchat.btactivity.Connect;
import com.example.bluetoothchat.config.Config;
import com.example.bluetoothchat.enums.DeviceType;

import java.nio.charset.StandardCharsets;
import java.util.Calendar;

public class CommonUtil {

    public static String getCurrentTime() {
        Calendar calendar = Calendar.getInstance();
        String hour = String.valueOf(calendar.getTime().getHours());
        String minute = String.valueOf(calendar.getTime().getMinutes());

        String noon = "AM";

        if (Integer.valueOf(hour) > 12)
            noon = "PM";

        if (Integer.valueOf(hour) > 12)
            hour = String.valueOf(Integer.parseInt(hour) - 12);
        if (hour.equals("0"))
            hour = "12";

        if (Integer.valueOf(minute) < 10)
            minute = "0" + minute;

        return hour + ":" + minute + " " + noon;

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

    public static boolean selectMenuItemOption(MenuItem item, Activity activity) {
        if (item.getTitle().toString().equals("Disconnect")) {
            disconnectConfirm();
        } else if (item.getTitle().toString().equals("Exit")) {
            activity.moveTaskToBack(true);
            System.exit(0);
        }
        return true;
    }

    public static void disconnect() {
        Config.getReadWriteThread().interrupt();
        Config.setReadWriteThreadAsNull();
        Config.setAcceptThreadAsNull();
        Config.setConnectThreadAsNull();
        ChatWindow.getActivity().finish();
        Intent intent = new Intent(ChatWindow.getActivity(), Connect.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ChatWindow.getActivity().startActivity(intent);
    }


    private static void disconnectConfirm() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ChatWindow.getActivity());
        alertDialogBuilder.setMessage("Are you sure to exit?");
        alertDialogBuilder.setCancelable(true);

        alertDialogBuilder.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String disconnectMessage = "<--DISCONNECTING-->";
                        Config.setReadWriteThread(Config.socket).write(disconnectMessage.getBytes(StandardCharsets.UTF_8));
                        dialog.cancel();
                    }
                });

        alertDialogBuilder.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    public static void errorDialogBox(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ChatWindow.getActivity());
        alertDialogBuilder.setMessage("Some error occurred!!Try again later");
        alertDialogBuilder.setCancelable(true);
        alertDialogBuilder.setTitle("ERROR");
        alertDialogBuilder.setIcon(R.drawable.warning);

        alertDialogBuilder.setPositiveButton(
                "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String disconnectMessage = "<--DISCONNECTING-->";
                        Config.setReadWriteThread(Config.socket).write(disconnectMessage.getBytes(StandardCharsets.UTF_8));
                        Config.setConnectThreadAsNull();
                        Config.setAcceptThreadAsNull();
                        Config.setReadWriteThreadAsNull();
                        dialog.cancel();
                        ChatWindow.getActivity().finish();
                        Intent i = new Intent(ChatWindow.getActivity(), Connect.class);
                        ChatWindow.getActivity().startActivity(i);
                    }
                });


        ChatWindow.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });

    }


    public static boolean isBluetoothEnabled(){
        BluetoothAdapter bluetoothAdapter=Config.getBluetoothAdapter();
        if(bluetoothAdapter==null || !bluetoothAdapter.isEnabled()){
            return false;
        }
        return true;
    }

}
