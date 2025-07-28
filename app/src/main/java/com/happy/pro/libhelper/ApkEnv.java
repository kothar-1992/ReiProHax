package com.happy.pro.libhelper;


import android.annotation.SuppressLint;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;

import com.blankj.molihuan.utilcode.util.FileUtils;
import net_62v.external.MetaApplicationInstaller;
import net_62v.external.MetaActivityManager;
import net_62v.external.MetaPackageManager;
import net_62v.external.MetaStorageManager;

import com.happy.pro.BoxApplication;
import com.happy.pro.R;
import com.happy.pro.utils.FLog;

import java.io.File;

public class ApkEnv {
    File obbContaine;

    private static ApkEnv singleton;

    public static ApkEnv getInstance() {
        if (singleton == null) {
            singleton = new ApkEnv();
        }
        return singleton;
    }

    public ApplicationInfo getApplicationInfo(String packageName) {
        ApplicationInfo applicationInfo = null;
        try {
        	applicationInfo = BoxApplication.get().getPackageManager().getApplicationInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException err) {
        	FLog.error(err.getMessage());
            BoxApplication.get().showToastWithImage(R.drawable.ic_error, err.getMessage());
            return null;
        }

        /*if (!AbiUtils.isSupport(new File(applicationInfo.sourceDir))) {
            BoxApplication.getInstance().showToastWithImage(R.drawable.ic_error, "Please Install Game " + (FCore.is64Bit() ? "64Bit" : "32Bit") + " version.");
            return null;
        }*/

        return applicationInfo;
    }

    public ApplicationInfo getApplicationInfoContainer(String packageName) {
    	if (!isInstalled(packageName)) {
            BoxApplication.get().showToastWithImage(R.drawable.ic_error, "App not install, install first");
            return null;
        }

        ApplicationInfo applicationInfo = null;
        try {
            applicationInfo = MetaPackageManager.getApplicationInfo(packageName);
        } catch (Exception e) {
            FLog.error("MetaPackageManager.getApplicationInfo failed: " + e.getMessage());
            // Fallback to regular PackageManager if Meta fails
            try {
                applicationInfo = getApplicationInfo(packageName);
            } catch (Exception fallbackException) {
                FLog.error("Fallback getApplicationInfo also failed: " + fallbackException.getMessage());
                return null;
            }
        }
        if (applicationInfo == null) {
            FLog.error("ApplicationInfo is null for package: " + packageName);
            return null;
        }
        return applicationInfo;
    }

    public boolean isInstalled(String packageName) {
        try {
            return MetaPackageManager.isInnerAppInstalled(packageName);
        } catch (Exception e) {
            FLog.error("MetaPackageManager.isInnerAppInstalled failed for " + packageName + ": " + e.getMessage());
            // Fallback to regular PackageManager check
            try {
                ApplicationInfo info = getApplicationInfo(packageName);
                return info != null;
            } catch (Exception fallbackException) {
                FLog.error("Fallback package check also failed: " + fallbackException.getMessage());
                return false;
            }
        }
    }

    public boolean isRunning(String packageName) {
    	try {
            return MetaActivityManager.isAppRunning(packageName, 0);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @SuppressLint("SuspiciousIndentation")
    public boolean installByFile(String packageName) {
        ApplicationInfo applicationInfo = getApplicationInfo(packageName);
        if (applicationInfo == null) {
            return false;
        }
    	try {
            MetaApplicationInstaller.installAppByPath(applicationInfo.sourceDir);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    public boolean installByPackage(String packageName) {
    	try {
            MetaApplicationInstaller.cloneApp(packageName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    public void unInstallApp(String packageName) {
    	try {
            MetaPackageManager.uninstallAppFully(packageName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void stopRunningApp(String packageName) {
    	try {
            MetaActivityManager.killAppByPkg(packageName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public File getObbContainerPath(String packageName) {
    	try {
            return new File(MetaStorageManager.obtainAppExternalStorageDir(packageName) + "/Android/obb", packageName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean tryAddLoader(String packageName) {
        boolean is_online = true;

        ApplicationInfo applicationInfo = getApplicationInfoContainer(packageName);
        if (applicationInfo == null) {
            FLog.error("Error, Application Info - Failed to get application info for package: " + packageName);
            BoxApplication.get().showToastWithImage(R.drawable.ic_error, "Failed to get app info for " + packageName);
            return false;
        }

        File loader = getFile(packageName, is_online);
        File loaderDest = new File(applicationInfo.nativeLibraryDir, packageName.equals("com.miraclegames.farlight84") ? "libfarlight.so" : "libAkAudioVisiual.so");

        if (loaderDest.exists()) loaderDest.delete();
        try {
        	if (FileUtils.copy(loader.toString(), loaderDest.toString())) {
                return true;
            }
        } catch(Exception err) {
        	FLog.error(err.getMessage());
            return false;
        }
        return false;
    }

    @NonNull
    private static File getFile(String packageName, boolean is_online) {
        String target;

        if (packageName.equals("com.miraclegames.farlight84")) {
            target = "libfarlight.so";
        } else if (packageName.equals("com.pubg.krmobile")) {
            target = "libSdk.so";
        }else if (packageName.equals("com.pubg.imobile")) {
            target = "libbgmi.so";
        }else{
            target = "libpubgm.so";
        }

        return new File(is_online ? new File(BoxApplication.get().getFilesDir(), "loader").toString() : BoxApplication.get().getApplicationInfo().nativeLibraryDir, target);
    }

    public void launchApk(String packageName) {
        if (!isInstalled(packageName)) {
            BoxApplication.get().showToastWithImage(R.drawable.icon, "Client not installed");
            return;
        }
        try {
            MetaActivityManager.launchApp(packageName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
