package com.bjut.servicedog.servicedog.event;

/**
 * Created by beibeizhu on 17/3/22.
 */

public class OnRefreshEvent {

    private boolean isRefresh;

    public OnRefreshEvent(boolean isRefresh) {
        this.isRefresh = isRefresh;
    }

    public boolean isRefresh() {
        return isRefresh;
    }

    public void setRefresh(boolean refresh) {
        isRefresh = refresh;
    }
}
