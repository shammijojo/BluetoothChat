package com.example.bluetoothchat;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bluetoothchat.config.Config;
import com.example.bluetoothchat.message.Message;
import com.example.bluetoothchat.message.MessageType;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ChatWindow extends AppCompatActivity {

    private static List<Message> list = new ArrayList<>();
    private static ListView listView;
    Button send;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);

        System.out.println("entry");
        send = findViewById(R.id.send);
        editText = findViewById(R.id.message);

        list.add(new Message("Hello!", MessageType.SENT,Calendar.getInstance().getTime().toString()));
        list.add(new Message("Good Morning", MessageType.RECEIVED,Calendar.getInstance().getTime().toString()));

        ChatList adapter = new ChatList(ChatWindow.this, list);
        listView = findViewById(R.id.chatlist);
        listView.setAdapter(adapter);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = editText.getText().toString();
                Config.getReadWriteThread(Config.socket).write(msg.getBytes(StandardCharsets.UTF_8));
                editText.setText("");
            }
        });
    }

    public static void addMsg(Message message) {
        list.add(new Message(message.getMessage(), message.getMessageType(), CommonUtil.getCurrentTime()));
        listView.invalidateViews();
    }

}
