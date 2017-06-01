package com.bjut.servicedog.servicedog.utils.upload;

/**
 * Created by beibeizhu on 17/4/10.
 */

public interface OnUploadListener {
    void onSuccess(String result);
    void onFailure(String msg);
}
