package com.flyman.app.util.bitmap;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * Created by maning on 16/3/9.
 * <p/>
 * 图片相关的工具类
 */
public class BitmapUtil {

    public static boolean saveBitmapToSD(Bitmap bitmap, String dir, String name, boolean isShowPhotos,Context mContext) {
        File path = new File(dir);
        if (!path.exists()) {
            path.mkdirs();
        }
        File file = new File(path + "/" + name);
        if (file.exists()) {
            file.delete();
        }
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        } else {
            return true;
        }
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100,
                    fileOutputStream);
            fileOutputStream.flush();

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                fileOutputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // 其次把文件插入到系统图库
        if (isShowPhotos) {
            try {
                MediaStore.Images.Media.insertImage(mContext.getContentResolver(),
                        file.getAbsolutePath(), name, null);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            // 最后通知图库更新
            mContext.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file)));
        }

        return true;
    }

    public static boolean saveResToSD(Context context, int resID, String dir, String name,Context mContext) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resID);
        return saveBitmapToSD(bitmap, dir, name, false,mContext);
    }

}
