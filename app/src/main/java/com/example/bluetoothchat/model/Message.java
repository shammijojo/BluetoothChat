package com.example.bluetoothchat.model;

import com.example.bluetoothchat.constants.MessageType;

public class Message {

    private String message;
    private MessageType messageType;
    private String time;

    public String getMessage() {
        return message;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public String getTime() {
        return time;
    }

    public Message(String message, MessageType messageType, String time) {
        this.message = message;
        this.messageType = messageType;
        this.time = time;
    }
}
