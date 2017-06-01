package com.bjut.servicedog.servicedog.utils;

import java.util.List;

/**
 * Created by Xran on 16/7/18.
 */
public interface AsyncResponse {
    void onDataReceivedSuccess(String url);
    void onDataReceivedFailed();
}
