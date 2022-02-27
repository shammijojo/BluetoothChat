package com.example.bluetoothchat.constants;

public enum MenuItemOptions {

    DISCONNECT("Disconnect"),
    EXIT("Exit"),
    CLEAR_CHAT_HISTORY("Clear Chat History"),
    CREDITS("Credits");

    private String option;

    MenuItemOptions(String option) {
        this.option = option;
    }

    public String getOption() {
        return option;
    }
}
