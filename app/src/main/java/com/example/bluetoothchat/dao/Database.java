package com.example.bluetoothchat.dao;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.example.bluetoothchat.config.Config;
import com.example.bluetoothchat.constants.MessageType;
import com.example.bluetoothchat.model.Message;
import com.example.bluetoothchat.utils.DialogBoxUtil;
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
          try {
               sqLiteDatabase = this.getWritableDatabase();
               String query = "create table if not exists device_" + deviceName +
                 "(message text,message_tye text,time text);";
               sqLiteDatabase.execSQL(query);
          } catch (Exception ex) {
               Log.e(TAG, "Error while creating table in SQLite");
               DialogBoxUtil.runDatabaseErrorDialogFromLooper();
          }
     }

     public void insertIntoTable(Message message) {
          try {
               sqLiteDatabase = this.getWritableDatabase();
               String query =
                 "insert into device_" + deviceName + " values(\"" + message.getMessage() +
                   "\",\"" + message.getMessageType() + "\",\"" + message.getTime() + "\");";
               sqLiteDatabase.execSQL(query);
               readTable();
          } catch (Exception ex) {
               Log.e(TAG, "Error while inserting data into SQLite");
               DialogBoxUtil.runDatabaseErrorDialogFromLooper();
          }
     }

     public List<Message> readTable() {
          List<Message> list = new ArrayList<>();
          try {
               createTables();
               sqLiteDatabase = this.getReadableDatabase();
               String query = "select * from device_" + deviceName;
               Cursor cursor = sqLiteDatabase.rawQuery(query, null);
               while (cursor.moveToNext()) {
                    Message message = new Message(cursor.getString(0),
                      MessageType.valueOf(cursor.getString(1)),
                      cursor.getString(2));
                    list.add(message);
               }
          } catch (Exception ex) {
               Log.e(TAG, "Error while reading  data from SQLite");
               DialogBoxUtil.runDatabaseErrorDialogFromLooper();
          }

          return list;
     }


     public void deleteChats() {
          try {
               sqLiteDatabase = this.getWritableDatabase();
               String query = "DELETE from device_" + deviceName;
               sqLiteDatabase.execSQL(query);
          } catch (Exception ex) {
               Log.e(TAG, "Error while deleting  data from SQLite");
               DialogBoxUtil.runDatabaseErrorDialogFromLooper();
          }
     }

     @Override
     public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

     }
}
