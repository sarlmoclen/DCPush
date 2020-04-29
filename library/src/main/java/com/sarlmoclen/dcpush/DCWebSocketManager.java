package com.sarlmoclen.dcpush;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Handler;
import android.os.HandlerThread;

import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DCWebSocketManager {

    public static final String TAG = "DCWebSocketManager";
    public static final String ACTION = "dc.socket.broadcast.ACTION";
    public static final String EXTRA = "dc.socket.broadcast.EXTRA";
    public static final int OPEN = 1;
    public static final int MESSAGE = 2;
    public static final int CLOSE = 3;
    public static final int ERROR = 4;
    public static final int HEART_BEAT_RATE = 5 * 1000;
    public static final String HEART_CONTENT = "HEART";
    public static Boolean DEBUG = false;
    private long heartBeatRate = HEART_BEAT_RATE;
    private Context context;
    private String heartContent = HEART_CONTENT;
    private Map<String, String> httpHeaders = new HashMap<>();
    private URI uri;
    private DCWebSocketClient dcWebSocketClient;
    private HandlerThread socketThread;
    private Handler mHandler;
    private DCRunnable dcRunnable;

    public class DCRunnable implements Runnable{

        public boolean isRunning = true;

        @Override
        public void run() {
            if(!isRunning){
                //防止主动关闭dcWebSocketClient，置为null,消息后执行,导致再次打开
                return;
            }
            if (dcWebSocketClient != null) {
                if (dcWebSocketClient.isClosed()) {
                    DCLog.log(TAG, "HEART-RECONNECTSOCKET");
                    reconnectSocket();
                    mHandler.postDelayed(this, heartBeatRate);
                    return;
                }
            } else {
                //如果client已为空，重新初始化websocket
                DCLog.log(TAG, "HEART-INIT");
                initSocket();
                mHandler.postDelayed(this, heartBeatRate);
                return;
            }
            //定时对长连接进行心跳检测
            DCLog.log(TAG, "HEART-NORMAL");
            heart();
            mHandler.postDelayed(this, heartBeatRate);
        }
    }

    private static final class Holder{
        public static final DCWebSocketManager INSTANCE = new DCWebSocketManager();
    }

    public static DCWebSocketManager getInstance(){
        return Holder.INSTANCE;
    }

    DCWebSocketManager(){
        socketThread = new HandlerThread(TAG);
        socketThread.start();
        mHandler = new Handler(socketThread.getLooper());
    }

    public DCWebSocketManager init(Context context, String uriContent){
        this.context = context;
        uri = URI.create(uriContent);
        return this;
    }

    public DCWebSocketManager setHeartContent(String heartContent){
        this.heartContent = heartContent;
        return this;
    }

    public DCWebSocketManager setHeartBeatRate(long heartBeatRate){
        if(heartBeatRate < HEART_BEAT_RATE){
            this.heartBeatRate = HEART_BEAT_RATE;
        } else {
            this.heartBeatRate = heartBeatRate;
        }
        return this;
    }

    public DCWebSocketManager setHttpHeaders(Map<String, String> httpHeaders){
        this.httpHeaders = httpHeaders;
        return this;
    }

    public DCWebSocketManager setDebug(boolean isDebug){
        this.DEBUG = isDebug;
        return this;
    }

    private void reconnectSocket() {
        try {
            //重连
            dcWebSocketClient.reconnectBlocking();
        } catch (InterruptedException e) {
            e.printStackTrace();
            DCLog.log(TAG, "reconnectSocket()-InterruptedException:" + e.toString());
        }
    }

    private void initSocket(){
        if(uri == null){
            DCLog.log(TAG, "please init websocket!");
            return;
        }
        dcWebSocketClient = new DCWebSocketClient(uri, httpHeaders) {

            @Override
            public void onMessage(String message) {
                super.onMessage(message);
                DCWebSocketResult dcWebSocketResult = new DCWebSocketResult();
                dcWebSocketResult.setFlag(MESSAGE);
                dcWebSocketResult.setMessage(message);
                sendBroadcast(dcWebSocketResult);
            }

            @Override
            public void onOpen(ServerHandshake handshakedata) {
                super.onOpen(handshakedata);
                DCWebSocketResult dcWebSocketResult = new DCWebSocketResult();
                dcWebSocketResult.setFlag(OPEN);
                dcWebSocketResult.setHttpStatus(handshakedata.getHttpStatus());
                dcWebSocketResult.setHttpStatusMessage(handshakedata.getHttpStatusMessage());
                sendBroadcast(dcWebSocketResult);
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
                super.onClose(code, reason, remote);
                DCWebSocketResult dcWebSocketResult = new DCWebSocketResult();
                dcWebSocketResult.setFlag(CLOSE);
                dcWebSocketResult.setCode(code);
                dcWebSocketResult.setReason(reason);
                dcWebSocketResult.setRemote(remote);
                sendBroadcast(dcWebSocketResult);
            }

            @Override
            public void onError(Exception ex) {
                super.onError(ex);
                DCWebSocketResult dcWebSocketResult = new DCWebSocketResult();
                dcWebSocketResult.setFlag(ERROR);
                dcWebSocketResult.setException(ex);
                sendBroadcast(dcWebSocketResult);
            }
        };
        try {
            dcWebSocketClient.connectBlocking();
        } catch (InterruptedException e) {
            e.printStackTrace();
            DCLog.log(TAG, "startSocket()-InterruptedException:" + e.toString());
        }
    }

    public void startSocket(){
        //开启心跳检测
        if(dcRunnable != null && dcRunnable.isRunning){
            return;
        }
        dcRunnable = new DCRunnable();
        mHandler.post(dcRunnable);
    }

    private void heart(){
        sendMessage(heartContent);
    }

    private void sendBroadcast(DCWebSocketResult dcWebSocketResult){
        try {
            Intent intent = new Intent();
            intent.putExtra(EXTRA,dcWebSocketResult);
            intent.setAction(ACTION);
            String packageName = context.getPackageName();
            List<ResolveInfo> receivers = context.getPackageManager().queryBroadcastReceivers(new Intent(ACTION), 0);
            for (ResolveInfo resolveInfo : receivers){
                String pName = resolveInfo.activityInfo.packageName;
                String name = resolveInfo.activityInfo.name;
                if(packageName.equals(pName)){
                    intent.setComponent(new ComponentName(pName,name));
                    context.sendBroadcast(intent);
                    break;
                }
            }
        }catch (Exception e){
        }
    }

    public void closeSocket(){
        try {
            if (null != dcWebSocketClient) {
                dcWebSocketClient.close();
                mHandler.removeCallbacksAndMessages(null);
                if(dcRunnable != null){
                    dcRunnable.isRunning = false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            DCLog.log(TAG, "closeSocket()-Exception:" + e.toString());
        } finally {
            dcWebSocketClient = null;
        }
    }

    public void sendMessage(String message){
        if (dcWebSocketClient != null && dcWebSocketClient.isOpen()) {
            try {
                dcWebSocketClient.send(message);
            }catch (Exception e){
                DCLog.log(TAG, e.toString());
            }
        } else {
            DCLog.log(TAG, "sendMessage()-fail");
        }
    }

    public DCWebSocketClient getDcWebSocketClient(){
        return dcWebSocketClient;
    }

}
