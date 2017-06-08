package com.flyman.app.androidgank.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.flyman.app.androidgank.R;
import com.flyman.app.androidgank.model.bean.ArticleResult;
import com.flyman.app.androidgank.utils.GlideHelper;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.List;
/**
 *  @ClassName ShowImageAdapter
 *  @description 福利页适配器
 *  
 *  @author Flyman
 *  @date 2017-5-14 12:09
 */
public class ShowImageAdapter extends PagerAdapter implements ViewPager.OnPageChangeListener{
    private List<ArticleResult.ResultsBean> mList;
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    public ShowImageAdapter(List<ArticleResult.ResultsBean> list, Context context) {
        mList = list;
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        final PhotoView mPhotoView = (PhotoView) mLayoutInflater.inflate(R.layout.item_acitivity_image,null,false);
        GlideHelper.loadBitmap(mContext, mList.get(position).getUrl(), new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                mPhotoView.setImageBitmap(resource);
            }
        });
        container.addView(mPhotoView);
        return mPhotoView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
