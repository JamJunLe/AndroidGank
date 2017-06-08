package com.flyman.app.androidgank.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;

import com.flyman.app.androidgank.R;
import com.flyman.app.androidgank.model.bean.ArticleResult;
import com.flyman.app.androidgank.utils.TimeUtil;
import com.flyman.app.util.string.ChenkNullUtil;

import org.byteam.superadapter.SuperAdapter;
import org.byteam.superadapter.SuperViewHolder;

import java.util.List;


public class NavigateArticleAdapter extends SuperAdapter<ArticleResult.ResultsBean> {


    public NavigateArticleAdapter(Context context, List<ArticleResult.ResultsBean> items, @LayoutRes int layoutResId) {
        super(context, items, layoutResId);
//        enableLoadAnimation(500, new AlphaInAnimation());
//        setOnlyOnce(false);
    }

    @Override
    public void onBind(SuperViewHolder holder, int viewType, int layoutPosition, ArticleResult.ResultsBean item) {
        holder.setText(R.id.tv_article_title, ChenkNullUtil.getNullString(item.getDesc()).trim());
        holder.setText(R.id.tv_article_time, ChenkNullUtil.getNullString(TimeUtil.computePastTime((item.getPublishedAt()).trim())));
        holder.setText(R.id.tv_article_who,ChenkNullUtil.getNullString(item.getWho()).trim());
//        ImageView imageView = (ImageView) holder.itemView.findViewById(R.id.iv_article_img);
//        imageView.setVisibility(View.GONE);
//        LinearLayout content = holder.findViewById(R.id.ll_navigate_content);
//        if (item.getImages() != null)//不包含图片
//        {
//            GlideHelper.loadAsBitmap(getContext(),item.getImages().get(0),imageView);
//            imageView.setVisibility(View.VISIBLE);
//            content.setPadding(DensityUtil.dip2px(getContext(),16),0,0,0);
//        }
//        else
//        {
//            content.setPadding(0,0,0,0);
//            imageView.setVisibility(View.GONE);
//        }
    }
}
