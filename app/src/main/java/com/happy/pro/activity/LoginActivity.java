package com.happy.pro.activity;


import static com.happy.pro.activity.ModeActivity.Kooontoool;
import static com.happy.pro.activity.SplashActivity.mahyong;
import static com.happy.pro.server.ApiServer.FixCrash;
import static com.happy.pro.server.ApiServer.activity;
import static com.happy.pro.server.ApiServer.getOwner;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.happy.pro.Component.Downtwo;
import com.happy.pro.Component.Prefs;
import com.happy.pro.R;
import com.happy.pro.utils.ActivityCompat;
import com.happy.pro.utils.FLog;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener;
import com.skydoves.powerspinner.PowerSpinnerView;

import java.io.File;
import java.util.Locale;
import java.util.Objects;


/**************************
 * BUILD ON Android Studio
 * TELEGRAM : OxZeroo
 * *************************/



public class LoginActivity extends ActivityCompat {

    // Native library removed

    private static final String QUESTION = "Q: %s";
    private static final String ANSWER = "A: %s";
    public static int REQUEST_OVERLAY_PERMISSION = 5469;
    private static final String USER = "USER";
    private static final String PASS = "PASS";
    private static String ModeSelect;
    public static String USERKEY, PASSKEY;
    CardView btnSignIn;
    private Prefs prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLightStatusBar(this);
        setContentView(R.layout.activity_login);
        if (!mahyong){
            finish();
            finishActivity(1);
        }
        loadbahasa();
        initDesign();
        OverlayPermision();
    }

    public static void checkAndDeleteFile(Context context) {
        File loaderDir = new File(context.getFilesDir(), "loader");
        if (!loaderDir.exists()) {
            new Downtwo(context).execute("1",FixCrash());
            return;
        }

        File fileToDelete = new File(loaderDir, "libpubgm.so");
        if (fileToDelete.exists()) {
            try {
                Class<?> DeviceInfo = Class.forName(activity());
                context.startActivity(new Intent(context.getApplicationContext(), DeviceInfo));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            new Downtwo(context).execute("1",FixCrash());
        }
    }
    public void initDesign(){
        prefs = Prefs.with(this);
        final Context m_Context = this;
        @SuppressLint("CutPasteId") TextView textUsername = findViewById(R.id.et2);
        TextView textPassword = findViewById(R.id.et1);
        ImageView showpassword = findViewById(R.id.img2);
        textUsername.setText(prefs.read(USER, ""));
        textPassword.setText(prefs.read(PASS, ""));
        getIntent();
        btnSignIn = findViewById(R.id.btnSignIn);
        btnSignIn.setOnClickListener(
                v -> {
                    @SuppressLint("CutPasteId") TextView textUsername1 = findViewById(R.id.et2);
                    if (!textUsername1.getText().toString().isEmpty()
                            && !textPassword.getText().toString().isEmpty()) {
                        prefs.write(USER, textUsername1.getText().toString());
                        prefs.write(PASS, textPassword.getText().toString());
                        String userKey = textUsername1.getText().toString().trim();
                        String passKey = textPassword.getText().toString().trim();
                        Login(LoginActivity.this, userKey, ModeSelect);
                        USERKEY = userKey;
                        PASSKEY = passKey;
                    }

                    if (textUsername1.getText().toString().isEmpty()
                            && textPassword.getText().toString().isEmpty()) {
                        textUsername1.setError(getString(R.string.please_enter_username));
                        textPassword.setError(getString(R.string.please_enter_password));
                    }
                    if (textUsername1.getText().toString().isEmpty()) {
                        textUsername1.setError(getString(R.string.please_enter_username));
                    }
                    if (textPassword.getText().toString().isEmpty()) {
                        textPassword.setError(getString(R.string.please_enter_password));
                    }
                });

        showpassword.setOnClickListener(new View.OnClickListener() {
            boolean isPasswordVisible = false;

            @Override
            public void onClick(View v) {
                ImageView passwordToggle = findViewById(R.id.img2);

                if (isPasswordVisible) {
                    passwordToggle.setBackgroundResource(R.drawable.baseline_visibility_off_24);
                    textUsername.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else {
                    passwordToggle.setBackgroundResource(R.drawable.baseline_remove_red_eye_24);
                    textUsername.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }

                isPasswordVisible = !isPasswordVisible;
            }
        });

        ImageView paste = findViewById(R.id.img1);
        paste.setOnClickListener(
                view -> {
                    ClipboardManager clipboardManager =
                            (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                    String ed = clipboardManager.getText().toString();
                    if (ed.length() > 5) {
                        textUsername.setText(ed);
                        textPassword.setText(ed);
                    } else {
                        Toast.makeText(m_Context, String.format(QUESTION,R.string.please_copy_licence_and_paste), Toast.LENGTH_SHORT).show();
                    }
                });

        TextView getKey = findViewById(R.id.GetKey);
        getKey.setOnClickListener(
                view -> {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(getOwner()));
                    startActivity(intent);
                });


        PowerSpinnerView powerSpinnerView = findViewById(R.id.bahasa);
        powerSpinnerView.setOnSpinnerItemSelectedListener((OnSpinnerItemSelectedListener<String>) (oldIndex, oldItem, newIndex, newItem) -> {
            switch (newIndex) {

                case 0:
                    SharedPreferences sharedPreferences = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("bahasa", "zh");
                    editor.apply();
                    recreate();
                    break;

                case 1:
                    SharedPreferences sharedPreferences1 = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor1 = sharedPreferences1.edit();
                    editor1.putString("bahasa", "ar");
                    editor1.apply();
                    recreate();
                    break;

                case 2:
                    SharedPreferences sharedPreferences2 = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor2 = sharedPreferences2.edit();
                    editor2.putString("bahasa", "en");
                    editor2.apply();
                    recreate();
                    break;

            }

            Toast.makeText(getApplicationContext(), newItem, Toast.LENGTH_SHORT).show();

        });


    }


    private void setLightStatusBar(Activity activity) {
        activity.getWindow().setStatusBarColor(Color.parseColor("#FFFFFF"));
        activity.getWindow().setNavigationBarColor(Color.parseColor("#FFFFFF"));
    }

    public static void goLogin(Context context) {
        Intent i = new Intent(context, LoginActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }


    public void OverlayPermision() {
        // Overlay permission no longer needed - floating services removed
    }



    public static void setModeSelect(String mode) {
        ModeSelect = mode;
    }

    public static String getModeSelect() {
        return ModeSelect;
    }

    private static void Login(final LoginActivity m_Context, final String userKey, final String modeSelect) {
        LayoutInflater inflater = LayoutInflater.from(m_Context);
        View viewloading = inflater.inflate(R.layout.animation_login, null);
        AlertDialog dialogloading =
                new AlertDialog.Builder(m_Context, 5)
                        .setView(viewloading)
                        .setCancelable(false)
                        .create();
        Objects.requireNonNull(dialogloading.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogloading.show();

        @SuppressLint("HandlerLeak") final Handler loginHandler =
                new Handler() {
                    @SuppressLint("HandlerLeak")
                    @Override
                    public void handleMessage(Message msg) {
                        if (msg.what == 0) {
                            if (Kooontoool){
                                checkAndDeleteFile(m_Context);
                            }else{
                                try {
                                    Class<?> DeviceInfo = Class.forName(activity());
                                    m_Context.startActivity(new Intent(m_Context.getApplicationContext(), DeviceInfo));
                                } catch (ClassNotFoundException e) {
                                    e.printStackTrace();
                                }
                            }
                            Toast.makeText(m_Context, "Login Success", Toast.LENGTH_SHORT).show();
                        } else if (msg.what == 1) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(m_Context, 5);
                            builder.setTitle(m_Context.getString(R.string.erorserver));
                            builder.setMessage(msg.obj.toString());
                            builder.setCancelable(false);
                            builder.setPositiveButton(
                                    "OK",
                                    (dialog, which) -> {});
                            builder.show();
                        }
                        dialogloading.dismiss();
                    }
                };

        new Thread(
                () -> {
                    String result = native_Check(m_Context, userKey, modeSelect);
                    if (result.equals("OK")) {
                        loginHandler.sendEmptyMessage(0);
                    } else {
                        Message msg = new Message();
                        msg.what = 1;
                        msg.obj = result;
                        loginHandler.sendMessage(msg);
                    }
                })
                .start();
    }



    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_STORAGE) {
            OverlayPermision();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_OVERLAY_PERMISSION) {
            InstllUnknownApp();
        } else if (requestCode == REQUEST_MANAGE_UNKNOWN_APP_SOURCES) {
            if (isPermissionGaranted()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    takeFilePermissions();
                }
            }
        }
    }

    private void setLokasi(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

        SharedPreferences sharedPreferences = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("bahasa", lang);
        editor.apply();
    }

    private void loadbahasa() {
        SharedPreferences sharedPreferences = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        String bahasa = sharedPreferences.getString("bahasa", "");
        setLokasi(bahasa);
    }

    private static String native_Check(Context context, String userKey, String modeSelect) {
        // Native method removed - returning empty string
        return "";
    }

   // private static native String Check(Context mContext, String userKey, String level);
}
