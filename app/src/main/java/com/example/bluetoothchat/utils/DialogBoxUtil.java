package com.example.bluetoothchat.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.EditText;
import android.widget.Toast;
import com.example.bluetoothchat.R;
import com.example.bluetoothchat.btactivity.ChatWindow;
import com.example.bluetoothchat.btactivity.Connect;
import com.example.bluetoothchat.config.Config;
import com.example.bluetoothchat.constants.AppConstants;
import com.example.bluetoothchat.constants.DialogBoxMessage;
import com.example.bluetoothchat.constants.ToastMessage;

public class DialogBoxUtil {

     public static void disconnectConfirm() {
          AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
            ChatWindow.getActivity());
          DialogBoxMessage dialogBoxMessage = DialogBoxMessage.DISCONNECT_CONFIRM;
          alertDialogBuilder.setMessage(dialogBoxMessage.getMessage());
          alertDialogBuilder.setCancelable(false);

          alertDialogBuilder.setPositiveButton(
            dialogBoxMessage.getPositiveOption(),
            new DialogInterface.OnClickListener() {
                 public void onClick(DialogInterface dialog, int id) {
                      dialog.cancel();
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
          AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
            ChatWindow.getActivity());
          alertDialogBuilder.setMessage(message.getMessage());
          alertDialogBuilder.setCancelable(false);
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
                      ChatWindow.getActivity().findViewById(R.id.message).setEnabled(false);
                      ((EditText) ChatWindow.getActivity().findViewById(R.id.message))
                        .setHint(AppConstants.DISCONNECTED + new String(Character.toChars(0x2757)));
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

          AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Connect.getActivity());
          DialogBoxMessage dialogBoxMessage = DialogBoxMessage.SWITCH_ON_BLUETOOTH_CONFIRM;
          alertDialogBuilder.setMessage(dialogBoxMessage.getMessage());
          alertDialogBuilder.setCancelable(false);

          alertDialogBuilder.setPositiveButton(
            dialogBoxMessage.getPositiveOption(),
            new DialogInterface.OnClickListener() {
                 @SuppressLint("MissingPermission")
                 public void onClick(DialogInterface dialog, int id) {
                      Config.getBluetoothAdapter().enable();
                      dialog.cancel();
                      Toast.makeText(Connect.getActivity(),
                        ToastMessage.BLUETOOTH_SWITCHED_ON.getMessage(),
                        Toast.LENGTH_SHORT).show();
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

     public static void confirmAppExit(Activity activity) {
          AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
          DialogBoxMessage dialogBoxMessage = DialogBoxMessage.EXIT_CONFIRM;
          alertDialogBuilder.setMessage(dialogBoxMessage.getMessage());
          alertDialogBuilder.setCancelable(false);

          alertDialogBuilder.setPositiveButton(
            dialogBoxMessage.getPositiveOption(),
            new DialogInterface.OnClickListener() {
                 public void onClick(DialogInterface dialog, int id) {
                      (activity).moveTaskToBack(true);
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


     public static void exitAppOnError(DialogBoxMessage dialogBoxMessage) {
          Toast.makeText(Connect.getActivity(),
            "Make sure bluetooth is switched on",
            Toast.LENGTH_SHORT).show();
          AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
            Config.getCurrentActivity());
          alertDialogBuilder.setMessage(dialogBoxMessage.getMessage());
          alertDialogBuilder.setTitle(AppConstants.ERROR);
          alertDialogBuilder.setIcon(R.drawable.warning);
          alertDialogBuilder.setCancelable(false);

          alertDialogBuilder.setPositiveButton(
            dialogBoxMessage.getPositiveOption(),
            new DialogInterface.OnClickListener() {
                 public void onClick(DialogInterface dialog, int id) {
                      dialog.cancel();
                      CommonUtil.disconnect();

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

     private static void databaseError() {
          AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
            Config.getCurrentActivity());
          DialogBoxMessage dialogBoxMessage = DialogBoxMessage.DATABASE_ERROR;
          alertDialogBuilder.setMessage(dialogBoxMessage.getMessage());
          alertDialogBuilder.setTitle(AppConstants.ERROR);
          alertDialogBuilder.setIcon(R.drawable.warning);
          alertDialogBuilder.setCancelable(false);

          alertDialogBuilder.setPositiveButton(
            dialogBoxMessage.getPositiveOption(),
            new DialogInterface.OnClickListener() {
                 public void onClick(DialogInterface dialog, int id) {
                      dialog.cancel();
                      System.exit(0);

                 }
            });

          alertDialogBuilder.setNegativeButton(
            dialogBoxMessage.getNegativeOption(),
            new DialogInterface.OnClickListener() {
                 public void onClick(DialogInterface dialog, int id) {
                 }
            });

          AlertDialog alertDialog = alertDialogBuilder.create();
          alertDialog.show();
     }

     public static void runDatabaseErrorDialogFromLooper() {
          Config.getCurrentActivity().runOnUiThread(new Runnable() {
               @Override
               public void run() {
                    databaseError();
               }
          });
     }

}
