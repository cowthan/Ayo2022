package org.ayo.editor.emoj.model;

/**
 * Created by lhy on 15/11/7.
 */
public class EmojiModel {
    public static final int TYPE_SMALL = 0;
    public static final int TYPE_BIG = 1;

    public String icon;
    public int type;//表情类型(1为聊天室专用表情)
    public String filename;//文件名
    public String alias;//[大笑]
    public String name;//[daxiao]

    /**
     * 表情包id
     */
    public int pkgId;

}
