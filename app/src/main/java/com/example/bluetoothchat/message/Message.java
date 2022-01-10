package com.example.bluetoothchat.message;

public class Message {

    private String message;
    private MessageType messageType;
    private String time;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Message(String message, MessageType messageType, String time) {
        this.message = message;
        this.messageType = messageType;
        this.time = time;
    }
}
