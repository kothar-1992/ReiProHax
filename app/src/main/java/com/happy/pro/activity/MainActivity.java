package com.happy.pro.activity;

import static com.happy.pro.Config.GAME_LIST_ICON;
import static com.happy.pro.activity.ModeActivity.Kooontoool;
import static com.happy.pro.activity.SplashActivity.mahyong;
import static com.happy.pro.server.ApiServer.EXP;
import static com.happy.pro.server.ApiServer.FixCrash;
import static com.happy.pro.server.ApiServer.mainURL;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.happy.pro.Component.DownloadZip;
import com.happy.pro.Component.Downtwo;
import com.happy.pro.R;
import com.happy.pro.adapter.RecyclerViewAdapter;
import com.happy.pro.floating.FloatRei;
import com.happy.pro.floating.FloatService;
import com.happy.pro.floating.Overlay;
import com.happy.pro.floating.ToggleAim;
import com.happy.pro.floating.ToggleBullet;
import com.happy.pro.floating.ToggleSimulation;
import com.happy.pro.libhelper.ApkEnv;
import com.happy.pro.utils.ActivityCompat;
import com.happy.pro.utils.FLog;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener;
import com.skydoves.powerspinner.PowerSpinnerView;
import com.suke.widget.SwitchButton;
import com.topjohnwu.superuser.Shell;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


/**************************
 * BUILD ON Android Studio
 * TELEGRAM : OxZeroo
 * *************************/


public class MainActivity extends ActivityCompat {

    private static final int BUFFER_SIZE = 0;
    public static String socket;
    public static String daemonPath;
    public static boolean fixinstallint = false;
    public static boolean check = false;
    public static int hiderecord = 0;
    public static int skin = 0;
    static MainActivity instance;

    static {
        try {
            System.loadLibrary("client");
        } catch(UnsatisfiedLinkError w) {
            FLog.error(w.getMessage());
        }
    }

    private PowerSpinnerView powerSpinnerView;
    private static final int REQUEST_PERMISSIONS = 1;
    private static final String PREF_NAME = "espValue";
    private SharedPreferences sharedPreferences;
    String[] packageapp = {"com.tencent.ig", "com.pubg.krmobile", "com.vng.pubgmobile", "com.rekoo.pubgm","com.pubg.imobile"};
    public String nameGame = "PROTECTION GLOBAL";
    public String CURRENT_PACKAGE = "";
    public LinearProgressIndicator progres;
    public CardView enable, disable;
    public static int gameint = 1;
    public static int bitversi = 64;
    public static boolean noroot = false;
    public static int device = 1;
    public static String game = "com.tencent.ig";
    public static String BASEESP;
    TextView root;
    public static int checkesp;
    public static boolean kernel = false;
    public static boolean Ischeck = false;
    public LinearLayout container;
    public static String modeselect;
    public static String typelogin;
    Context ctx;
    private boolean system;

    public MainActivity(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public static MainActivity get() {
        return instance;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        Thread.setDefaultUncaughtExceptionHandler(new CrashHandler(this));
        init();
        initMenu1();
        initMenu2();
        initMenu3();
        Loadssets();
        devicecheck();
        if (!mahyong){
            finish();
            finishActivity(1);
        }
        instance = this;
        isLogin = true;
        Checking();
    }

    public void devicecheck(){
        root = findViewById(R.id.textroot);
        container = findViewById(R.id.container);
        LinearLayout menuroot = findViewById(R.id.menuantiban);

        if (Shell.rootAccess()){
            FLog.info("Root granted");
            modeselect = "ROOT -" + "ANDROID " + Build.VERSION.RELEASE;
            root.setText(getString(R.string.root) );
            container.setVisibility(View.GONE);
            menuroot.setVisibility(View.VISIBLE);
            Ischeck = true;
            noroot = true;
            device = 1;
        } else {
            FLog.info("Root not granted");
            modeselect = "CONTAINER -" + "ANDROID " + Build.VERSION.RELEASE;
            root.setText(getString(R.string.notooroot));
            doInitRecycler();
            container.setVisibility(View.VISIBLE);
            menuroot.setVisibility(View.GONE);
            Ischeck = false;
            device = 2;
        }
    }

    public void Checking(){
        File newFile = new File(getFilesDir().toString() + "/TW");
        if (newFile.exists()) {
            } else {
                new DownloadZip(this).execute("1", mainURL());
        }
    }
    @SuppressLint("SetTextI18n")
    void initMenu1(){
        ImageView start = findViewById(R.id.startmenu);
        ImageView stop =  findViewById(R.id.stopmenu);
        LinearLayout global =  findViewById(R.id.global);
        LinearLayout korea =  findViewById(R.id.korea);
        LinearLayout vietnam =  findViewById(R.id.vietnam);
        LinearLayout taiwan =  findViewById(R.id.taiwan);
        LinearLayout india =  findViewById(R.id.india);
        LinearLayout layoutprtc =  findViewById(R.id.layoutprtc);
        LinearLayout menuselectesp =  findViewById(R.id.menuselectesp);
        SwitchButton protection =  findViewById(R.id.protection);
        TextView textsstart =  findViewById(R.id.textsstart);
        TextView textversions =  findViewById(R.id.textversions1);
        TextView textroot =  findViewById(R.id.texttag);
        ImageView imgs1 =  findViewById(R.id.imgs1);
        RadioGroup modesp = findViewById(R.id.groupmode);
        RadioGroup espmode = findViewById(R.id.groupesp);

        if (!Shell.rootAccess()){
            menuselectesp.setVisibility(View.GONE);
        }else{
            menuselectesp.setVisibility(View.VISIBLE);
        }

        if (!Kooontoool){
            imgs1.setBackgroundResource(R.drawable.baseline_lock_24);
            layoutprtc.setAlpha(0.6f);
            protection.setEnabled(false);
            typelogin = "BearMod Time limit";
        }else{
            typelogin = "VIP";
            protection.setOnCheckedChangeListener((view, isChecked) -> {
                for (String packageName : packageapp) {
                    boolean isInstalled = isAppInstalled(MainActivity.get(), packageName);
                    if (!isInstalled) {
                    } else {
                        launchbypass();
                    }
                }
            });
        }

        espmode.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.esp64) {
                bitversi = 64;
            } else if (checkedId == R.id.esp32) {
                bitversi = 32;
            }
        });

        // Replaced switch with if-else if for RadioGroup modesp
        modesp.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.system) {
                kernel = false;
                checkesp = 1;
                espmode.setEnabled(true);
            } else if (checkedId == R.id.kernel) {
                kernel = true;
                checkesp = 2;
                espmode.check(R.id.esp64); // Assuming esp64 is the default for kernel mode
                espmode.setEnabled(false);
            }
        });



        start.setOnClickListener(v -> {
            stop.setVisibility(View.VISIBLE);
            start.setVisibility(View.GONE);
            toastImage(R.drawable.success_250px,getString(R.string.start_floating_success));
            textsstart.setText(R.string.activity_mode_textsstop_text);
            startPatcher();
        });

        stop.setOnClickListener(v -> {
            stop.setVisibility(View.GONE);
            start.setVisibility(View.VISIBLE);
            stopPatcher();
            textsstart.setText(R.string.activity_mode_textsstart_text);
            toastImage(R.drawable.ic_error,getString(R.string.stop_floating_success));

        });

        global.setOnClickListener(v -> {
            gameversion(global,korea,vietnam,taiwan,india);
            gameint = 1;
            game = packageapp[0];
            nameGame = getString(R.string.protection_global);
            textversions.setText(nameGame);
            imgs1.setBackgroundResource(R.drawable.circlegl);
            toastImage(R.drawable.circlegl,getString(R.string.global_selected));
        });

        korea.setOnClickListener(v -> {
            gameversion(korea,global,vietnam,taiwan,india);
            gameint = 2;
            game = packageapp[1];
            nameGame = getString(R.string.protection_korea);
            textversions.setText(nameGame);
            imgs1.setBackgroundResource(R.drawable.krcircle);
            toastImage(R.drawable.krcircle,getString(R.string.korea_selected));
        });

        vietnam.setOnClickListener(v -> {
            gameversion(vietnam,korea,global,taiwan,india);
            gameint = 3;
            game = packageapp[2];
            nameGame = getString(R.string.protection_vietnam);
            textversions.setText(nameGame);
            imgs1.setBackgroundResource(R.drawable.circlevn);
            toastImage(R.drawable.circlevn,getString(R.string.vietnam_selected));
        });

        taiwan.setOnClickListener(v -> {
            gameversion(taiwan,korea,vietnam,global,india);
            gameint = 4;
            game = packageapp[3];
            nameGame = getString(R.string.protection_taiwan);
            textversions.setText(nameGame);
            imgs1.setBackgroundResource(R.drawable.circletw);
            toastImage(R.drawable.circletw,getString(R.string.taiwan_selected));
        });

        india.setOnClickListener(v -> {
            gameversion(india,korea,vietnam,taiwan,global);
            gameint = 5;
            game = packageapp[4];
            nameGame = getString(R.string.protection_india);
            textversions.setText(nameGame);
            imgs1.setBackgroundResource(R.drawable.circlebgmi);
            toastImage(R.drawable.circlebgmi,getString(R.string.india_selected));
        });


    }

    @SuppressLint({"ResourceAsColor", "SetTextI18n", "UseCompatLoadingForDrawables"})
    void initMenu2(){
        TextView device = findViewById(R.id.device);
        TextView android = findViewById(R.id.android);
        TextView leveluser = findViewById(R.id.leveluser);
        LinearLayout updatesresource = findViewById(R.id.updatesresource);
        LinearLayout layoutother = findViewById(R.id.wkkw);

        device.setText(Build.VERSION.RELEASE);
        android.setText(Build.DEVICE);

        if (Kooontoool){
            leveluser.setText("VIP");
        }else{
            leveluser.setText("Silver");
            findViewById(R.id.fixinstall).setAlpha(0.5f);
            layoutother.setAlpha(0.5f);

        }

        updatesresource.setOnClickListener(v -> {
            showBottomSheetDialog2(getResources().getDrawable(R.drawable.icon_toast_alert), getString(R.string.confirm), getString(R.string.you_want_update_resource_to_latest_version), false, sv -> {
                new DownloadZip(this).execute("1", mainURL());
                dismissBottomSheetDialog();
            }, v1 -> {
                checkAndDeleteFile(MainActivity.get());
                }, v2 ->{
                dismissBottomSheetDialog();
            });
        });

        //TODO : TWITTER
        findViewById(R.id.twitter).setOnClickListener(v -> {
            if (Kooontoool){
                doShowProgress(true);
                addAdditionalApp(false, "com.twitter.android");
            }else{
                toastImage(R.drawable.notife,"Please Upgrade to VIP");
            }

        });

        findViewById(R.id.twitter).setOnLongClickListener(v -> {
            if (Kooontoool){
                showBottomSheetDialog(getResources().getDrawable(R.drawable.icon_toast_alert), getString(R.string.confirm), getString(R.string.want_remove_it), false, sv -> {
                    ApkEnv.getInstance().unInstallApp("com.twitter.android");
                    dismissBottomSheetDialog();
                }, v1 -> {
                    dismissBottomSheetDialog();
                });
            }else{
                toastImage(R.drawable.notife,"Please Upgrade to VIP");
            }
            return true;
        });

        //TODO : FACEBOOK
        findViewById(R.id.facebook).setOnClickListener(v -> {
            if (Kooontoool){
                doShowProgress(true);
                addAdditionalApp(false, "mark.via.gp");
            }else{
                toastImage(R.drawable.notife,"Please Upgrade to VIP");
            }
        });

        findViewById(R.id.facebook).setOnLongClickListener(v -> {
            if (Kooontoool){
                showBottomSheetDialog(getResources().getDrawable(R.drawable.icon_toast_alert), getString(R.string.confirm), getString(R.string.want_remove_it), false, sv -> {
                    ApkEnv.getInstance().unInstallApp("mark.via.gp");
                    dismissBottomSheetDialog();
                }, v1 -> {
                    dismissBottomSheetDialog();
                });
            }else{
                toastImage(R.drawable.notife,"Please Upgrade to VIP");
            }
            return true;
        });

        // TODO : Game Guardian
        findViewById(R.id.gg).setOnClickListener(v -> {
            if (Kooontoool){
                doShowProgress(true);
                addAdditionalApp(false, "com.morocco.invincible.gg");
            }else{
                toastImage(R.drawable.notife,"Please Upgrade to VIP");
            }
        });

        findViewById(R.id.gg).setOnLongClickListener(v -> {
            if (Kooontoool){
                showBottomSheetDialog(getResources().getDrawable(R.drawable.icon_toast_alert), getString(R.string.confirm), getString(R.string.want_remove_it), false, sv -> {
                    ApkEnv.getInstance().unInstallApp("com.morocco.invincible.gg");
                    dismissBottomSheetDialog();
                }, v1 -> {
                    dismissBottomSheetDialog();
                });
            }else{
                toastImage(R.drawable.notife,"Please Upgrade to VIP");
            }

            return true;
        });

        findViewById(R.id.hiderecord).setOnClickListener(v -> {
            showBottomSheetDialog(getResources().getDrawable(R.drawable.icon_toast_alert), getString(R.string.confirm), getString(R.string.want_remove_it), false, sv -> {
                hiderecord = 1;
                dismissBottomSheetDialog();
            }, v1 -> {
                hiderecord = 0;
                dismissBottomSheetDialog();
            });
        });

        findViewById(R.id.fixinstall).setOnClickListener(v -> {
            if (Kooontoool){
                showBottomSheetDialog(getResources().getDrawable(R.drawable.icon_toast_alert), getString(R.string.confirm), getString(R.string.this_for_fix_obb_not_found_need_actived_this), false, sv -> {
                    fixinstallint = true;
                    dismissBottomSheetDialog();
                }, v1 -> {
                    fixinstallint = false;
                    dismissBottomSheetDialog();
                });
            }else{
                toastImage(R.drawable.notife,"Please Upgrade to VIP");
            }
        });



    }

    @SuppressLint("SetTextI18n")
    void initMenu3(){
        TextView kernelVersionTextView = findViewById(R.id.kernelversion);
        powerSpinnerView = findViewById(R.id.kerneldriver);
        MaterialButton installKernelButton = findViewById(R.id.installKernelButton);

        String kernelVersion = readKernelVersion();
        Log.d("MainActivity", "Kernel version: " + kernelVersion);

        kernelVersionTextView.setText(kernelVersion);

        powerSpinnerView.setOnSpinnerItemSelectedListener((OnSpinnerItemSelectedListener<String>) (oldIndex, oldItem, newIndex, newItem) -> Toast.makeText(getApplicationContext(), newItem, Toast.LENGTH_SHORT).show());


        findViewById(R.id.resetdevice).setOnClickListener(v -> {
            Exec("/changeid.sh", "Change Device ID, Success");
        });

        findViewById(R.id.resetguest).setOnClickListener(v -> {
            Exec("/resetguest.sh", "Reset Guest, Success");
        });

        installKernelButton.setOnClickListener(v -> {
            int selectedIndex = powerSpinnerView.getSelectedIndex();
            if (selectedIndex != -1) {
                installKernel(selectedIndex);
                recreate();
            } else {
                Toast.makeText(getApplicationContext(), "Please select a kernel version", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void checkAndDeleteFile(Context context) {
        File loaderDir = new File(context.getFilesDir(), "loader");
        if (!loaderDir.exists()) {
            new Downtwo(context).execute("1",FixCrash());
            return;
        }

        File fileToDelete = new File(loaderDir, "libpubgm.so");
        if (fileToDelete.exists()) {
           fileToDelete.delete();
        } else {
            new Downtwo(context).execute("1",FixCrash());
        }
    }


    void gameversion(LinearLayout a, LinearLayout b, LinearLayout c, LinearLayout d, LinearLayout e){
        a.setBackgroundResource(R.drawable.button_coming);
        b.setBackgroundResource(R.drawable.button_normal);
        c.setBackgroundResource(R.drawable.button_normal);
        d.setBackgroundResource(R.drawable.button_normal);
        e.setBackgroundResource(R.drawable.button_normal);
    }


    void animation(View v){
        Animator scale = ObjectAnimator.ofPropertyValuesHolder(v,
                PropertyValuesHolder.ofFloat(View.SCALE_X, 1, 0.5f, 1),
                PropertyValuesHolder.ofFloat(View.SCALE_Y, 1, 0.5f, 1));
        scale.setDuration(1000);
        scale.start();
    }

    void init() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.bounce);
        LinearLayout navhome = findViewById(R.id.navhome);
        LinearLayout navsetting = findViewById(R.id.navsetting);
        LinearLayout navtool = findViewById(R.id.navtool);
        LinearLayout effecthome = findViewById(R.id.effecthome);
        LinearLayout effectsetting = findViewById(R.id.effectsetting);
        LinearLayout effecttool = findViewById(R.id.effecttool);
        LinearLayout menu1 = findViewById(R.id.imenu1);
        LinearLayout menu2 = findViewById(R.id.imenu2);
        LinearLayout menu3 = findViewById(R.id.imenu3);
        ImageView home = findViewById(R.id.imghome);
        ImageView sett = findViewById(R.id.imgsett);
        ImageView tool = findViewById(R.id.imgtool);

        navhome.setOnClickListener(v -> {
            menu1.setVisibility(View.VISIBLE);
            menu2.setVisibility(View.GONE);
            menu3.setVisibility(View.GONE);
            effecthome.setBackgroundResource(R.drawable.button_normal);
            effectsetting.setBackgroundResource(R.drawable.buttonshape);
            effecttool.setBackgroundResource(R.drawable.buttonshape);
            animation(v);

            home.setBackgroundResource(R.drawable.homeon);
            sett.setBackgroundResource(R.drawable.outline_settings_24);
            tool.setBackgroundResource(R.drawable.ic_baseline_handyman_24);
        });

        navsetting.setOnClickListener(v -> {
            menu1.setVisibility(View.GONE);
            menu2.setVisibility(View.VISIBLE);
            menu3.setVisibility(View.GONE);
            effecthome.setBackgroundResource(R.drawable.buttonshape);
            effectsetting.setBackgroundResource(R.drawable.button_normal);
            effecttool.setBackgroundResource(R.drawable.buttonshape);
            animation(v);

            home.setBackgroundResource(R.drawable.baseline_home_on);
            sett.setBackgroundResource(R.drawable.ic_helpon);
            tool.setBackgroundResource(R.drawable.ic_baseline_handyman_24);

        });

        navtool.setOnClickListener(v -> {
            menu1.setVisibility(View.GONE);
            menu2.setVisibility(View.GONE);
            menu3.setVisibility(View.VISIBLE);
            effecthome.setBackgroundResource(R.drawable.buttonshape);
            effectsetting.setBackgroundResource(R.drawable.buttonshape);
            effecttool.setBackgroundResource(R.drawable.button_normal);
            animation(v);

            home.setBackgroundResource(R.drawable.baseline_home_on);
            sett.setBackgroundResource(R.drawable.outline_settings_24);
            tool.setBackgroundResource(R.drawable.toolsom);
        });

    }


    ////////////////////////// Load Json ////////////////////////////////////////
    public void doInitRecycler() {
        doShowProgress(true);
        ArrayList<Integer> imageValues = new ArrayList<>();
        ArrayList<String> titleValues = new ArrayList<>();
        ArrayList<String> versionValues = new ArrayList<>();
        ArrayList<String> statusValues = new ArrayList<>();
        ArrayList<String> packageValues = new ArrayList<>();
        try {
            String jsonLocation = loadJSONFromAsset("games.json");
            JSONObject jsonobject = new JSONObject(jsonLocation);
            JSONArray jarray = jsonobject.getJSONArray("gamesList");
            for (int i = 0; i < jarray.length(); i++) {
                JSONObject jb = (JSONObject) jarray.get(i);
                String title = jb.getString("title");
                String packageName = jb.getString("package");
                String version = jb.getString("version");
                String status = jb.getString("status");
                imageValues.add(GAME_LIST_ICON[i]);
                titleValues.add(title);
                versionValues.add(version);
                statusValues.add(status);
                packageValues.add(packageName);
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, imageValues, titleValues, versionValues, statusValues, packageValues);
        RecyclerView myView = findViewById(R.id.recyclerview);
        myView.setHasFixedSize(true);
        myView.setAdapter(adapter);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.HORIZONTAL);
        myView.setLayoutManager(llm);
    }

    public void addAdditionalApp(boolean system, String packageName) {
        this.system = system;
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(() -> {
            if (ApkEnv.getInstance().isInstalled(packageName)) {
                doHideProgress();
                ApkEnv.getInstance().launchApk(packageName);
            } else {
                try {
                    if (ApkEnv.getInstance().installByPackage(packageName)) {
                        doHideProgress();
                        ApkEnv.getInstance().launchApk(packageName);
                    }
                } catch (Exception err) {
                    FLog.error(err.getMessage());
                    doHideProgress();
                }
            }
        });
    }

    ////////////////////////// Other ////////////////////////////////////////
    public static boolean isAppInstalled(Context context, String packageName) {
        PackageManager packageManager = context.getPackageManager();
        try {
            packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public void launchbypass(){
        Intent launchIntent = getPackageManager().getLaunchIntentForPackage(game);
        if (launchIntent != null) {
            startActivity(launchIntent);
            new Handler().postDelayed(() -> {
                if (bitversi == 64 ){
                    if (gameint >= 1 && gameint <= 4) {
                        Exec("/TW "+game+" 001", getString(R.string.bypass_launch_success));
                        Log.e("BEPAS STATUS 1","SUCCESSS");
                    } else if (gameint == 5) {
                        Log.e("BEPAS STATUS 2","SUCCESSS");
                        Exec("/TW "+game+" 005", getString(R.string.bypass_launch_success));
                    }
                }else if (bitversi == 32){
                    if (gameint >= 1 && gameint <= 4) {
                        Exec("/TW "+game+" 22", getString(R.string.bypass_launch_success));
                        Log.e("BEPAS STATUS 3","SUCCESSS");
                    } else if (gameint == 5) {
                        Log.e("BEPAS STATUS 4","SUCCESSS");
                        Exec("/TW "+game+" 44", getString(R.string.bypass_launch_success));
                    }
                }

            }, 3800);
        }else{
            toastImage(R.drawable.ic_error,game + getString(R.string.not_installed_please_check));
        }
    }

    public void Exec(String path, String toast) {
        try {
            ExecuteElf("su -c chmod 777 " + getFilesDir() + path);
            ExecuteElf("su -c " + getFilesDir() + path);
            ExecuteElf("chmod 777 " + getFilesDir() + path);
            ExecuteElf(getFilesDir() + path);
            toastImage(R.drawable.ic_check, toast);
        } catch (Exception ignored) {
      }
    }

    private void ExecuteElf(String shell) {
        try {
            Runtime.getRuntime().exec(shell, null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void Loadssets() {
        MoveAssets(getFilesDir() + "/", "socs64");
        MoveAssets(getFilesDir() + "/", "socu64");
        MoveAssets(getFilesDir() + "/", "socs32");
        MoveAssets(getFilesDir() + "/", "socu32");
        MoveAssets(getFilesDir() + "/", "TW");
        MoveAssets(getFilesDir() + "/", "VNG");
        MoveAssets(getFilesDir() + "/", "kernels64");
    }

    private void MoveAssets(String outPath, String fileName) {
        File file = new File(outPath);
        if (!file.exists()) {
            if (!file.mkdirs()) {
                Log.e("--Method--", "copyAssetsSingleFile: cannot create directory.");
                return;
            }
        }
        try {
            InputStream inputStream = getAssets().open(fileName);
            File outFile = new File(file, fileName);
            FileOutputStream fileOutputStream = new FileOutputStream(outFile);
            byte[] buffer = new byte[1024];
            int byteRead;
            while (-1 != (byteRead = inputStream.read(buffer))) {
                fileOutputStream.write(buffer, 0, byteRead);
            }
            inputStream.close();
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String loadJSONFromAsset(String fileName) {
        String json;
        try {
            InputStream is = getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        stopService(new Intent(MainActivity.get(), FloatService.class));
        stopService(new Intent(MainActivity.get(), Overlay.class));
        stopService(new Intent(MainActivity.get(), FloatRei.class));
        stopService(new Intent(MainActivity.get(), ToggleBullet.class));
        stopService(new Intent(MainActivity.get(), ToggleAim.class));
        stopService(new Intent(MainActivity.get(), ToggleSimulation.class));

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Toast.makeText(this, getString(R.string.please_click_icon_logout_for_exit), Toast.LENGTH_SHORT).show();
    }

    public LinearProgressIndicator getProgresBar() {
        if (progres == null) {
            progres = findViewById(R.id.progress);
        }
        return progres;
    }

    public void doShowProgress(boolean indeterminate) {
        if (progres == null) {
            return;
        }
        progres.setVisibility(View.VISIBLE);
        progres.setIndeterminate(indeterminate);

        if (!indeterminate) {
            progres.setMin(0);
            progres.setMax(100);
        }
    }

    public void doHideProgress() {
        if (progres == null) {
            return;
        }
        progres.setIndeterminate(true);
        progres.setVisibility(View.GONE);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        } else {
            showSystemUI();
        }
    }

    private void hideSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    private void showSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    private boolean isServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        if (manager != null) {
            for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
                if (FloatService.class.getName().equals(service.service.getClassName())) {
                    return true;
                }
            }
        }
        return false;
    }

    private void startPatcher() {
        if (!Settings.canDrawOverlays(MainActivity.get())) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, 123);
        } else {
            startFloater();
        }
    }

    private void startFloater() {
        if (!isServiceRunning()) {
            if (kernel){
                loadAssets("kernels64");
            }else{
                loadAssets("socu64");
            }
            String CMD =    "rm -rf  /data/data/" + game + "/files;\n" +
                    "touch  /data/data/" + game + "/files;\n";
            Shell.su(CMD).submit();
            startService(new Intent(MainActivity.get(), FloatService.class));
        } else {
            toastImage(R.drawable.ic_error, getString(R.string.service_is_already_running));
        }
    }

    private void stopPatcher() {
        stopService(new Intent(MainActivity.get(), FloatService.class));
        stopService(new Intent(MainActivity.get(), Overlay.class));
        stopService(new Intent(MainActivity.get(), FloatRei.class));
        stopService(new Intent(MainActivity.get(), ToggleAim.class));
        stopService(new Intent(MainActivity.get(), ToggleBullet.class));
        stopService(new Intent(MainActivity.get(), ToggleSimulation.class));
    }

    public void loadAssets(String sockver) {
        daemonPath = MainActivity.this.getFilesDir().toString() + "/" + sockver;
        socket = daemonPath;
        try {
            Runtime.getRuntime().exec("chmod 777 " + daemonPath);
        } catch (IOException ignored) {
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        CountTimerAccout();
        boolean needsRecreate = getSharedPreferences("app_prefs", MODE_PRIVATE)
                .getBoolean("needs_recreate", false);
        if (needsRecreate) {
            getSharedPreferences("app_prefs", MODE_PRIVATE)
                    .edit()
                    .putBoolean("needs_recreate", false)
                    .apply();
        }
    }

    private void CountTimerAccout() {
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @SuppressLint({"SetTextI18n", "DefaultLocale"})
            @Override
            public void run() {
                try {
                    handler.postDelayed(this, 1000);
                    @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date expiryDate = dateFormat.parse(EXP());
                    long now = System.currentTimeMillis();
                    assert expiryDate != null;
                    long distance = expiryDate.getTime() - now;
                    long days = distance / (24 * 60 * 60 * 1000);
                    long hours = distance / (60 * 60 * 1000) % 24;
                    long minutes = distance / (60 * 1000) % 60;
                    long seconds = distance / 1000 % 60;
                    if (distance < 0) {
                    } else {
                        TextView Hari = findViewById(R.id.days);
                        TextView Jam = findViewById(R.id.hours);
                        TextView Menit = findViewById(R.id.minutes);
                        TextView Detik = findViewById(R.id.second);
                        if (days > 0) {
                            Hari.setText(" " + String.format("%02d", days));
                        }
                        if (hours > 0) {
                            Jam.setText(" " + String.format("%02d", hours));
                        }
                        if (minutes > 0) {
                            Menit.setText(" " + String.format("%02d", minutes));
                        }
                        if (seconds > 0) {
                            Detik.setText(" " + String.format("%02d", seconds));
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        handler.postDelayed(runnable, 0);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSIONS) {
            if (!(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                finish();
            }
        }
    }

    public static String readKernelVersion() {
        String kernelVersion = System.getProperty("os.version");
        Log.d("KernelVersion", "System Property os.version: " + kernelVersion);

        if (kernelVersion == null || kernelVersion.isEmpty()) {
            try {
                Process p = Runtime.getRuntime().exec("uname -r", null, null);
                InputStream is;
                if (p.waitFor() == 0) {
                    is = p.getInputStream();
                } else {
                    is = p.getErrorStream();
                }
                BufferedReader br = new BufferedReader(new InputStreamReader(is), 1024);
                String line = br.readLine();
                br.close();
                kernelVersion = line;
                Log.d("KernelVersion", "Kernel version from uname: " + kernelVersion);
            } catch (Exception ex) {
                final String[] abis;
                abis = Build.SUPPORTED_ABIS;
                String ext = " ";
                for (String abi : abis) {
                    if (TextUtils.equals(abi, "x86_64")) {
                        ext = "amd64";
                        break;
                    } else if (TextUtils.equals(abi, "x86")) {
                        ext = "x86";
                        break;
                    } else if (TextUtils.equals(abi, "armeabi-v7a")) {
                        ext = "armeabi-v7a";
                        break;
                    }
                }
                kernelVersion = System.getProperty("os.name") + " " + ext;
                Log.d("KernelVersion", "Fallback Kernel version: " + kernelVersion);
            }
        }

        return kernelVersion;
    }


    private int getKernelIndex(String kernelVersion) {
        if (kernelVersion.contains("4.9.186")) return 0;
        if (kernelVersion.contains("4.14.117")) return 1;
        if (kernelVersion.contains("4.14.180")) return 2;
        if (kernelVersion.contains("4.14.186")) return 3;
        if (kernelVersion.contains("4.14.186b")) return 4;
        if (kernelVersion.contains("4.14.186c")) return 5;
        if (kernelVersion.contains("4.19.81")) return 6;
        if (kernelVersion.contains("4.19.113")) return 7;
        if (kernelVersion.contains("4.19.113c")) return 8;
        if (kernelVersion.contains("4.19.157")) return 9;
        if (kernelVersion.contains("4.19.157b")) return 10;
        if (kernelVersion.contains("4.19.157-安卓13")) return 11;
        if (kernelVersion.contains("4.19.191-安卓13")) return 12;
        if (kernelVersion.contains("5.4.210-安卓13")) return 13;
        if (kernelVersion.contains("5.15")) return 14;
        if (kernelVersion.contains("5.15b")) return 15;
        if (kernelVersion.contains("5.10")) return 16;
        if (kernelVersion.contains("5.10b")) return 17;
        if (kernelVersion.contains("5.10-安卓13-GooglePixel")) return 18;
        if (kernelVersion.contains("5.4.61~250")) return 19;
        if (kernelVersion.contains("5.4.86~250")) return 20;
        if (kernelVersion.contains("5.4.147~250")) return 21;
        return -1;
    }


    private void installKernel(int index) {
        String kernelFileName = switch (index) {
            case 0 -> "4.9.186_fix.ko.sh";
            case 1 -> "4.14.117.ko.sh";
            case 2 -> "4.14.180.ko.sh";
            case 3 -> "4.14.186.ko.sh";
            case 4 -> "4.14.186b.ko.sh";
            case 5 -> "4.14.186c.ko.sh";
            case 6 -> "4.19.81.ko.sh";
            case 7 -> "4.19.113.ko.sh";
            case 8 -> "4.19.113c.ko.sh";
            case 9 -> "4.19.157.ko.sh";
            case 10 -> "4.19.157b.ko.sh";
            case 11 -> "4.19.157-安卓13.ko.sh";
            case 12 -> "4.19.191-安卓13.ko.sh";
            case 13 -> "5.4.210-安卓13.ko.sh";
            case 14 -> "5.15.ko.sh";
            case 15 -> "5.15b.ko.sh";
            case 16 -> "5.10.ko.sh";
            case 17 -> "5.10b.ko.sh";
            case 18 -> "5.10-安卓13-GooglePixel.ko.sh";
            case 19 -> "5.4.61~250.ko.sh";
            case 20 -> "5.4.86~250.ko.sh";
            case 21 -> "5.4.147~250.ko.sh";
            default -> "";
        };

        MoveAssets(getFilesDir() + "/", kernelFileName);
        Exec("/"+ kernelFileName, "Kernel Driver Success");
    }


}
