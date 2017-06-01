package com.bjut.servicedog.servicedog.event;

/**
 * Created by beibeizhu on 17/1/17.
 */

public class CloseEvent {
    private boolean isClose;

    public CloseEvent(boolean isClose) {
        this.isClose = isClose;
    }

    public boolean isClose() {
        return isClose;
    }

    public void setClose(boolean close) {
        isClose = close;
    }
}
