package com.cjzq.family.base;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewbinding.ViewBinding;

public abstract class BaseActivity<T extends ViewBinding> extends AppCompatActivity {
    public T binding;
    protected Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        binding = getViewBinding();
        setContentView(binding.getRoot());
        initData();
        getParentIntent();
        initView();
        initListener();
        initBroadcast();
    }

    @Override
    protected void onStart() {
        super.onStart();
        initAnimation();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startAnimation();
    }

    protected abstract T getViewBinding();

    protected abstract void initData();

    protected abstract void initView();

    protected void initAnimation() {
    }

    protected void startAnimation() {
    }

    protected void initListener() {
    }

    protected void getParentIntent() {
    }

    protected void initBroadcast() {
    }

    /**
     * 重载startActivity()
     */
    protected void startActivity(Context context, Class<?> cls) {
        startActivity(context, cls, null, null, null);
    }

    protected void startActivity(Context context, Class<?> cls, String action) {
        startActivity(context, cls, action, null, null);
    }

    protected void startActivity(Context context, Class<?> cls, String bundleKey, Bundle bundle) {
        startActivity(context, cls, null, bundleKey, bundle);
    }

    protected void startActivity(Context context, Class<?> cls, String action, String bundleKey, Bundle bundle) {
        Intent intent = new Intent(context, cls);
        if (action != null)
            intent.setAction(action);
        if (bundle != null)
            intent.putExtra(bundleKey, bundle);
        startActivity(intent);
    }

    public static AlertDialog.Builder confirmBuilder(Context c, String title, String message, DialogInterface.OnClickListener okL, DialogInterface.OnClickListener cancelL) {
        AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(c);
        mDialogBuilder
                .setTitle(title)
                .setMessage(message)
                .setNegativeButton(android.R.string.no, okL)
                .setPositiveButton(android.R.string.yes, cancelL);
        return mDialogBuilder;
    }

    /**
     * 开启新activity并结束本身
     */
    protected void finishStartActivity(Context context, Class<?> cls) {
        startActivity(context, cls, null, null, null);
        finish();
    }

    protected void finishStartActivity(Context context, Class<?> cls, String action) {
        startActivity(context, cls, action, null, null);
        finish();
    }

    protected void finishStartActivity(Context context, Class<?> cls, String bundleKey, Bundle bundle) {
        startActivity(context, cls, null, bundleKey, bundle);
        finish();
    }
}
