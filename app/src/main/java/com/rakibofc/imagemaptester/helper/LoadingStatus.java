package com.rakibofc.imagemaptester.helper;

public enum LoadingStatus {

    START_LOADING(0),
    END_LOADING(1);

    private final int value;

    LoadingStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
