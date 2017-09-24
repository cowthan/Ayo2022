package org.ayo.editor;

import android.app.Application;

import org.ayo.editor.emoj.db.EmojiRepo;
import org.ayo.editor.emoj.model.EmojiCategoryModel;
import org.ayo.editor.emoj.model.EmojiModel;

import java.util.List;

/**
 * Created by qiaoliang on 2017/8/19.
 */

public class MyEditor {

    private MyEditor() {
    }

    private static final class Holder {
        private static final MyEditor instance = new MyEditor();
    }

    public static MyEditor getDefault() {
        return Holder.instance;
    }

    private static Application application;

    private static boolean DEBUG;

    public void init(Application application, boolean isDebug) {
        MyEditor.application = application;
        DEBUG = isDebug;
    }

    public static Application app() {
        return application;
    }

    public static boolean isDebug() {
        return DEBUG;
    }

    private EmojiRepo emojiRepo;

    public void setEmojiRepo(EmojiRepo emojiRepo){
        this.emojiRepo = emojiRepo;
    }

    public EmojiRepo getEmojiRepo(){
        if(emojiRepo == null){
            emojiRepo = new EmojiRepo() {
                @Override
                public List<EmojiModel> queryAllEmojiList() {
                    return null;
                }

                @Override
                public List<EmojiModel> queryEmojiListByPackageId(int pkgId) {
                    return null;
                }

                @Override
                public List<EmojiCategoryModel> queryNormalEmojiList() {
                    return null;
                }
            };
        }
        return this.emojiRepo;
    }

    public static final int UPLOAD_LIMIT_M = 20;
}
