package com.sarlmoclen.dcpush;

import java.io.Serializable;

public class DCWebSocketResult implements Serializable {

    private int flag;

    /**onMessage*/
    private String message;

    /**onOpen*/
    private short httpStatus;
    /**onOpen*/
    private String httpStatusMessage;

    /**onClose*/
    private int code;
    /**onClose*/
    private String reason;
    /**onClose*/
    private boolean remote;

    /**onError*/
    private Exception exception;

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public short getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(short httpStatus) {
        this.httpStatus = httpStatus;
    }

    public String getHttpStatusMessage() {
        return httpStatusMessage;
    }

    public void setHttpStatusMessage(String httpStatusMessage) {
        this.httpStatusMessage = httpStatusMessage;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public boolean isRemote() {
        return remote;
    }

    public void setRemote(boolean remote) {
        this.remote = remote;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

}
