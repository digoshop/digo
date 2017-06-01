package com.bjut.servicedog.servicedog.po;

/**
 * Created by Xran on 16/9/13.
 */
public class UpdateInfo {
    private String down_url;
    private String new_version;
    private boolean update;
    private String target_size;
    private String u_log;
    private String type;
    private String new_md5;

//    public  String getFileSize() {
//        DecimalFormat df = new DecimalFormat("#.00");
//        String fileSizeString = "";
//        String wrongSize="0B";
//        if(target_size==0){
//            return wrongSize;
//        }
//        if (target_size < 1024){
//            fileSizeString = df.format((double) target_size) + "B";
//        }
//        else if (target_size < 1048576){
//            fileSizeString = df.format((double) target_size / 1024) + "KB";
//        }
//        else if (target_size < 1073741824){
//            fileSizeString = df.format((double) target_size / 1048576) + "MB";
//        }
//        else{
//            fileSizeString = df.format((double) target_size / 1073741824) + "GB";
//        }
//        return fileSizeString;
//    }
    public String getDown_url() {
        return down_url;
    }

    public void setDown_url(String down_url) {
        this.down_url = down_url;
    }

    public String getNew_version() {
        return new_version;
    }

    public void setNew_version(String new_version) {
        this.new_version = new_version;
    }

    public boolean isUpdate() {
        return update;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }

    public String getTarget_size() {
        return target_size;
    }

    public void setTarget_size(String target_size) {
        this.target_size = target_size;
    }

    public String getU_log() {
        return u_log;
    }

    public void setU_log(String u_log) {
        this.u_log = u_log;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNew_md5() {
        return new_md5;
    }

    public void setNew_md5(String new_md5) {
        this.new_md5 = new_md5;
    }
}
