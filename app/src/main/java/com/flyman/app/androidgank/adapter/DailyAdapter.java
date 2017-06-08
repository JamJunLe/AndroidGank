package com.flyman.app.androidgank.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.SparseArray;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.flyman.app.androidgank.R;
import com.flyman.app.androidgank.model.bean.DayHistoryArticleCompound;
import com.flyman.app.androidgank.model.bean.ImageViewVH;
import com.flyman.app.androidgank.utils.GlideHelper;

import org.byteam.superadapter.IMulItemViewType;
import org.byteam.superadapter.SuperAdapter;
import org.byteam.superadapter.SuperViewHolder;

import java.util.List;


public class DailyAdapter extends SuperAdapter<DayHistoryArticleCompound> {
    private static final int VIEW_TYPE_COUNT = 3;
    private SparseArray<ImageViewVH> mHeightArray;
    public DailyAdapter(Context context, List<DayHistoryArticleCompound> items, IMulItemViewType<DayHistoryArticleCompound> mulItemViewType) {
        super(context, items, mulItemViewType);
        mHeightArray = new SparseArray<>();
        enableLoadAnimation();
    }

    @Override
    public void onBind(SuperViewHolder holder, int viewType, final int layoutPosition, DayHistoryArticleCompound item) {
        switch (viewType) {
            case DayHistoryArticleCompound.Type.date: {
                if (item.getDate() != null) {
                    holder.setText(R.id.tv_date,item.getDate().trim());
                } else {
                    holder.setText(R.id.tv_date,"");
                }
                break;
            }
            case DayHistoryArticleCompound.Type.beauty: {
                final ImageView imageView = holder.findViewById(R.id.iv_beauty);
                imageView.setImageDrawable(null);
                String url = item.getArticle().getUrl();
                if (url != null) {
                    GlideHelper.loadBitmap(getContext(), url, new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                            ViewGroup.LayoutParams params = imageView.getLayoutParams();
                            if (mHeightArray.get(layoutPosition) != null) {
                                params.height = mHeightArray.get(layoutPosition).getHeigh();
//                    params.width  = mHeightArray.get(layoutPosition).getWidth();
                            } else {
                                int vw = imageView.getWidth() - imageView.getPaddingLeft() - imageView.getPaddingRight();
                                float scale = (float) vw / (float) resource.getWidth();
                                int vh = Math.round(resource.getHeight() * scale);
                                params.height = vh + imageView.getPaddingTop() + imageView.getPaddingBottom();
                                ImageViewVH imageViewVH = new ImageViewVH(params.width, params.height);
                                mHeightArray.put(layoutPosition, imageViewVH);
                            }
                            imageView.setLayoutParams(params);
                            imageView.setImageBitmap(resource);
                        }
                    });
                } else {
                    imageView.setImageDrawable(null);
                }
                break;
            }
            default: {
                if (item.getArticle() != null) {
                    holder.setText(R.id.tv_title,item.getArticle().getDesc().trim());
                    holder.setText(R.id.tv_type,item.getArticle().getType().trim());
                } else {
                    holder.setText(R.id.tv_title,"");
                    holder.setText(R.id.tv_type,"");
                }
                break;
            }
        }
    }

    @Override
    protected IMulItemViewType<DayHistoryArticleCompound> offerMultiItemViewType() {
        return new IMulItemViewType<DayHistoryArticleCompound>() {
            @Override
            public int getViewTypeCount() {
                return VIEW_TYPE_COUNT;//=3
            }

            @Override
            public int getItemViewType(int position, DayHistoryArticleCompound dayHistoryArticleCompound) {
                return dayHistoryArticleCompound.getType();
            }

            @Override
            public int getLayoutId(int viewType) {
                switch (viewType) {
                    case DayHistoryArticleCompound.Type.date: {//第一种类型
                        return R.layout.item_homepage_date;
                    }
                    case DayHistoryArticleCompound.Type.beauty: {//第二种类型
                        return R.layout.item_homepage_beauty;
                    }
                    default: {
                        //(我这边只设置3种类型,其他判断过了就默认此种类型)
                        return R.layout.item_homepage_content;//默认的类型
                    }
                }
            }
        };
    }
}
