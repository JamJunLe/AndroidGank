package com.flyman.app.androidgank.base;

import android.app.Application;
import android.content.Context;

import com.flyman.app.androidgank.R;
import com.flyman.app.util.log.LogUtils;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class GankApplication extends Application {
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        init();
        setTypeFace();
    }

    private void init() {
        /**
         * lBuilder = new LogUtils.Builder()
         .setLogSwitch(BuildConfig.DEBUG)// 设置log总开关，默认开
         .setGlobalTag("CMJ")// 设置log全局标签，默认为空
         // 当全局标签不为空时，我们输出的log全部为该tag，
         // 为空时，如果传入的tag为空那就显示类名，否则显示tag
         .setLog2FileSwitch(false)// 打印log时是否存到文件的开关，默认关
         .setBorderSwitch(true)// 输出日志是否带边框开关，默认开
         .setLogFilter(LogUtils.V);// log过滤器，和logcat过滤器同理，默认Verbose
         */
        LogUtils.Builder builder = new LogUtils.Builder(this);
        mContext = getApplicationContext();
    }

    private void setTypeFace() {
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/web_font.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }

    /**
     * 获取Context 供全局使用
     *
     * @param
     * @return Context
     */
    public static Context getAppContext() {
        return mContext;
    }
}
