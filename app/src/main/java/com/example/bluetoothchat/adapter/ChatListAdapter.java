package com.example.bluetoothchat.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;

import com.example.bluetoothchat.R;
import com.example.bluetoothchat.constants.MessageType;
import com.example.bluetoothchat.model.Message;

import java.util.List;

public class ChatListAdapter extends ArrayAdapter<Message> {

    private LayoutInflater inflater;

    public ChatListAdapter(@NonNull Context context, @NonNull List<Message> list) {
        super(context, R.layout.chatbox_layout, list);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @SuppressLint("MissingPermission")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = inflater.inflate(R.layout.chatbox_layout, null, true);
        TextView editText = view.findViewById(R.id.chatbox);
        TextView currentTime = view.findViewById(R.id.time);

        Message msg = getItem(position);
        editText.setText(msg.getMessage());
        currentTime.setText(msg.getTime());

        if (msg.getMessageType() == MessageType.SENT) {
            editText.setBackground(ContextCompat.getDrawable(this.getContext(), R.drawable.sent_msg));
            ConstraintLayout constraintLayout = view.findViewById(R.id.chatboxLayout);
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(constraintLayout);
            constraintSet.connect(R.id.chatbox, ConstraintSet.RIGHT, R.id.chatboxLayout, ConstraintSet.RIGHT, 10);
            constraintSet.applyTo(constraintLayout);

            ConstraintLayout constraintLayoutForTime = view.findViewById(R.id.chatboxLayout);
            ConstraintSet constraintSetForTime = new ConstraintSet();
            constraintSetForTime.clone(constraintLayoutForTime);
            constraintSetForTime.connect(R.id.time, ConstraintSet.RIGHT, R.id.chatbox, ConstraintSet.RIGHT, 10);
            constraintSetForTime.applyTo(constraintLayoutForTime);
        } else {
            ConstraintLayout constraintLayout = view.findViewById(R.id.chatboxLayout);
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(constraintLayout);
            constraintSet.connect(R.id.chatbox, ConstraintSet.LEFT, R.id.chatboxLayout, ConstraintSet.LEFT, 10);
            constraintSet.applyTo(constraintLayout);

            ConstraintLayout constraintLayoutForTime = view.findViewById(R.id.chatboxLayout);
            ConstraintSet constraintSetForTime = new ConstraintSet();
            constraintSetForTime.clone(constraintLayoutForTime);
            constraintSetForTime.connect(R.id.time, ConstraintSet.LEFT, R.id.chatbox, ConstraintSet.LEFT, 10);
            constraintSetForTime.applyTo(constraintLayoutForTime);
        }

        return view;
    }

}
