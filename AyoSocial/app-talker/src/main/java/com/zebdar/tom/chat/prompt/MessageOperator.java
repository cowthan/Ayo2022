package com.zebdar.tom.chat.prompt;

import android.app.Activity;

import com.zebdar.tom.chat.MessageCenter;
import com.zebdar.tom.chat.model.IMMsg;
import com.zebdar.tom.speech.SpeechReader;

import org.ayo.core.Lang;
import org.ayo.notify.toaster.Toaster;

/**
 * Created by Administrator on 2017/7/20.
 */

public class MessageOperator {
    /**
     * 带复制文本的操作
     */
    public static void clip(final Activity activity, final IMMsg msg) {
        new ActionSheetBottomDialog(activity)
                .builder()
                .addSheetItem("复制", ActionSheetBottomDialog.SheetItemColor.Blue, new ActionSheetBottomDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        Lang.copyToClipboard(msg.getContent());
                        Toaster.toastShort("已复制到剪切板");
                    }
                })
                .addSheetItem("朗读", ActionSheetBottomDialog.SheetItemColor.Blue, new ActionSheetBottomDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        SpeechReader.getDefault().speech(msg.getContent());
                    }
                })
                .addSheetItem("删除", ActionSheetBottomDialog.SheetItemColor.Blue, new ActionSheetBottomDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        MessageCenter.getDefault().delete(msg);
                    }
                })
                .show();
    }

    /**
     * 仅有删除操作
     */
    public static void delonly(final Activity activity, final IMMsg msg) {
        new ActionSheetBottomDialog(activity)
                .builder()
                .addSheetItem("删除", ActionSheetBottomDialog.SheetItemColor.Blue, new ActionSheetBottomDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        MessageCenter.getDefault().delete(msg);
                    }
                })
                .show();
    }

}
