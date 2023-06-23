package com.lsn.lib.utils.util;


import android.os.Looper;
import android.text.TextUtils;

import androidx.annotation.StringRes;


/**
 * Author: Chris
 * Blog: https://www.jianshu.com/u/a3534a2292e8
 * Date: 2018/12/5
 * Description
 */
public class MToast {
    private static String oldMes = "";
    private static long timeMillis;
    private static final long DURATION = 2000;

    public static void show(String msg) {
        if (!TextUtils.isEmpty(msg)) {
            if (!msg.equals(oldMes)) {
                showToast(msg);
                timeMillis = System.currentTimeMillis();
            } else {
                if (System.currentTimeMillis() - timeMillis > DURATION) {
                    showToast(msg);
                    timeMillis = System.currentTimeMillis();
                }
            }
            oldMes = msg;
        }
    }


    public static void show(@StringRes int msg) {
        showToast(msg);
    }

    private static void showToast(String msg) {

        try {
            android.widget.Toast.makeText(Utils.getApp(), msg, android.widget.Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static void showToast(@StringRes int msg) {

        try {
            if (ThreadUtils.isMainThread()) {
                android.widget.Toast.makeText(Utils.getApp(), msg, android.widget.Toast.LENGTH_SHORT).show();
            } else {
                Looper.prepare();
                android.widget.Toast.makeText(Utils.getApp(), msg, android.widget.Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
