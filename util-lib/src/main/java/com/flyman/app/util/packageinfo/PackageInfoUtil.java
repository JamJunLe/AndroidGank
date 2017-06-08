package com.flyman.app.util.packageinfo;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

public class PackageInfoUtil {
    //版本名
    public static String getVersionName(Context mContext) {
        return getPackageInfo(mContext).versionName;
    }

    //版本号
    public static int getVersionCode(Context mContext) {
        return getPackageInfo(mContext).versionCode;
    }

    private static PackageInfo getPackageInfo(Context mContext) {
        PackageInfo pi = null;
        try {
            PackageManager pm = mContext.getPackageManager();
            pi = pm.getPackageInfo(mContext.getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);
            return pi;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pi;
    }
}
