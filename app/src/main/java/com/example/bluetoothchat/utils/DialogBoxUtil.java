package com.example.bluetoothchat.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

import com.example.bluetoothchat.R;
import com.example.bluetoothchat.btactivity.ChatWindow;
import com.example.bluetoothchat.btactivity.Connect;
import com.example.bluetoothchat.config.Config;
import com.example.bluetoothchat.constants.AppConstants;
import com.example.bluetoothchat.constants.DialogBoxMessage;
import com.example.bluetoothchat.constants.ToastMessage;

import java.nio.charset.StandardCharsets;

public class DialogBoxUtil {

    public static void disconnectConfirm() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ChatWindow.getContext());
        DialogBoxMessage dialogBoxMessage = DialogBoxMessage.EXIT_CONFIRM;
        alertDialogBuilder.setMessage(dialogBoxMessage.getMessage());
        alertDialogBuilder.setCancelable(true);

        alertDialogBuilder.setPositiveButton(
                dialogBoxMessage.getPositiveOption(),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        String disconnectMessage = AppConstants.DISCONNECTING;
                        Config.setReadWriteThread(Config.socket).write(disconnectMessage.getBytes(StandardCharsets.UTF_8));
                        CommonUtil.disconnect();
                    }
                });

        alertDialogBuilder.setNegativeButton(
                dialogBoxMessage.getNegativeOption(),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public static void errorDialogBox(DialogBoxMessage message, int code) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ChatWindow.getContext());
        alertDialogBuilder.setMessage(message.toString());
        alertDialogBuilder.setCancelable(true);
        alertDialogBuilder.setTitle(AppConstants.ERROR);
        alertDialogBuilder.setIcon(R.drawable.warning);

        alertDialogBuilder.setPositiveButton(
                message.getPositiveOption(),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        CommonUtil.disconnect();
                    }
                });

        alertDialogBuilder.setNegativeButton(
                message.getNegativeOption(),
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

    public static void confirmBluetoothEnable() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Connect.getContext());
        DialogBoxMessage dialogBoxMessage = DialogBoxMessage.SWITCH_ON_BLUETOOTH_CONFIRM;
        alertDialogBuilder.setMessage(dialogBoxMessage.getMessage());
        alertDialogBuilder.setCancelable(true);

        alertDialogBuilder.setPositiveButton(
                dialogBoxMessage.getPositiveOption(),
                new DialogInterface.OnClickListener() {
                    @SuppressLint("MissingPermission")
                    public void onClick(DialogInterface dialog, int id) {
                        Config.getBluetoothAdapter().enable();
                        dialog.cancel();
                        Toast.makeText(Connect.getContext(), ToastMessage.BLUETOOTH_SWITCHED_OD.toString(), Toast.LENGTH_SHORT).show();
                    }
                });

        alertDialogBuilder.setNegativeButton(
                dialogBoxMessage.getNegativeOption(),
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
        DialogBoxMessage dialogBoxMessage = DialogBoxMessage.EXIT_CONFIRM;
        alertDialogBuilder.setMessage(dialogBoxMessage.getMessage());
        alertDialogBuilder.setCancelable(true);

        alertDialogBuilder.setPositiveButton(
                dialogBoxMessage.getPositiveOption(),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ((Activity) context).moveTaskToBack(true);
                        System.exit(0);
                    }
                });

        alertDialogBuilder.setNegativeButton(
                dialogBoxMessage.getNegativeOption(),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }


}
