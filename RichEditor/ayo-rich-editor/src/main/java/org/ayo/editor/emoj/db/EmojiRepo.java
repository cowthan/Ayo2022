package org.ayo.editor.emoj.db;

import org.ayo.editor.emoj.model.EmojiCategoryModel;
import org.ayo.editor.emoj.model.EmojiModel;

import java.util.List;

/**
 * Created by Administrator on 2017/8/21.
 */

public interface EmojiRepo {
    List<EmojiModel> queryAllEmojiList();

    List<EmojiModel> queryEmojiListByPackageId(int pkgId);

    List<EmojiCategoryModel> queryNormalEmojiList();

}
