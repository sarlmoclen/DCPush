package com.example.dcpush.demo;


import android.content.Context;
import android.content.Intent;

import com.sarlmoclen.dcpush.DCLog;
import com.sarlmoclen.dcpush.DCWebSocketReceiver;

import static com.sarlmoclen.dcpush.DCWebSocketManager.TAG;

public class DCReceiver extends DCWebSocketReceiver {

    @Override
    protected void onOpen(Context context, String data) {
        DCLog.log(TAG, "onOpen()-data:" + data);
        send(context, "onOpen()-data:" + data);
    }

    @Override
    protected void onMessage(Context context, String data) {
        DCLog.log(TAG, "onMessage()-data:" + data);
        send(context, "onMessage()-data:" + data);
    }

    @Override
    protected void onClose(Context context, String data) {
        DCLog.log(TAG, "onClose()-data:" + data);
        send(context, "onClose()-data:" + data);
    }

    @Override
    protected void onError(Context context, String data) {
        DCLog.log(TAG, "onError()-data:" + data);
        send(context, "onError()-data:" + data);
    }

    private void send(Context context, String content){
        Intent intent = new Intent();
        intent.putExtra("content",content);
        intent.setAction("test.action");
        context.sendBroadcast(intent);
    }
}
