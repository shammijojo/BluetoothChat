package com.example.bluetoothchat.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.bluetoothchat.config.Config;
import com.example.bluetoothchat.constants.MessageType;
import com.example.bluetoothchat.model.Message;

import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "MyDBName.db";
    private SQLiteDatabase sqLiteDatabase;
    private String deviceName;

    public Database(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        this.sqLiteDatabase = sqLiteDatabase;
        deviceName = Config.getDeviceName();
    }

    public void createTables() {
        sqLiteDatabase = this.getWritableDatabase();
        String query = "create table if not exists device_" + deviceName +
                "(message text,message_tye text,time text);";
        sqLiteDatabase.execSQL(query);
    }

    public void insertIntoTable(Message message) {
        sqLiteDatabase = this.getWritableDatabase();
        String query = "insert into device_" + deviceName + " values(\"" + message.getMessage() +
                "\",\"" + message.getMessageType() + "\",\"" + message.getTime() + "\");";
        sqLiteDatabase.execSQL(query);
        readTable();
    }

    public List<Message> readTable() {
        createTables();
        List<Message> list = new ArrayList<>();
        sqLiteDatabase = this.getReadableDatabase();
        String query = "select * from device_" + deviceName;
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        while (cursor.moveToNext()) {
            Message message = new Message(cursor.getString(0), MessageType.valueOf(cursor.getString(1)), cursor.getString(2));
            list.add(message);
        }
        return list;
    }


    public void deleteChats() {
        sqLiteDatabase = this.getWritableDatabase();
        String query = "DELETE from device_" + deviceName;
        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
