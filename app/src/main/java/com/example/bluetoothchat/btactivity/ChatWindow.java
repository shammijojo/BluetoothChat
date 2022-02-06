package com.example.bluetoothchat.btactivity;

import static com.example.bluetoothchat.utils.DialogBoxUtil.confirmAppExit;
import static com.example.bluetoothchat.utils.DialogBoxUtil.disconnectConfirm;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.example.bluetoothchat.constants.DialogBoxMessage;
import com.example.bluetoothchat.constants.MenuItemOptions;
import com.example.bluetoothchat.model.Message;
import com.example.bluetoothchat.utils.CommonUtil;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ChatWindow extends AppCompatActivity {

    private static List<Message> list;
    private static ListView listView;
    private ImageButton send;
    private EditText editText;
    private static Menu menu;
    private static Context context;
    public static Activity activity;
    private static ChatListAdapter adapter;

    public static void addMsg(Message message) {
        list.add(
          new Message(message.getMessage(), message.getMessageType(),
            CommonUtil.getCurrentTime()));
        listView.invalidateViews();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        menu = null;
        context = null;
        adapter = null;
        listView = null;
        list = null;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return selectMenuItemOption(item, ChatWindow.this);
    }

    public static void disableMenuOptions() {
        if (menu != null) {
            menu.getItem(0).getSubMenu().getItem(0).setEnabled(false);
        }
    }

    @Override
    public void onBackPressed() {
        disconnectConfirm();
    }

    public static boolean selectMenuItemOption(MenuItem item, Activity activity) {
        if (item.getTitle().toString().equals(MenuItemOptions.DISCONNECT.getOption())) {
            disconnectConfirm();
        } else if (item.getTitle().toString().equals(MenuItemOptions.EXIT.getOption())) {
            confirmAppExit(activity);
        } else if (item.getTitle().toString()
          .equals(MenuItemOptions.CLEAR_CHAT_HISTORY.getOption())) {
            clearChatHistoryConfirm();
        }
        return true;
    }

    public static void clearChatHistoryConfirm() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
          ChatWindow.getActivity());
        DialogBoxMessage dialogBuilderMessage = DialogBoxMessage.CLEAR_CHAT_HISTORY_CONFIRM;
        alertDialogBuilder.setMessage(dialogBuilderMessage.getMessage());
        alertDialogBuilder.setCancelable(true);

        alertDialogBuilder.setPositiveButton(
          dialogBuilderMessage.getPositiveOption(),
          new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog, int id) {
                  Config.getDatabaseObject(context).deleteChats();
                  list.clear();
                  adapter.notifyDataSetChanged();
                  dialog.cancel();
              }
          });

        alertDialogBuilder.setNegativeButton(
          dialogBuilderMessage.getNegativeOption(),
          new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog, int id) {
                  dialog.cancel();
              }
          });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void initialise() {
        list = new ArrayList<>();
        context = getApplicationContext();
        activity = this;

        send = findViewById(R.id.send);
        editText = findViewById(R.id.message);

        adapter = new ChatListAdapter(ChatWindow.this, list);
        listView = findViewById(R.id.chatlist);
        listView.setAdapter(adapter);
    }

    public static Context getContext() {
        return context;
    }

    public static Activity getActivity() {
        return activity;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu m) {
        super.onCreateOptionsMenu(m);
        Config.setCurrentActivity(this);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, m);
        menu = m;
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Config.setCurrentActivity(this);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_twotone_bluetooth_searching_24);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        overridePendingTransition(0, 0);
        setContentView(R.layout.activity_chat_window);
        initialise();
        list.clear();
        list.addAll(Config.getDatabaseObject(getApplicationContext()).readTable());

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = editText.getText().toString();
                Config.setReadWriteThread(Config.socket)
                  .write(msg.getBytes(StandardCharsets.UTF_8));
                editText.setText("");
            }
        });
    }

}
