package com.bjut.servicedog.servicedog.po;

/**
 * Created by beibeizhu on 16/11/25.
 */

public class CommitEvent {

    private boolean isCommitOK;

    public CommitEvent(boolean isCommitOK) {
        this.isCommitOK = isCommitOK;
    }

    public boolean isCommitOK() {
        return isCommitOK;
    }

    public CommitEvent setCommitOK(boolean commitOK) {
        isCommitOK = commitOK;
        return this;
    }
}
