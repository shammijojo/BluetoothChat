package com.example.bluetoothchat.constants;

public enum ToastMessage {

    BLUETOOTH_SWITCHED_OD("Bluetooth switched on successfully"),
    CONNECTING("Connecting..."),
    UNABLE_TO_CONNECT("Unable to Connect. Try again later"),
    CONNECTED_SUCCESSFULLY("Connected Successfully"),
    WAITING_FOR_DEVICES("Waiting for devices...");


    private String Message;

    ToastMessage(String message) {
        Message = message;
    }
}
