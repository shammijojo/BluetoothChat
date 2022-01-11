package com.example.bluetoothchat;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.example.bluetoothchat.connection.Connect;
import com.example.bluetoothchat.message.Message;
import com.example.bluetoothchat.message.MessageType;

import java.time.OffsetTime;
import java.util.Calendar;

public class CommonUtil {

    public static String getCurrentTime(){
        Calendar calendar=Calendar.getInstance();
        String hour=String.valueOf(calendar.getTime().getHours());
        String minute=String.valueOf(calendar.getTime().getMinutes());

        String noon="AM";

        if(Integer.valueOf(hour)>12)
            noon="PM";

        if(Integer.valueOf(hour)>12)
            hour=String.valueOf(Integer.parseInt(hour)-12);
        if(hour.equals("0"))
            hour="12";

        if(Integer.valueOf(minute)<10)
            minute="0"+minute;

        return  hour+":"+minute+" "+noon;

    }

    @SuppressLint("MissingPermission")
    public static void getDeviceType(View view, BluetoothDevice bluetoothDevice){
        Integer bluetoothClass=bluetoothDevice.getBluetoothClass().getDeviceClass();
        DeviceType deviceType=DeviceType.OTHERS;

        if(bluetoothClass>=512 && bluetoothClass<=532)
            deviceType= DeviceType.SMARTPHONE;
        else if(bluetoothClass>=256 && bluetoothClass<=280)
            deviceType= DeviceType.LAPTOP;
        else if(bluetoothClass>=1024 && bluetoothClass<=1096)
            deviceType= DeviceType.SPEAKER;


        switch (deviceType){
            case SMARTPHONE:
                ((ImageView)view.findViewById(R.id.deviceIcon)).setImageResource(R.drawable.phone);
                break;
            case LAPTOP:
                ((ImageView)view.findViewById(R.id.deviceIcon)).setImageResource(R.drawable.laptop);
                break;
            case SPEAKER:
                ((ImageView)view.findViewById(R.id.deviceIcon)).setImageResource(R.drawable.audio);
                break;
            case OTHERS:
                ((ImageView)view.findViewById(R.id.deviceIcon)).setImageResource(R.drawable.others);
        }

    }

}
