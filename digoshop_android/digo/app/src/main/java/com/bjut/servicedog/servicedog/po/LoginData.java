package com.bjut.servicedog.servicedog.po;

/**
 * Created by Xran on 16/8/2.
 */
public class LoginData {
    private UserInfo user_info;
    private Shop shop_info;

    public UserInfo getUser_info() {
        return user_info;
    }

    public void setUser_info(UserInfo user_info) {
        this.user_info = user_info;
    }

    public Shop getShop_info() {
        return shop_info;
    }

    public void setShop_info(Shop shop_info) {
        this.shop_info = shop_info;
    }
}
