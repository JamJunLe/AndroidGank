package com.flyman.app.androidgank.ui.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.flyman.app.androidgank.R;
import com.flyman.app.androidgank.base.GankApplication;
import com.flyman.app.androidgank.base.activity.SubscriberActivity;
import com.flyman.app.androidgank.utils.IntentUtil;
import com.flyman.app.util.log.LogUtils;
import com.flyman.app.util.string.ChenkNullUtil;
import com.flyman.app.util.webview.IWebClientListener;
import com.flyman.app.util.webview.MyWebChromeClient;
import com.flyman.app.util.webview.MyWebViewClient;

import butterknife.Bind;
import butterknife.ButterKnife;

public class WebActivity extends SubscriberActivity implements IWebClientListener {
    private WebView mWebView;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.pb_web)
    ProgressBar mProgressBar;
    @Bind(R.id.fl_web_container)
    FrameLayout mFrameLayout;
    private String url;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        ButterKnife.bind(this);
        ButterKnife.bind(mToolbar);
        getIntentFormActivity();
        initValAndConfig();
        setToolbar(mToolbar, url);
    }

    @Override
    protected void getIntentFormActivity() {
        super.getIntentFormActivity();
        if (!ChenkNullUtil.isNullObj(getIntent())) {
            url = getIntent().getStringExtra(IntentUtil.WEB_URL);
            LogUtils.e("onClick", "url " + url);
        }
    }

    @Override
    protected void initValAndConfig() {
        //进度条
        mWebView = new WebView(GankApplication.getAppContext());
        mWebView.setWebChromeClient(new MyWebChromeClient(this));
        mWebView.setWebViewClient(new MyWebViewClient(this));
        mWebView.getSettings().setSupportZoom(false);
        mWebView.getSettings().setBuiltInZoomControls(false);
        mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setDomStorageEnabled(true);
        mFrameLayout.addView(mWebView);
        mWebView.loadUrl(url);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_image, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.menu_action_open_with_web: {
                IntentUtil.openByDefaultWeb(this, url);
                return true;
            }
            case R.id.menu_action_share: {
                IntentUtil.shareText(this, url);
                return true;
            }
            default: {

            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        mProgressBar.setVisibility(View.VISIBLE);
        LogUtils.e("onProgressChanged", "newProgress " + newProgress);
        mProgressBar.setProgress(newProgress);
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        mProgressBar.setProgress(0);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        mProgressBar.setProgress(100);
        mProgressBar.setVisibility(View.GONE);

    }

    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        showLongSnackBar("加载出了问题请,下拉刷新重试", mWebView);
    }


    /**
     * 按键响应，在WebView中查看网页时，按返回键的时候按浏览历史退回,如果不做此项处理则整个WebView返回退出
     *
     * @param
     * @return nothing
     */

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Check if the key event was the Back button and if there's history
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
            // 返回键退回
            mWebView.goBack();
            return true;
        }
        // If it wasn't the Back key or there's no web page history, bubble up
        // to the default
        // system behavior (probably exit the activity)
        return super.onKeyDown(keyCode, event);
    }

    public void clearWebViewResource() {
        if (mWebView != null) {
            mWebView.removeAllViews();
            // in android 5.1(sdk:21) we should invoke this to avoid memory leak
            // see (https://coolpers.github.io/webview/memory/leak/2015/07/16/
            // android-5.1-webview-memory-leak.html)
            ((ViewGroup) mWebView.getParent()).removeView(mWebView);
            mWebView.setTag(null);
            mWebView.clearHistory();
            mWebView.destroy();
            mWebView = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clearWebViewResource();
    }
}
