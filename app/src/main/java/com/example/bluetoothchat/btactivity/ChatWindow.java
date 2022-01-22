package com.example.bluetoothchat.btactivity;

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

import com.example.bluetoothchat.R;
import com.example.bluetoothchat.adapter.ChatListAdapter;
import com.example.bluetoothchat.config.Config;
import com.example.bluetoothchat.model.Message;
import com.example.bluetoothchat.utils.CommonUtil;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ChatWindow extends AppCompatActivity {

    private static List<Message> list = new ArrayList<>();
    private static ListView listView;
    private ImageButton send;
    private EditText editText;
    private static Activity activity;

    @Override
    public boolean onCreateOptionsMenu(Menu m) {
        super.onCreateOptionsMenu(m);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, m);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return CommonUtil.selectMenuItemOption(item, ChatWindow.this);
    }

    @Override
    public void onBackPressed() {
        CommonUtil.disconnectConfirm();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(0,0);
        setContentView(R.layout.activity_chat_window);
        initialise();

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

    private void initialise(){
        activity = ChatWindow.this;
        send = findViewById(R.id.send);
        editText = findViewById(R.id.message);

        ChatListAdapter adapter = new ChatListAdapter(ChatWindow.this, list);
        listView = findViewById(R.id.chatlist);
        listView.setAdapter(adapter);
    }

    public static Activity getActivity() {
        return activity;
    }

}
