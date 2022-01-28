package com.example.bluetoothchat.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

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
            activity.finish();
            Intent intent = new Intent(ChatWindow.getActivity(), Connect.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ChatWindow.getActivity().startActivity(intent);

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
            //Config.socket=null;
            Config.socket.close();
        } catch (Exception ex) {
            errorDialogBox("Some error occurred!! Try again later", 0);
        }

    }


    public static void disconnectConfirm() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ChatWindow.getActivity());
        alertDialogBuilder.setMessage("Are you sure to exit?");
        alertDialogBuilder.setCancelable(true);

        alertDialogBuilder.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        String disconnectMessage = "<--DISCONNECTING-->";
                        Config.setReadWriteThread(Config.socket).write(disconnectMessage.getBytes(StandardCharsets.UTF_8));
                        disconnect();
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


    public static void errorDialogBox(String message, int code) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ChatWindow.getActivity());
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setCancelable(true);
        alertDialogBuilder.setTitle("ERROR");
        alertDialogBuilder.setIcon(R.drawable.warning);

        alertDialogBuilder.setPositiveButton(
                "EXIT",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        disconnect();
                    }
                });

        alertDialogBuilder.setNegativeButton(
                "STAY HERE ",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ChatWindow.getActivity().findViewById(R.id.send).setEnabled(false);
                        ChatWindow.getActivity().findViewById(R.id.send).setAlpha(.50f);
                        Config.setReadWriteThreadAsNull();
                        ChatWindow.disableMenuOptions();
                    }
                });


        ChatWindow.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (Config.getReadWriteThread() != null) {
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
            }
        });

    }


    public static boolean isBluetoothEnabled() {
        BluetoothAdapter bluetoothAdapter = Config.getBluetoothAdapter();
        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
            return false;
        }
        return true;
    }

    public static void confirmBluetoothEnable() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Connect.getContext());
        alertDialogBuilder.setMessage("Switch On Bluetooth?");
        alertDialogBuilder.setCancelable(true);

        alertDialogBuilder.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    @SuppressLint("MissingPermission")
                    public void onClick(DialogInterface dialog, int id) {
                        Config.getBluetoothAdapter().enable();
                        dialog.cancel();
                        Toast.makeText(Connect.getContext(), "Bluetooth switched on successfully", Toast.LENGTH_SHORT).show();
                    }
                });

        alertDialogBuilder.setNegativeButton(
                "Exit",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        System.exit(0);
                    }
                });


        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    public static void confirmAppExit(Context context) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setMessage("Are you sure to exit?");
        alertDialogBuilder.setCancelable(true);

        alertDialogBuilder.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ((Activity)context).moveTaskToBack(true);
                        System.exit(0);
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


}
