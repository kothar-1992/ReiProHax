package com.happy.pro.Component;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.os.Handler;
import android.os.IBinder;
import android.content.Intent;
import com.blankj.molihuan.utilcode.util.ToastUtils;
import com.happy.pro.R;
import com.happy.pro.utils.FLog;

import java.util.Objects;


public class MainService extends Service {
    private static MainService instance;
    public static boolean isRunning = false;
    private static Context context;
    private static String packageName;

    static {
        try {
            System.loadLibrary("client");
        } catch(UnsatisfiedLinkError w) {
            FLog.error(w.getMessage());
        }
    }
    
    public static native String InitBase();
    public static native void closeSocket();
    
    public static MainService get() {
    	return instance;
    }
    
    public static void startService(Context context, String packageName) {
        MainService.context = context;
        MainService.packageName = packageName;
    }
    
    public static void stopService() {
    	if (instance != null) {
            instance.onDestroy();
        }
    }
    
    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }
    
    @Override
    public void onCreate() {
        instance = this;
        super.onCreate();
        
        try {
            if (!isRunning) {
                RunServer();
                isRunning = true;
            }
        } catch(Exception err) {
        	FLog.error(err.getMessage());
        }
    }
    
    private static void RunServer() {
    	try {
    	    new Handler().postDelayed(() -> {
                String respon = InitBase();
                if (respon.equalsIgnoreCase("Server Accept")) {
                    toast(R.drawable.ic_check, "Server Connected");
                } else {
                    toast(R.drawable.ic_error, respon);//"Error Server No Connected, Please restart.");
                    stopService();
                }
            }, 10 * 1000);
    	} catch(Exception err) {
    		FLog.error(Objects.requireNonNull(err.getCause()).getMessage());
            stopService();
    	}
    }
    
    @Override
    public void onDestroy() {
        closeSocket();
        isRunning = false;
        stopSelf();
        instance = null;
        super.onDestroy();
    }
    
    @SuppressLint("ResourceAsColor")
    private static void toast(int id, CharSequence msg) {
        ToastUtils _toast = ToastUtils.make();
        _toast.setBgColor(android.R.color.white);
        _toast.setLeftIcon(id);
        _toast.setTextColor(android.R.color.black);
        _toast.setNotUseSystemToast();
        _toast.show(msg);
    }
    
    
}
