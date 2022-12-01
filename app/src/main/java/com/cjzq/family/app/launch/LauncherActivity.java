package com.cjzq.family.app.launch;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cjzq.family.R;
import com.cjzq.family.app.home.ExcelActivity;
import com.cjzq.family.base.BaseActivity;
import com.cjzq.family.databinding.ActivityLauncherBinding;

import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

/**
 * Author : Gab
 * TIme   : 2022.12.1 16:06
 * Desc   : 启动页
 */
@RuntimePermissions
public class LauncherActivity extends BaseActivity<ActivityLauncherBinding> {


    @Override
    protected ActivityLauncherBinding getViewBinding() {
        return ActivityLauncherBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void initData() {
    }


    @Override
    protected void initView() {
        LauncherActivityPermissionsDispatcher.initAfterPermissionCheckedWithPermissionCheck(this);
    }

    @NeedsPermission({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public void initAfterPermissionChecked() {
        startActivity(new Intent(LauncherActivity.this, ExcelActivity.class));
        finish();
    }

    @OnShowRationale({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    //给用户解释要请求什么权限，为什么需要此权限
    public void showRationale(final PermissionRequest request) {
        new AlertDialog.Builder(LauncherActivity.this, R.style.Theme_AppCompat_Light_Dialog_Alert)
                .setMessage("使用APP需要存储权限，是否继续请求权限")
                .setPositiveButton(getString(android.R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        request.proceed();//继续执行请求
                    }
                }).setNegativeButton(getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                request.cancel();//取消执行请求
            }
        }).show();
    }

    @OnNeverAskAgain({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public void multiNeverAsk() {
        Toast.makeText(this, "权限未授予,部分功能可能无法正常执行", Toast.LENGTH_SHORT).show();
        initAfterPermissionChecked();
    }

    //一旦用户拒绝了
    @OnPermissionDenied({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public void multiDenied() {
        Toast.makeText(this, "已拒绝存储权限,将无法正常存储数据", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        LauncherActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }
}
