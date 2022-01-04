package com.example.bluetoothchat;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;

import java.util.List;

public class ChatList extends ArrayAdapter<Message> {

    LayoutInflater inflater;

    public ChatList(@NonNull Context context, @NonNull List<Message> list) {
        super(context,R.layout.chatbox_layout,list);
        inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @SuppressLint("MissingPermission")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view=inflater.inflate(R.layout.chatbox_layout,null,true);
        TextView editText=view.findViewById(R.id.chatbox);

        Message msg=getItem(position);
        editText.setText(msg.getMessage());

        if(msg.getMessageType()==MessageType.SENT){

            editText.setBackground(ContextCompat.getDrawable(this.getContext(),R.drawable.sent_msg));
            ConstraintLayout constraintLayout=view.findViewById(R.id.chatboxLayout);
            ConstraintSet constraintSet=new ConstraintSet();
            constraintSet.clone(constraintLayout);
            constraintSet.connect(R.id.chatbox,ConstraintSet.RIGHT,R.id.chatboxLayout,ConstraintSet.RIGHT,10);
            constraintSet.applyTo(constraintLayout);
        }

        return view;
    }

}
