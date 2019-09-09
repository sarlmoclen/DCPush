package com.sarlmoclen.dcpush;

import android.util.Log;

public class DCLog {

    public static void log(String tag,  String msg){
        if(!DCWebSocketManager.DEBUG){
            return;
        }
        Log.i(tag,msg);
    }

}
