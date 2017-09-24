package org.ayo.editor.emoj.engine;

import android.app.Application;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.os.Handler;
import android.os.StatFs;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import org.ayo.editor.MyEditor;
import org.ayo.editor.MyEmojiLoader;
import org.ayo.editor.MyEventBus;

import java.io.File;
import java.io.IOException;

/**
 * Created by qiaoliang on 2017/8/19.
 */

public class EmojiUtils {

    /**
     * 是否有足够空间存储表情包
     *
     * @return
     */
    public static boolean isStoreEmojiEnable() {
        long size = getSDFreeSize();
        if (size >= 0 && size < 1024 * 200) {
            return false;
        }
        return true;
    }

    /**
     * 获取SD卡空余空间
     *
     * @return 返回剩余空间 单位：byte （-1：表示失败）
     */
    public static long getSDFreeSize() {
        if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            return -1;
        }
        // 取得SD卡文件路径
        File path = Environment.getExternalStorageDirectory();
        StatFs sf = new StatFs(path.getPath());
        // 获取单个数据块的大小(Byte)
        long blockSize = sf.getBlockSize();
        // 空闲的数据块的数量
        long freeBlocks = sf.getAvailableBlocks();
        // 返回SD卡空闲大小
        return (freeBlocks * blockSize); // 单位byte
    }



    public static void hideSoftInput(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    // 显示虚拟键盘
    public static void showSoftInput(Context context, View v) {
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        imm.showSoftInput(v, InputMethodManager.SHOW_FORCED);
    }




    private EmojiUtils() {
    }

    private static final class Holder {
        private static final EmojiUtils instance = new EmojiUtils();
    }

    public static EmojiUtils getDefault() {
        return Holder.instance;
    }

    private static Handler mHandler = new Handler();

    private EmojiPackagesInfoObserver mEmojiPackagesInfoObserver;

    private EmojiInfoDoneObserver mEmojiInfoDoneObserver;

    private EmojiInfoDeleteObserver mEmojiInfoDeleteObserver;

    private static ExpressionParser mExpressionParser;

    private Runnable mEmojiPackagesInfoRunnable = new Runnable() {
        @Override
        public void run() {
            MyEmojiLoader.startDownloadSmallEmojiPackage(MyEditor.app());
        }
    };

    private Runnable mEmojiInfoDoneRunnable = new Runnable() {
        @Override
        public void run() {
            MyEmojiLoader.startInitExpressionParser(MyEditor.app());
            MyEventBus.getDefault().post(new ExpressionParser.EmojiDownLoadDoneEvent());
        }
    };

    private Runnable mEmojiInfoDeleteRunnable = new Runnable() {
        @Override
        public void run() {
        }
    };

    public static void initExpressionParser() {
        if (mExpressionParser == null)
            mExpressionParser = ExpressionParser.getInstance();
        else
            mExpressionParser.init();
    }

    public void init(Application app) {
    }

    private class EmojiPackagesInfoObserver extends android.database.ContentObserver {
        private EmojiPackagesInfoObserver() {
            super(mHandler);
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            mHandler.removeCallbacks(mEmojiPackagesInfoRunnable);
            mHandler.postDelayed(mEmojiPackagesInfoRunnable, 20);
        }
    }

    private class EmojiInfoDoneObserver extends android.database.ContentObserver {
        private EmojiInfoDoneObserver() {
            super(mHandler);
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            mHandler.removeCallbacks(mEmojiInfoDoneRunnable);
            mHandler.postDelayed(mEmojiInfoDoneRunnable, 20);
        }
    }

    private class EmojiInfoDeleteObserver extends android.database.ContentObserver {
        private EmojiInfoDeleteObserver() {
            super(mHandler);
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            mHandler.removeCallbacks(mEmojiInfoDeleteRunnable);
            mHandler.postDelayed(mEmojiInfoDeleteRunnable, 20);
        }
    }

    public static void replaceEmojiTextView(TextView tv, CharSequence text) {
        tv.setText(replaceEmoji(text, tv.getTextSize()));
    }

    public static CharSequence replaceEmoji(CharSequence text, float fontSize) {
        if (mExpressionParser == null) {
            mExpressionParser = ExpressionParser.getInstance();
        }
        return mExpressionParser.replace(text, fontSize);
    }

    public static CharSequence replaceEmojiTextView(CharSequence text, float textSize) {
        if (mExpressionParser == null) {
            mExpressionParser = ExpressionParser.getInstance();
        }
        return mExpressionParser.replace(text, textSize);
    }

    public static String getBigEmojiPath(String name) {
        if (mExpressionParser == null) {
            mExpressionParser = ExpressionParser.getInstance();
        }
        return mExpressionParser.queryBigExpressionFilePath(name);
    }

    public void release() {
        ExpressionParser.release();
    }


    public static Bitmap createBitmapByLocalUri(String uri){
        Bitmap bm = null;
        if(uri.startsWith("file:///android_asset/")){
            uri = uri.replace("file:///android_asset/", "");
            AssetManager am = MyEditor.app().getAssets();
            try {
                bm = BitmapFactory.decodeStream(am.open(uri));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if(uri.startsWith("file://")){
            uri = uri.replace("file://", "");
            bm = BitmapFactory.decodeFile(uri);
        }else{
            bm = BitmapFactory.decodeFile(uri);
        }
        return bm;
    }

    public static Drawable createDrawableByLocalUri(String uri){
        Drawable drawable = null;
        if(uri.startsWith("file:///android_asset/")){
            uri = uri.replace("file:///android_asset/", "");
            AssetManager am = MyEditor.app().getAssets();
            try {
                Bitmap bm = BitmapFactory.decodeStream(am.open(uri));
                drawable = new BitmapDrawable(bm);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if(uri.startsWith("file://")){
            uri = uri.replace("file://", "");
            drawable= Drawable.createFromPath(uri);
        }else{
            drawable= Drawable.createFromPath(uri);
        }
        return drawable;
    }

}
