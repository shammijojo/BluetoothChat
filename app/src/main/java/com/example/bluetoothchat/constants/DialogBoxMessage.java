package com.example.bluetoothchat.constants;

public enum DialogBoxMessage {

    DISCONNECT_CONFIRM("Are you sure to disconnect?", "YES", "NO"),
    EXIT_CONFIRM("Are you sure to exit?", "YES", "NO"),
    SWITCH_ON_BLUETOOTH_CONFIRM("Switch On Bluetooth?", "YES", "EXIT"),
    ERROR_OCCURRED("Some error occurred!! Try again later", "EXIT", "STAY HERE"),
    CONNECTION_LOST("Connection Lost!!", "EXIT", "STAY HERE"),
    CLEAR_CHAT_HISTORY_CONFIRM("Are you sure to clear chat history?", "YES", "NO"),
    EXIT_APP("Some error occurred!!", "TRY AGAIN", "EXIT"),
    UNABLE_TO_ACCEPT("Unable to accept connections", "TRY AGAIN", "EXIT"),
    UNABLE_TO_REQUEST("Unable to request connections", "TRY AGAIN", "EXIT"),
    UNABLE_TO_CONNECT("Unable to connect to selected device", "TRY AGAIN", "EXIT"),
    DATABASE_ERROR("Data corrupted!! Please restart the application", "OK", "");


    private String message;
    private String positiveOption;
    private String negativeOption;


    DialogBoxMessage(String message, String positiveOption, String negativeOption) {
        this.message = message;
        this.positiveOption = positiveOption;
        this.negativeOption = negativeOption;
    }

    public String getMessage() {
        return message;
    }

    public String getPositiveOption() {
        return positiveOption;
    }

    public String getNegativeOption() {
        return negativeOption;
    }
}
