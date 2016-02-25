package com.ruiyu.taozhuma.utils;


import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
/**
 * 包名检测工具
 * @author Fu
 *
 */
public class PackageUtils {

	public static boolean isApkInstalled(Context context, String packageName) {
		try {
			context.getPackageManager().getApplicationInfo(packageName,
					PackageManager.GET_UNINSTALLED_PACKAGES);
			return true;
		} catch (NameNotFoundException e) {
			return false;
		}
	}
}
