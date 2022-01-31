package com.example.bluetoothchat.constants;

public enum DialogBoxMessage {

    EXIT_CONFIRM("Are you sure to exit?", "YES", "NO"),
    SWITCH_ON_BLUETOOTH_CONFIRM("Switch On Bluetooth?", "YES", "EXIT"),
    ERROR_OCCURRED("Some error occurred!! Try again later", "EXIT", "STAY HERE"),
    CONNECTION_LOST("Connection Lost!!", "EXIT", "STAY HERE"),
    CLEAR_CHAT_HISTORY_CONFIRM("Are you sure to clear chat history?", "YES", "NO");


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
