package com.flyman.app.androidgank.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.flyman.app.androidgank.R;
import com.flyman.app.androidgank.model.bean.DayHistoryArticleCompound;
import com.flyman.app.androidgank.model.bean.ImageViewVH;
import com.flyman.app.androidgank.utils.GlideHelper;
import com.flyman.app.util.log.LogUtils;

import java.util.List;


public class HomepageAdapter extends RecyclerView.Adapter {
    private View.OnClickListener mOnClickListener1;
    private View.OnClickListener mOnClickListener2;
    private View.OnClickListener mOnClickListener3;
    private List<DayHistoryArticleCompound> mList;
    private Context mContext;
    private SparseArray<ImageViewVH> mHeightArray;

    public HomepageAdapter(Context mContext, List<DayHistoryArticleCompound> list) {
        this.mList = list;
        this.mContext = mContext;
        mHeightArray = new SparseArray<>();
    }


    @Override
    public int getItemViewType(int position) {
        if (mList != null || mList.size() > 0) {
            int type = mList.get(position).getType();
            switch (type) {
                case DayHistoryArticleCompound.Type.date: {
                    return DayHistoryArticleCompound.Type.date;
                }
                case DayHistoryArticleCompound.Type.beauty: {
                    return DayHistoryArticleCompound.Type.beauty;
                }
                default: {
                    return type;
                }
            }
        }
        return super.getItemViewType(position);
    }

    public void setOnitemClickListener(View.OnClickListener onClickListener1, View.OnClickListener onClickListener2, View.OnClickListener onClickListener3) {
        this.mOnClickListener1 = onClickListener1;
        this.mOnClickListener2 = onClickListener2;
        this.mOnClickListener3 = onClickListener3;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LogUtils.e("Thread123", viewType);
        switch (viewType) {
            case DayHistoryArticleCompound.Type.date: {
                View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_homepage_date, parent, false);
                itemView.setOnClickListener(mOnClickListener1);
                return new DateViewHolder(itemView);
            }
            case DayHistoryArticleCompound.Type.beauty: {
                View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_homepage_beauty, parent, false);
                itemView.setOnClickListener(mOnClickListener2);
                return new BeautyViewHolder(itemView);
            }
            default: {
                View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_homepage_content, parent, false);
                itemView.setOnClickListener(mOnClickListener3);
                return new AtricleViewHolder(itemView);
            }
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof AtricleViewHolder) {
            AtricleViewHolder mHolder = (AtricleViewHolder) holder;
            holder.itemView.setTag(position);
            if (mList.get(position).getArticle() != null) {
                mHolder.tv_title.setText(mList.get(position).getArticle().getDesc());
                mHolder.tv_type.setText(mList.get(position).getArticle().getType());
            } else {
                mHolder.tv_title.setText("");
                mHolder.tv_type.setText("");
            }

        } else if (holder instanceof DateViewHolder) {
            DateViewHolder mHolder = (DateViewHolder) holder;
            holder.itemView.setTag(position);
            if (mList.get(position).getDate() != null) {
                mHolder.tv_date.setText(mList.get(position).getDate());
            } else {
                mHolder.tv_date.setText("");
            }

        } else if (holder instanceof BeautyViewHolder) {
            final BeautyViewHolder mHolder = (BeautyViewHolder) holder;
            holder.itemView.setTag(position);
            final ImageView imageView = mHolder.iv_beauty;
            String url = mList.get(position).getArticle().getUrl();
            if (url != null) {
                GlideHelper.loadBitmap(mContext, mList.get(position).getArticle().getUrl(), new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                        ViewGroup.LayoutParams params = imageView.getLayoutParams();
                        if (mHeightArray.get(position) != null) {
                            params.height = mHeightArray.get(position).getHeigh();
//                    params.width  = mHeightArray.get(layoutPosition).getWidth();
                        } else {
                            int vw = imageView.getWidth() - imageView.getPaddingLeft() - imageView.getPaddingRight();
                            float scale = (float) vw / (float) resource.getWidth();
                            int vh = Math.round(resource.getHeight() * scale);
                            params.height = vh + imageView.getPaddingTop() + imageView.getPaddingBottom();
                            ImageViewVH imageViewVH = new ImageViewVH(params.width, params.height);
                            mHeightArray.put(position, imageViewVH);
                        }
                        imageView.setLayoutParams(params);
                        imageView.setImageBitmap(resource);
                    }
                });
            } else {
                mHolder.iv_beauty.setImageDrawable(null);
            }


        }


    }

    @Override
    public int getItemCount() {
        return mList == null || mList.size() == 0 ? 0 : mList.size();
    }

    public class AtricleViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_title;
        public TextView tv_type;

        public AtricleViewHolder(View itemView) {
            super(itemView);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_type = (TextView) itemView.findViewById(R.id.tv_type);
        }

    }

    public class DateViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_date;

        public DateViewHolder(View itemView) {
            super(itemView);
            tv_date = (TextView) itemView.findViewById(R.id.tv_date);
        }


    }

    public class BeautyViewHolder extends RecyclerView.ViewHolder {
        public ImageView iv_beauty;

        public BeautyViewHolder(View itemView) {
            super(itemView);
            iv_beauty = (ImageView) itemView.findViewById(R.id.iv_beauty);
        }


    }
}
