package com.happy.pro.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.Toast;

import com.blankj.molihuan.utilcode.util.ClipboardUtils;
import com.blankj.molihuan.utilcode.util.DeviceUtils;
import com.blankj.molihuan.utilcode.util.ToastUtils;
import com.happy.pro.R;
import com.happy.pro.databinding.ActivityCrashBinding;

import com.happy.pro.utils.ActivityCompat;

import java.util.Objects;
//import io.michaelrocks.paranoid.Obfuscate;

/**************************
 * BUILD ON Android Studio
 * TELEGRAM : OxZeroo
 * *************************/


//@Obfuscate
public class CrashActivity extends ActivityCompat {
    private ActivityCrashBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toast.makeText(this, "Application Crash", Toast.LENGTH_LONG).show();
        
        binding = ActivityCrashBinding.inflate(getLayoutInflater());
        setSupportActionBar(binding.topAppBar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("BearMod Log Crash");

        String error = "Manufacturer: " + DeviceUtils.getManufacturer() + "\n" +
                "Device: " + DeviceUtils.getModel() + "\n" +
                getIntent().getStringExtra("Software") +
                "\n\n" +
                getIntent().getStringExtra("Error") +
                "\n\n" +
                getIntent().getStringExtra("Date");

        binding.result.setText(error);

        binding.fab.setOnClickListener(v -> {
            ToastUtils.make().setBgColor(Color.GRAY).setLeftIcon(R.drawable.ic_launcher_foreground).setNotUseSystemToast().setTextColor(Color.WHITE).show("Text Copy");
            ClipboardUtils.copyText(binding.result.getText());
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        System.exit(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem close = menu.add(getString(R.string.close));
        close.setContentDescription("Close App");
        close.setIcon(R.drawable.ic_close);
        close.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (Objects.equals(item.getTitle(), getString(R.string.close))) {
            finish();
            return true;
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    @Override
    public void finish() {
        super.finish();
        finishAndRemoveTask();
    }
}
