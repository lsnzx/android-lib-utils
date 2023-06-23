package com.lsn.lib.utils.util;


import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.AppOpsManagerCompat;
import androidx.core.content.ContextCompat;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by wangjun on 2019-10-10.
 */
public class MPermissionUtils {

    public static final String TAG = "MyPermissionUtils";

    /**
     * 检查悬浮窗权限
     *
     * @param context
     * @return
     */
    public boolean checkFloatWindowPermission(Context context) {
        //6.0 版本之后由于 google 增加了对悬浮窗权限的管理，所以方式就统一了
        if (Build.VERSION.SDK_INT < 23) {
            if (RomUtils.isXiaomi()) {
                return MiuiUtils.checkFloatWindowPermission(context);
            } else if (RomUtils.isMeizu()) {
                return MeizuUtils.checkFloatWindowPermission(context);
            } else if (RomUtils.isHuawei()) {
                return HuaweiUtils.checkFloatWindowPermission(context);
            } else if (RomUtils.is360()) {
                return QikuUtils.checkFloatWindowPermission(context);
            } else if (RomUtils.isOppo()) {
                return OppoUtils.checkFloatWindowPermission(context);
            }
        }
        return commonROMPermissionCheck(context);
    }

    public static boolean hasPermission(@NonNull Context context, @NonNull String permission) {
        List<String> permisstions = new ArrayList<>();
        permisstions.add(permission);
        return hasPermission(context, permisstions);
    }

    /**
     * 系统层的权限判断
     *
     * @param context     上下文
     * @param permissions 申请的权限 Manifest.permission.READ_CONTACTS
     * @return 是否有权限 ：其中有一个获取不了就是失败了
     */
    public static boolean hasPermission(@NonNull Context context, @NonNull List<String> permissions) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return true;
        for (String permission : permissions) {
            try {
                String op = AppOpsManagerCompat.permissionToOp(permission);
                if (TextUtils.isEmpty(op)) continue;
                int result = AppOpsManagerCompat.noteOp(context, op, android.os.Process.myUid(), context.getPackageName());
                if (result == AppOpsManagerCompat.MODE_IGNORED) return false;
                AppOpsManager appOpsManager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
                String ops = AppOpsManager.permissionToOp(permission);
                int locationOp = appOpsManager.checkOp(ops, Binder.getCallingUid(), context.getPackageName());
                if (locationOp == AppOpsManager.MODE_IGNORED) return false;
                result = ContextCompat.checkSelfPermission(context, permission);
                if (result != PackageManager.PERMISSION_GRANTED) return false;
            } catch (Exception ex) {
                Log.e(TAG, "[hasPermission] error ", ex);
            }
        }
        return true;
    }

    /**
     * 跳转到权限设置界面
     *
     * @param context
     */
    public static void toPermissionSetting(Context context) throws NoSuchFieldException, IllegalAccessException {
        if (Build.VERSION.SDK_INT < 23) {
            if (RomUtils.isXiaomi()) {
                MiuiUtils.applyMiuiPermission(context);
            } else if (RomUtils.isMeizu()) {
                MeizuUtils.applyPermission(context);
            } else if (RomUtils.isHuawei()) {
                HuaweiUtils.applyPermission(context);
            } else if (RomUtils.is360()) {
                QikuUtils.applyPermission(context);
            } else if (RomUtils.isOppo()) {
                OppoUtils.applyOppoPermission(context);
            } else {
                RomUtils.getAppDetailSettingIntent(context);
            }
        } else {
            if (RomUtils.isMeizu()) {
                MeizuUtils.applyPermission(context);
            } else {
                if (RomUtils.isOppo() || RomUtils.isVivo()
                        || RomUtils.isHuawei() || RomUtils.isSamsung()) {
                    RomUtils.getAppDetailSettingIntent(context);
                } else if (RomUtils.isXiaomi()) {
                    MiuiUtils.toPermisstionSetting(context);
                } else {
                    RomUtils.commonROMPermissionApplyInternal(context);
                }
            }
        }
    }

    private static boolean commonROMPermissionCheck(Context context) {
        //最新发现魅族6.0的系统这种方式不好用，天杀的，只有你是奇葩，没办法，单独适配一下
        if (RomUtils.isMeizu()) {
            return MeizuUtils.checkFloatWindowPermission(context);
        } else {
            Boolean result = true;
            if (Build.VERSION.SDK_INT >= 23) {
                try {
                    Class clazz = Settings.class;
                    Method canDrawOverlays = clazz.getDeclaredMethod("canDrawOverlays", Context.class);
                    result = (Boolean) canDrawOverlays.invoke(null, context);
                } catch (Exception e) {
                    Log.e(TAG, Log.getStackTraceString(e));
                }
            }
            return result;
        }
    }


    public static void goIntentSetting(Context context) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", context.getPackageName(), null);
        intent.setData(uri);
        try {
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
