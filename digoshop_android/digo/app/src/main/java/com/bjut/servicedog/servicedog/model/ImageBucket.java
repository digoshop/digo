package com.bjut.servicedog.servicedog.model;

import java.util.List;

/**
 * 相册对象
 *封面
 */
public class ImageBucket
{
	public int count = 0;
	public String bucketName;
	public List<ImageItem> imageList;
	public boolean selected = false;
}
