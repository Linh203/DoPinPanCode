package com.example.dopinpan.Common;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.dopinpan.Model.Request;
import com.example.dopinpan.Model.User;

public class Common {
    public static User currentUser;


    public static final String USER_KEY = "User";
    public static final String PWD_KEY = "Password";
    public static final int PICK_IMAGE_REQUEST = 71;
    public static Request currentRequest;
    public static final String UPDATE = "Update";
    public static final String DELETE = "Delete";

    public static final String PHONE_TEXT = "userPhone";


    public static String convertCodeToStatus(String status) {
        if (status.equals("0"))
            return "Đơn Hàng Đang Được Chuẩn Bị !";
        else if (status.equals("1"))
            return "Đơn Hàng Đang Được Giao !";
        else if (status.equals("2"))
            return "Đơn Hàng Đã Bị Hủy !";
        else
            return "Đơn Hàng Được Giao Thành Công !";

    }

    public static boolean isConectedInternet(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
                        return true;
                }
            }
        }
        return false;
    }

}
