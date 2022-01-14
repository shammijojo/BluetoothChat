package com.example.bluetoothchat;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bluetoothchat.config.Config;
import com.example.bluetoothchat.message.Message;
import com.example.bluetoothchat.message.MessageType;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ChatWindow extends AppCompatActivity {

    private  static List<Message> list = new ArrayList<>();
    private static ListView listView;
    ImageButton send;
    EditText editText;
    static Activity activity;

    @Override
    public boolean onCreateOptionsMenu(Menu m) {
        super.onCreateOptionsMenu(m);
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu,m);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return CommonUtil.selectMenuItemOption(item,ChatWindow.this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);
        activity=ChatWindow.this;

        System.out.println("entry");
        send = findViewById(R.id.send);
        editText = findViewById(R.id.message);

        ChatList adapter = new ChatList(ChatWindow.this, list);
        listView = findViewById(R.id.chatlist);
        listView.setAdapter(adapter);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = editText.getText().toString();
                Config.setReadWriteThread(Config.socket).write(msg.getBytes(StandardCharsets.UTF_8));
                editText.setText("");
            }
        });


    }

    public static void addMsg(Message message) {
        list.add(new Message(message.getMessage(), message.getMessageType(), CommonUtil.getCurrentTime()));
        listView.invalidateViews();
    }

    public static Activity getActivity(){
        return activity;
    }

}
