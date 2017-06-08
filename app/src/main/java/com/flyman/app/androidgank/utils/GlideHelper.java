package com.flyman.app.androidgank.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.flyman.app.util.R;
import com.flyman.app.util.glide.GlideCircleTransform;
import com.flyman.app.util.glide.GlideRoundTransform;

import java.util.concurrent.ExecutionException;


/**
 * @author Flyman
 * @ClassName GlideHelper
 * @description 图片加载了类助手 使用glide
 * @date 2017-4-11 23:41
 */
public class GlideHelper {

//    /**
//     * 加载静态图
//     *
//     * @param
//     * @return nothing
//     */
//    public static void loadBitmap2FixXY(Context mContext, ImageView view, String url) {
//        Glide.with(mContext).load(url).asBitmap().error(R.drawable.drawable_pic_placeholder).placeholder(R.drawable.drawable_pic_placeholder).into(view);
//    }

    /**
     * 加载静态图
     *
     * @param
     * @return nothing
     */
    public static void loadBitmap(Context mContext, String url, SimpleTarget simpleTarget) {
        Glide.with(mContext)
                .load(url)
                .asBitmap()
                .fitCenter()
                .dontAnimate()
                .dontTransform()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.drawable_pic_placeholder)
                .error(R.drawable.drawable_pic_placeholder)
                .into(simpleTarget);
    }

    /**
     * 加载静态图
     *
     * @param
     * @return nothing
     */
    public static void loadBitmap(Context mContext, String url, SimpleTarget simpleTarget,int width,int heigh) {
        Glide.with(mContext)
                .load(url)
                .asBitmap()
                .centerCrop()
                .dontTransform()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.drawable_pic_placeholder)
                .error(R.drawable.drawable_pic_placeholder)
                .override(width,heigh)
                .into(simpleTarget);
    }

    /**
     * 加载静态图
     *
     * @param
     * @return nothing
     */
    public static void loadBitmap(Context mContext, String url,ImageView imageView) {
        Glide.with(mContext)
                .load(url)
                .crossFade()
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.drawable.drawable_pic_placeholder)
                .placeholder(R.drawable.drawable_pic_placeholder)
                .into(imageView);
    }

    /**
     * 加载静态图
     *
     * @param
     * @return nothing
     */
    public static void loadAsBitmap(Context mContext, String url,ImageView imageView) {
        Glide.with(mContext)
                .load(url)
                .asBitmap()
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.drawable.drawable_pic_placeholder)
                .placeholder(R.drawable.drawable_pic_placeholder)
                .into(imageView);
    }

    /**
     * 获得图片的宽高
     *
     * @param
     * @return nothing
     */
    public static Bitmap getBitmap(Context mContext, String url) {
        try {
            return Glide.with(mContext)
                    .load(url)
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .get();
        } catch (InterruptedException e) {
            return null;
        } catch (ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 加载静态图
     *
     * @param
     * @return nothing
     */
    public static void loadBitmapWithNoPlaceHolder(Context mContext, String url, final ImageView imageView) {
        Glide.with(mContext)
                .load(url)
                .asBitmap()
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.drawable_pic_placeholder)
                .error(R.drawable.drawable_pic_placeholder)
                .dontAnimate()
                .into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {

                    @Override
                    public void onResourceReady(Bitmap bitmap, GlideAnimation glideAnimation) {
                        // Do something with bitmap here.
                        int height = bitmap.getHeight(); //获取bitmap信息，可赋值给外部变量操作，也可在此时行操作。
                        int width = bitmap.getWidth();
                        ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
                        layoutParams.height = height;
                        layoutParams.width = width;
                        imageView.setLayoutParams(layoutParams);
                    }

                });
    }

    /**
     * 加载静态图
     *
     * @param
     * @return nothing
     */
    public static void loadBitmap2FixXY(Context mContext ,String url, final ImageView imageView) {
        Glide.with(mContext)
                .load(url)
                .asBitmap()
                .error(R.drawable.drawable_pic_placeholder)
                .placeholder(R.drawable.drawable_pic_placeholder)
                .listener(new RequestListener<String, Bitmap>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        if (imageView == null) {
                            return false;
                        }
                        if (imageView.getScaleType() != ImageView.ScaleType.FIT_XY) {
                            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                        }
                        ViewGroup.LayoutParams params = imageView.getLayoutParams();
                        int vw = imageView.getWidth() - imageView.getPaddingLeft() - imageView.getPaddingRight();
                        float scale = (float) vw / (float) resource.getWidth();
                        int vh = Math.round(resource.getHeight() * scale);
                        params.height = vh + imageView.getPaddingTop() + imageView.getPaddingBottom();
                        imageView.setLayoutParams(params);
                        return false;
                    }
                })
                .into(imageView);
    }




    /**
     * 加载圆角静态图
     *
     * @param
     * @return nothing
     */
    public static void loadRoundBitmap(Context mContext, ImageView view, String url, int roundDp) {
        Glide.with(mContext).load(url).asBitmap().error(R.drawable.drawable_pic_placeholder).transform(new GlideRoundTransform(mContext, roundDp)).into(view);
    }

    /**
     * 加载圆形静态图
     *
     * @param
     * @return nothing
     */
    public static void loadCircleBitmap(Context mContext, ImageView view, String url) {
        Glide.with(mContext).load(url).asBitmap().error(R.drawable.drawable_pic_circle_placeholder).placeholder(R.drawable.drawable_pic_circle_placeholder).transform(new GlideCircleTransform(mContext)).into(view);
    }

    /**
     * 加载动态图
     * 需要设置缓存diskCacheStrategy(DiskCacheStrategy.NONE)
     * 那应该是因为你没有配置diskCacheStrategy，加载gif图一定要把diskCacheStrategy设置成NONE，或者是SOURCE，
     * 不配不行，因为不配默认就是ALL，这种情况下会把GIF图的每一帧都去压缩然后缓存，时间极长，可能要几分钟gif图才会显示出来
     *
     * @param
     * @return nothing
     */
    public static void loadGif(Context mContext, ImageView view, String url) {
        Glide.with(mContext).load(url).asGif().diskCacheStrategy(DiskCacheStrategy.NONE).into(view);
    }


}
