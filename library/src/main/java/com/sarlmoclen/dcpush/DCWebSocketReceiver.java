package com.sarlmoclen.dcpush;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public abstract class DCWebSocketReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        DCWebSocketResult dcWebSocketResult = (DCWebSocketResult) intent.getSerializableExtra(DCWebSocketManager.EXTRA);
        if(dcWebSocketResult.getFlag() == DCWebSocketManager.OPEN){
            onOpen(context, dcWebSocketResult.getHttpStatus(), dcWebSocketResult.getHttpStatusMessage());
        }
        if(dcWebSocketResult.getFlag() == DCWebSocketManager.MESSAGE){
            onMessage(context, dcWebSocketResult.getMessage());
        }
        if(dcWebSocketResult.getFlag() == DCWebSocketManager.CLOSE){
            onClose(context, dcWebSocketResult.getCode(), dcWebSocketResult.getReason(), dcWebSocketResult.isRemote());
        }
        if(dcWebSocketResult.getFlag() == DCWebSocketManager.ERROR){
            onError(context, dcWebSocketResult.getException());
        }
    }

    protected abstract void onOpen(Context context, short httpStatus, String httpStatusMessage);

    protected abstract void onMessage(Context context, String message);

    protected abstract void onClose(Context context, int code, String reason, boolean remote);

    protected abstract void onError(Context context, Exception ex);

}
