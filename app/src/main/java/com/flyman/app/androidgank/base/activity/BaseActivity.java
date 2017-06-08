package com.flyman.app.androidgank.base.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import java.io.Serializable;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public abstract class BaseActivity extends AppCompatActivity {
    protected Toast mToast;
    protected Snackbar mSnackbar;

    protected abstract void initValAndConfig();

    /**
     * 发出一个短Toast
     *
     * @param text 内容
     */
    public void toastShort(String text) {
        toast(text, Toast.LENGTH_SHORT);
    }

    /**
     * 发出一个长toast提醒
     *
     * @param text 内容
     */
    public void toastLong(String text) {
        toast(text, Toast.LENGTH_LONG);
    }


    private void toast(final String text, final int duration) {
        if (!TextUtils.isEmpty(text)) {
            if (mToast == null) {
                mToast = Toast.makeText(getApplicationContext(), text, duration);
            } else {
                mToast.cancel();
                mToast.setText(text);
                mToast.setDuration(duration);
            }
            mToast.show();
        }
    }

    public void showLongSnackBar(String text, View view) {
        showSnackBar(text, view, Snackbar.LENGTH_LONG);
    }

    public void showShortSnackBar(String text, View view) {
        showSnackBar(text, view, Snackbar.LENGTH_SHORT);
    }


    private void showSnackBar(final String text, View view, final int duration) {
        if (!TextUtils.isEmpty(text)) {
            if (mSnackbar == null) {
                mSnackbar = Snackbar.make(view, text, duration);
            } else {
                if (mSnackbar.isShown() == true) {
                    mSnackbar.dismiss();
                }
                mSnackbar.setText(text);
                mSnackbar.setDuration(duration);
            }
            mSnackbar.show();
        }
    }

    protected void openActivity(Class<?> cls) {
        openActivity(this, cls);
    }

    public static void openActivity(Context context, Class<?> cls) {
        Intent intent = new Intent(context, cls);
        context.startActivity(intent);
    }

    /**
     * 打开 Activity 的同时传递一个数据
     */
    protected <V extends Serializable> void openActivity(Class<?> cls, String key, V value) {
        openActivity(this, cls, key, value);
    }


    /**
     * 打开 Activity 的同时传递一个数据
     */
    public <V extends Serializable> void openActivity(Context context, Class<?> cls, String key, V value) {
        Intent intent = new Intent(context, cls);
        intent.putExtra(key, value);
        context.startActivity(intent);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    protected void setToolbar(Toolbar mToolbar, String title) {
        if (mToolbar != null) {
            mToolbar.setTitle(title);
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

        }
    }

    protected void getIntentFormActivity() {
    }

}
