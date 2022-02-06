package com.example.bluetoothchat.constants;

public enum ToastMessage {

     BLUETOOTH_SWITCHED_ON("Bluetooth switched on successfully"),
     CONNECTING("Connecting..."),
     UNABLE_TO_CONNECT("Unable to Connect. Try again later\nMake sure bluetooth is switched on"),
     CONNECTED_SUCCESSFULLY("Connected Successfully"),
     WAITING_FOR_DEVICES("Waiting for devices...");

     private final String Message;

     ToastMessage(String message) {
          Message = message;
     }

     public String getMessage() {
          return Message;
     }
}
