package com.example.smsforwarder.listener;

public interface SmsCatchListener<T> {
    void onSmsCatch(String message, String receiver);
}
