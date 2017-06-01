package com.bjut.servicedog.servicedog.model;

import java.io.Serializable;

/**
 * 图片对象
 */

public class ImageItem implements Serializable {
    private static final long serialVersionUID = -7188270558443739436L;
    public String imageId;
    public String thumbnailPath;//缩略图的位置
    public String sourcePath;//资源位置
    public boolean isSelected = false;
    public String imageUrl = "";

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getThumbnailPath() {
        return thumbnailPath;
    }

    public void setThumbnailPath(String thumbnailPath) {
        this.thumbnailPath = thumbnailPath;
    }

    public String getSourcePath() {
        return sourcePath;
    }

    public void setSourcePath(String sourcePath) {
        this.sourcePath = sourcePath;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }
}
