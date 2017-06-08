package com.flyman.app.util.webview;

import android.webkit.WebChromeClient;
import android.webkit.WebView;


public class MyWebChromeClient extends WebChromeClient {

    private IWebClientListener mIWebClientListener;

    public MyWebChromeClient(IWebClientListener mIWebClientListener) {
        super();
        this.mIWebClientListener = mIWebClientListener;
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        super.onProgressChanged(view, newProgress);
        mIWebClientListener.onProgressChanged(view, newProgress);
    }
}
