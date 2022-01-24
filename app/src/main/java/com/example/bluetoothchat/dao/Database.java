package com.example.bluetoothchat.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.bluetoothchat.config.Config;
import com.example.bluetoothchat.enums.MessageType;
import com.example.bluetoothchat.model.Message;

import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "MyDBName.db";
    private SQLiteDatabase sqLiteDatabase;

    public Database(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
       this.sqLiteDatabase=sqLiteDatabase;
    }

    public void createTables() {
        sqLiteDatabase=this.getWritableDatabase();
        String deviceName = Config.getReadWriteThread().getSocket().getRemoteDevice().getAddress().replace(":","_");
        System.out.println("Connected device: "+deviceName);
        String query="create table if not exists device_"+ deviceName+
                "(message text,message_tye text,time text);";
        sqLiteDatabase.execSQL(query);
    }

    public void insertIntoTable(Message message){
        sqLiteDatabase=this.getWritableDatabase();
        String deviceName = Config.getReadWriteThread().getSocket().getRemoteDevice().getAddress().replace(":","_");
        String query="insert into device_"+deviceName+" values(\""+message.getMessage()+
                "\",\""+message.getMessageType()+"\",\""+message.getTime()+"\");";
        sqLiteDatabase.execSQL(query);
        readTable();
    }

    public List<Message> readTable(){
        createTables();
        List<Message> list=new ArrayList<>();
        System.out.println("Reading table....");
        String deviceName = Config.getReadWriteThread().getSocket().getRemoteDevice().getAddress().replace(":","_");
        sqLiteDatabase=this.getReadableDatabase();
        String query="select * from device_"+deviceName;
        Cursor cursor=sqLiteDatabase.rawQuery(query,null);
        while(cursor.moveToNext()){
            //System.out.println(cursor.getString(0)+" "+cursor.getString(1));
            Message message=new Message(cursor.getString(0), MessageType.valueOf(cursor.getString(1)),cursor.getString(2));
            list.add(message);
        }
        return list;
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
