package com.example.bluetoothchat;

public class Message {

    private String message;
    private MessageType messageType;

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

    public Message(String message, MessageType messageType) {
        this.message = message;
        this.messageType = messageType;
    }
}
