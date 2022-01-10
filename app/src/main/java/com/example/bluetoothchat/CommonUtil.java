package com.example.bluetoothchat;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.OffsetTime;
import java.util.Calendar;

public class CommonUtil {

    public static String getCurrentTime(){
        //OffsetTime offsetTime=OffsetTime.now();
//        String hour= String.valueOf(offsetTime.getHour());
//        String minute= String.valueOf(offsetTime.getMinute());

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

}
