package com.flyman.app.androidgank.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.flyman.app.androidgank.model.bean.ArticleResult;
import com.flyman.app.androidgank.ui.activity.ImageActivity;
import com.flyman.app.androidgank.ui.activity.WebActivity;

import java.io.Serializable;
import java.util.List;

public class IntentUtil {
    public static final String IMAGE_ARRAY_LIST = "IMAGE_ARRAY_LIST";
    public static final String IMAGE_ARRAY_LIST_POSITION = "IMAGE_ARRAY_LIST_POSITION";

    public static final String WEB_URL = "WEB_URL";
    public static void startImageActivity(Context context, List<ArticleResult.ResultsBean> mList,int position) {
        Intent intent = new Intent(context, ImageActivity.class);
        Bundle mBundle = new Bundle();
        mBundle.putSerializable(IMAGE_ARRAY_LIST, (Serializable) mList);
        mBundle.putInt(IMAGE_ARRAY_LIST_POSITION,position);
        intent.putExtras(mBundle);
        context.startActivity(intent);
    }

    public static void startWebActivity(Context context,String url) {
        Intent intent = new Intent(context, WebActivity.class);
        intent.putExtra(WEB_URL,url);
        context.startActivity(intent);
    }

    public static void openByDefaultWeb(Context context,String url) {
        Intent mIntent = new Intent();
        mIntent.setAction(Intent.ACTION_VIEW);
        Uri mUri = Uri.parse(url);
        mIntent.setData(mUri);
        context.startActivity(mIntent);
    }

    public static void shareText(Context context,String msg) {
        ShareUtil.shareText(context,msg);
    }
}
