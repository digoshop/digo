package com.bjut.servicedog.servicedog.po;

import java.util.List;

/**
 * Created by Xran on 16/7/21.
 */
public class AttentionList_data {
    private long max_id;
    private long min_id;
    private long target_id;
    private List<Attention> relations;

    public long getMax_id() {
        return max_id;
    }

    public void setMax_id(long max_id) {
        this.max_id = max_id;
    }

    public long getMin_id() {
        return min_id;
    }

    public void setMin_id(long min_id) {
        this.min_id = min_id;
    }

    public long getTarget_id() {
        return target_id;
    }

    public void setTarget_id(long target_id) {
        this.target_id = target_id;
    }

    public List<Attention> getRelations() {
        return relations;
    }

    public void setRelations(List<Attention> relations) {
        this.relations = relations;
    }
}
