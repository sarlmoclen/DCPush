package com.example.dcpush.demo;


import android.content.Context;
import android.content.Intent;

import com.sarlmoclen.dcpush.DCLog;
import com.sarlmoclen.dcpush.DCWebSocketReceiver;

import static com.sarlmoclen.dcpush.DCWebSocketManager.TAG;

public class DCReceiver extends DCWebSocketReceiver {

    @Override
    protected void onOpen(Context context, short httpStatus, String httpStatusMessage) {
        DCLog.log(TAG, "onOpen()-httpStatus:" + httpStatus + "-httpStatusMessage:" + httpStatusMessage);
        send(context, "onOpen()-httpStatus:" + httpStatus + "-httpStatusMessage:" + httpStatusMessage);
    }

    @Override
    protected void onMessage(Context context, String message) {
        DCLog.log(TAG, "onMessage()-message:" + message);
        send(context, "onMessage()-message:" + message);
    }

    @Override
    protected void onClose(Context context, int code, String reason, boolean remote) {
        DCLog.log(TAG, "onClose()-code:" + code + "-reason:" + reason + "-remote:" + remote);
        send(context, "onClose()-code:" + code + "-reason:" + reason + "-remote:" + remote);
    }

    @Override
    protected void onError(Context context, Exception ex) {
        DCLog.log(TAG, "onError()-ex:" + ex.toString());
        send(context, "onError()-ex:" + ex.toString());
    }

    private void send(Context context, String content){
        Intent intent = new Intent();
        intent.putExtra("content",content);
        intent.setAction("test.action");
        context.sendBroadcast(intent);
    }
}
