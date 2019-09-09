package com.sarlmoclen.dcpush;

import java.io.Serializable;

public class DCWebSocketResult implements Serializable {

    private int flag;
    private String data;

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

}
