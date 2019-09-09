package com.sarlmoclen.dcpush;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.Map;

public class DCWebSocketClient extends WebSocketClient {

    public static final String TAG = "DCWebSocketClient";

    public DCWebSocketClient(URI serverUri) {
        super(serverUri);
    }

    public DCWebSocketClient(URI serverUri, Draft protocolDraft) {
        super(serverUri, protocolDraft);
    }

    public DCWebSocketClient(URI serverUri, Map<String, String> httpHeaders) {
        super(serverUri, httpHeaders);
    }

    public DCWebSocketClient(URI serverUri, Draft protocolDraft, Map<String, String> httpHeaders) {
        super(serverUri, protocolDraft, httpHeaders);
    }

    public DCWebSocketClient(URI serverUri, Draft protocolDraft, Map<String, String> httpHeaders, int connectTimeout) {
        super(serverUri, protocolDraft, httpHeaders, connectTimeout);
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        DCLog.log(TAG,"onOpen()-status:" + handshakedata.getHttpStatusMessage());
    }

    @Override
    public void onMessage(String message) {
        DCLog.log(TAG,"onMessage()-message:" + message);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        DCLog.log(TAG,"onClose()-code:" + code + "&reason:" + reason + "&remote:" + remote);
    }

    @Override
    public void onError(Exception ex) {
        DCLog.log(TAG,"onError()-Exception:" + ex.toString());
    }

}
