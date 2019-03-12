package com.zankong.tool.zkapp.util;

public class MessageEvent {
    private String event;//事件
    private Object data;//数据
    private boolean valid = true;//有效
    public MessageEvent(String event,Object data){
        this.event=event;
        this.data = data;
    }

    public synchronized boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }
}
