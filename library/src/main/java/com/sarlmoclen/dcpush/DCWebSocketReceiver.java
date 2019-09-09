package com.sarlmoclen.dcpush;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public abstract class DCWebSocketReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        DCWebSocketResult dcWebSocketResult = (DCWebSocketResult) intent.getSerializableExtra(DCWebSocketManager.EXTRA);
        if(dcWebSocketResult.getFlag() == DCWebSocketManager.OPEN){
            onOpen(context, dcWebSocketResult.getData());
        }
        if(dcWebSocketResult.getFlag() == DCWebSocketManager.MESSAGE){
            onMessage(context, dcWebSocketResult.getData());
        }
        if(dcWebSocketResult.getFlag() == DCWebSocketManager.CLOSE){
            onClose(context, dcWebSocketResult.getData());
        }
        if(dcWebSocketResult.getFlag() == DCWebSocketManager.ERROR){
            onError(context, dcWebSocketResult.getData());
        }
    }

    protected abstract void onOpen(Context context, String data);

    protected abstract void onMessage(Context context, String data);

    protected abstract void onClose(Context context, String data);

    protected abstract void onError(Context context, String data);

}
