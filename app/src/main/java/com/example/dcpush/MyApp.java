package com.example.dcpush;

import android.app.Application;

import com.sarlmoclen.dcpush.DCWebSocketManager;

import java.util.HashMap;

public class MyApp extends Application {

    /**
     * 例子：ws://echo.websocket.org
     * ws:不加密
     * wss:加密
     */
    private String uriContent = "ws://echo.websocket.org";

    @Override
    public void onCreate() {
        super.onCreate();
        DCWebSocketManager.getInstance()
                .init(getApplicationContext(),uriContent)
                .setHeartBeatRate(10 * 1000)
                .setHeartContent("heart")
                .setDebug(BuildConfig.DEBUG)
                .setHttpHeaders(new HashMap<String, String>());
    }

}
