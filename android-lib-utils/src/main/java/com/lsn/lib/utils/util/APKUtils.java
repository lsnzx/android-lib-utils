package com.lsn.lib.utils.util;

import static android.os.Build.VERSION_CODES.O;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import java.io.File;

/**
 * Created by Chris on 2018/3/7.
 */

public class APKUtils {
    final static private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    public static final int INSTALL_PACKAGES_REQUEST_CODE = 20;


    /**
     * 获取当前本地apk的版本
     *
     * @param mContext
     * @return
     */
    public static int getVersionCode(Context mContext) {
        int versionCode = 0;
        try {
            //获取软件版本号，对应AndroidManifest.xml下android:versionCode
            versionCode = mContext.getPackageManager().
                    getPackageInfo(mContext.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    /**
     * 获取版本号名称
     *
     * @param context 上下文
     * @return
     */
    public static String getVerName(Context context) {
        String verName = "";
        try {
            verName = context.getPackageManager().
                    getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return verName;
    }


    /**
     * 获取版本号名称
     *
     * @param context 上下文
     * @return
     */
    public static int getVerCode(Context context) {
        int verName = 0;
        try {
            verName = context.getPackageManager().
                    getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return verName;
    }


    public static String getVersionNameFromApk(Context context, String archiveFilePath) {
        String versionCode = "";
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo packInfo = pm.getPackageArchiveInfo(archiveFilePath, PackageManager.GET_ACTIVITIES);
            if (packInfo != null) {
                versionCode = packInfo.versionName;
            }
            return versionCode;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


    public static int getVersionCodeFromApk(Context context, String archiveFilePath) {
        int versionCode = 0;
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo packInfo = pm.getPackageArchiveInfo(archiveFilePath, PackageManager.GET_ACTIVITIES);
            if (packInfo != null) {
                versionCode = packInfo.versionCode;
            }
            return versionCode;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static boolean checkIsAndroidO(Context context) {
        if (Build.VERSION.SDK_INT >= O) {
            boolean b = context.getPackageManager().canRequestPackageInstalls();
            if (b) {
                return true;
            } else {
                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.REQUEST_INSTALL_PACKAGES}, INSTALL_PACKAGES_REQUEST_CODE);
                return false;
            }
        } else {
            return true;
        }
    }


    public static void installApk(Context context, File path, String authority) {

        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            // 由于没有在Activity环境下启动Activity,设置下面的标签
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //版本在7.0以上是不能直接通过uri访问的
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                File file = (new File(path.getAbsolutePath()));
                //参数1 上下文, 参数2 Provider主机地址 和配置文件中保持一致   参数3  共享的文件
                Uri apkUri = FileProvider.getUriForFile(context, authority, file);
                //添加这一句表示对目标应用临时授权该Uri所代表的文件
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
                context.startActivity(intent);
            } else {
                intent.setDataAndType(Uri.fromFile(new File(path.getAbsolutePath())),
                        "application/vnd.android.package-archive");
                context.startActivity(intent);
            }
        } catch (Exception e) {
            LogUtils.i(e.getMessage());
        }
    }
}
