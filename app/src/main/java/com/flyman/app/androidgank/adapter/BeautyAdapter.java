package com.flyman.app.androidgank.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.util.SparseArray;

import com.flyman.app.androidgank.R;
import com.flyman.app.androidgank.model.bean.ArticleResult;
import com.flyman.app.androidgank.model.bean.ImageViewVH;
import com.flyman.app.androidgank.utils.GlideHelper;
import com.flyman.app.androidgank.wrapper.ScaleImageView;
import com.flyman.app.util.log.LogUtils;
import com.flyman.app.util.screen.DensityUtil;

import org.byteam.superadapter.SuperAdapter;
import org.byteam.superadapter.SuperViewHolder;

import java.util.List;

public class BeautyAdapter extends SuperAdapter<ArticleResult.ResultsBean> {
    private SparseArray<ImageViewVH> mHeightArray;
    private int screenWidth;
    public BeautyAdapter(Context context, List<ArticleResult.ResultsBean> items, @LayoutRes int layoutResId) {
        super(context, items, layoutResId);
        mHeightArray = new SparseArray();
        screenWidth = DensityUtil.getWidth(context);
    }

    @Override
    public void onBind(SuperViewHolder holder, int viewType, final int layoutPosition, final ArticleResult.ResultsBean item) {
        final ScaleImageView imageView = holder.findViewById(R.id.iv_stagger_beauty);
//        String time = item.getPublishedAt();
//        holder.setText(R.id.tv_stagger_time, time.substring(0, time.indexOf("T")));
        int imageHeight = item.getHeight();//图片的实际高
        int imageWidth = item.getWidth();//图片的实际宽
        LogUtils.e("setInitSize", "图片的原始宽 = " + imageWidth + " 图片的原始高 = " + imageHeight);
        //经过计算后保存的宽高
        if (mHeightArray.get(layoutPosition) != null) {
            imageWidth = mHeightArray.get(layoutPosition).getWidth();
            imageHeight = mHeightArray.get(layoutPosition).getHeigh();
        } else {
            int vw = screenWidth / 2 - imageView.getPaddingLeft() - imageView.getPaddingRight();
            float scale = (float) vw / (float) imageWidth;
            int vh = (int) (scale * imageHeight);
            imageHeight = vh + imageView.getPaddingTop() + imageView.getPaddingBottom();
            imageWidth = vw;
            mHeightArray.put(layoutPosition, new ImageViewVH(imageWidth, imageHeight));
        }
        imageView.setInitSize(imageWidth, imageHeight);
        LogUtils.e("setInitSize", "控件的imageWidth= " + imageWidth + " 控件的imageHeight = " + imageHeight);
        imageView.setTag(item.getUrl());
        GlideHelper.loadBitmap(getContext(), item.getUrl(), imageView);
    }


}
