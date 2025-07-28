package com.happy.pro.adapter;

import static com.happy.pro.activity.MainActivity.fixinstallint;
import static com.happy.pro.activity.ModeActivity.Kooontoool;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.molihuan.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.AppUtils;
import com.happy.pro.activity.MainActivity;
import com.happy.pro.floating.ToggleAim;
import com.happy.pro.floating.ToggleBullet;
import com.happy.pro.floating.ToggleSimulation;
import com.happy.pro.libhelper.FileHelper;
import com.happy.pro.utils.ActivityCompat;
import com.happy.pro.utils.FLog;
import com.happy.pro.utils.PermissionUtils;
import com.happy.pro.utils.UiKit;



import java.util.ArrayList;

import android.content.Intent;
import com.happy.pro.floating.FloatService;
import com.happy.pro.floating.Overlay;
import com.happy.pro.floating.FloatRei;
import android.content.Context;
import android.content.pm.PackageManager;
import com.happy.pro.R;
import com.happy.pro.libhelper.ApkEnv;

/**************************
 * BUILD ON Android Studio
 * TELEGRAM : OxZeroo
 * *************************/

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {
    public MainActivity activity;
    public ArrayList<Integer> imageValues;
    public ArrayList<String> titleValues;
    public ArrayList<String> versionValues;
    public ArrayList<String> statusValues;
    public ArrayList<String> packageValues;

    public RecyclerViewAdapter(MainActivity activity, ArrayList<Integer> imageValues, ArrayList<String> titleValues, ArrayList<String> versionValues, ArrayList<String> statusValues, ArrayList<String> packageValues) {
        this.activity = activity;
        this.imageValues = new ArrayList<>();
        this.titleValues = new ArrayList<>();
        this.versionValues = new ArrayList<>();
        this.statusValues = new ArrayList<>();
        this.packageValues = new ArrayList<>();
        filterInstalledGames(imageValues, titleValues, versionValues, statusValues, packageValues);
    }

    private void filterInstalledGames(ArrayList<Integer> imageValues, ArrayList<String> titleValues, ArrayList<String> versionValues, ArrayList<String> statusValues, ArrayList<String> packageValues) {
        for (int i = 0; i < packageValues.size(); i++) {
            if (AppUtils.isAppInstalled(packageValues.get(i))) {
                this.imageValues.add(imageValues.get(i));
                this.titleValues.add(titleValues.get(i));
                this.versionValues.add(versionValues.get(i));
                this.statusValues.add(statusValues.get(i));
                this.packageValues.add(packageValues.get(i));
            }
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View listItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_games, parent, false);
        return new MyViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.gameIcon.setImageResource(imageValues.get(position));
        holder.gameTitle.setText(titleValues.get(position));
        holder.gameVersion.setText(versionValues.get(position));

        testanjing(holder.okBtn, holder.status, packageValues.get(position));

        holder.okBtn.setOnClickListener(v -> {
            if (statusValues.get(position).equals("Maintenance") || statusValues.get(position).equals("Coming Soon")) {
                ActivityCompat.toastImage(R.drawable.icon, "App is currently under: " + statusValues.get(position));
            } else {
                activity.doShowProgress(true);
                doInstallAndRun(holder, position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return imageValues.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private final ImageView gameIcon;
        private final TextView gameTitle;
        private final TextView gameVersion;
        private final TextView status;
        private final FrameLayout okBtn;

        public MyViewHolder(View itemView) {
            super(itemView);
            gameIcon = itemView.findViewById(R.id.gameIcon);
            gameTitle = itemView.findViewById(R.id.gameTitle);
            gameVersion = itemView.findViewById(R.id.gameVersion);
            okBtn = itemView.findViewById(R.id.okBtn);
            status = itemView.findViewById(R.id.status);
        }
    }

    @SuppressLint("SetTextI18n")
    public void testanjing(FrameLayout game, TextView status, String pkg){

        activity.runOnUiThread(()-> {
            if (ApkEnv.getInstance().isInstalled(pkg)){
                if (ApkEnv.getInstance().isRunning(pkg)){
                    status.setText("Kill Game");
                }else{
                    status.setText("Open Game");
                }
            }else{
                status.setText("Not Installed");
            }
        });

        game.setOnLongClickListener(new View.OnLongClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public boolean onLongClick(View v) {
                activity.showBottomSheetDialog(activity.getResources().getDrawable(R.drawable.icon_toast_alert), activity.getString(R.string.confirm), activity.getString(R.string.want_remove_it), false, sv -> {
                    activity.doShowProgress(true);
                    unInstallWithDellay(pkg);
                    activity.dismissBottomSheetDialog();
                }, v1 -> {
                    activity.dismissBottomSheetDialog();
                });
                return false;
            }
        });
    }

    @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
    private void doInstallAndRun(MyViewHolder holder, int position) {
        if (activity == null) {
            ToastUtils.showLong("Null Activity");
            return;
        }

        activity.CURRENT_PACKAGE = packageValues.get(position);
    	Handler handler = new Handler(Looper.getMainLooper());
        handler.post(() -> {
            if (ApkEnv.getInstance().isInstalled(packageValues.get(position))) {
                activity.doHideProgress();
                if (ApkEnv.getInstance().isRunning(packageValues.get(position))) {
                    ApkEnv.getInstance().stopRunningApp(packageValues.get(position));
                    holder.status.setText("Open Game");
                    activity.stopService(new Intent(MainActivity.get(), FloatService.class));
                    activity.stopService(new Intent(MainActivity.get(), Overlay.class));
                    activity.stopService(new Intent(MainActivity.get(), ToggleBullet.class));
                    activity.stopService(new Intent(MainActivity.get(), ToggleAim.class));
                    activity.stopService(new Intent(MainActivity.get(), ToggleSimulation.class));
                } else {
                    if (ApkEnv.getInstance().tryAddLoader(packageValues.get(position))) {
                        activity.launchSplash(packageValues.get(position));
                        testanjing(holder.okBtn, holder.status, packageValues.get(position));
                    }
                }
            } else {
                    try {
                        if (Kooontoool){
                            activity.showBottomSheetDialog(activity.getResources().getDrawable(imageValues.get(position)), "Client: " + titleValues.get(position), "This process may take 1-3 minutes, please do not close the application until the process is complete.", false, v -> {
                                activity.dismissBottomSheetDialog();
                                if (!fixinstallint){
                                    FileHelper.tryInstallWithCopyObb(activity, activity.getProgresBar(), packageValues.get(position));
                                }else{
                                   PermissionUtils.openobb(activity,1,packageValues.get(position));
                                }
                            }, v1 -> {
                                activity.doHideProgress();
                                activity.dismissBottomSheetDialog();
                            });
                        }else{
                            ActivityCompat.toastImage(R.drawable.notife,"Please Upgrade to VIP");
                        }

                    } catch(Exception err) {
                        FLog.error(err.getMessage());
                  }
            }
        });
    }

    private void unInstallWithDellay(String packageName) {
        UiKit.defer().when(() -> {
            long time = System.currentTimeMillis();
            ApkEnv.getInstance().unInstallApp(packageName);
            time = System.currentTimeMillis() - time;
            long delta = 500L - time;
            if (delta > 0) {
                UiKit.sleep(delta);
            }
        }).done((res) -> {
            activity.doInitRecycler();
            activity.doHideProgress();
            ActivityCompat.toastImage(R.drawable.ic_check, packageName + " was successfully uninstalled.");
        });
    }
}
