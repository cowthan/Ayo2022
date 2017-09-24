
package org.ayo.editor.emoj.engine;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.widget.TextView;

import org.ayo.editor.MyEditor;
import org.ayo.editor.emoj.model.EmojiCategoryModel;
import org.ayo.editor.emoj.model.EmojiModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 为Textview添加表情 Created by lhy on 15/11/5.
 */
public class ExpressionParser {

    private static ExpressionParser mExpressionParser;

    public static ExpressionParser getInstance() {
        if (null == mExpressionParser) {
            mExpressionParser = new ExpressionParser();
        }
        return mExpressionParser;
    }

    private Context mContext;

    private Pattern mPattern;

    private Map<String,String> mSmallEmojiValues;

    private Map<String,String> mBigEmojiValues;

    private Map<String,Drawable> memoryCache =new HashMap<>();

    private ExpressionParser() {
        mContext = MyEditor.app();
        init();
    }

    public void init() {
        if (mSmallEmojiValues != null)
            mSmallEmojiValues.clear();
        if (mBigEmojiValues != null)
            mBigEmojiValues.clear();
        List<EmojiModel> list = MyEditor.getDefault().getEmojiRepo().queryAllEmojiList();
        setupEmojiValues(list);
        mPattern = buildPattern(list);
    }

    public Map<String,Drawable> getMemoryCache(){
        if(memoryCache == null)
            memoryCache = new HashMap<>();
        return memoryCache;
    }

    // 构建正则表达式
    private Pattern buildPattern(List<EmojiModel> list) {
        if (list == null || list.isEmpty())
            return null;
        StringBuilder patternString = new StringBuilder(list.size() * 3);
        patternString.append('(');

        for (int i = 0; i < list.size(); i++) {
            patternString.append(Pattern.quote(list.get(i).alias));
            patternString.append('|');

        }
        patternString.replace(patternString.length() - 1, patternString.length(), ")");
        Pattern p = null;
        try {
            p = Pattern.compile(patternString.toString());
        } catch (Exception e) {
             e.printStackTrace();
        }
        return p;
    }

    /**
     * 获取表情个数
     * @param text
     * @return true
     */
    public boolean getExpressionTotal(CharSequence text){
        int expressionTotal = 0;
        Matcher matcher = mPattern.matcher(text);
        while (matcher.find() && expressionTotal < EmojiConst.EXPRESSION_TOTAL_MAX) {
            expressionTotal++;
        }
        if (expressionTotal < EmojiConst.EXPRESSION_TOTAL_MAX) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 根据文本替换成图片
     **/
    public CharSequence replace(CharSequence text, float textSize) {
        // 防止发送内容为空造成奔溃
        if (text == null)
            return text;
        //清空缓存，避免表情的大小和textview的textsize不一致
        if(memoryCache ==null){
            memoryCache =new HashMap<>();
        }
        memoryCache.clear();
        SpannableStringBuilder builder = new SpannableStringBuilder(text);
        if (mPattern == null) {
            return text;
        }
        if(mSmallEmojiValues==null){
            List<EmojiModel> list = MyEditor.getDefault().getEmojiRepo().queryAllEmojiList();
            setupEmojiValues(list);
        }
        int expressionTotal = 0;
        Matcher matcher = mPattern.matcher(text);
        while (matcher.find() &&  expressionTotal < EmojiConst.EXPRESSION_TOTAL_MAX) {
            String resId = mSmallEmojiValues.get(matcher.group());
            // 这里根据字体大小设置图片的大小,自适应
            Drawable drawable = getCacheExpression(resId);
//            Drawable drawable = Drawable.createFromPath(resId);
            if (drawable == null)
                return text;
            drawable.setBounds(0, 0, (int) (textSize * 1.5), (int) (textSize * 1.5));
            VerticalCenterImageSpan imageSpan = new VerticalCenterImageSpan(drawable);

            builder.setSpan(imageSpan, matcher.start(), matcher.end(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            expressionTotal++;
        }
        return builder;
    }

    private Drawable getCacheExpression(String resId){
        Drawable drawable = getMemoryCache().get(resId);
        if(drawable == null){
            drawable = EmojiUtils.createDrawableByLocalUri(resId);
            if(drawable!=null){
                getMemoryCache().put(resId,drawable);
            }
        }
        return drawable;
    }

    public void replace(TextView tv, CharSequence text) {
        tv.setText(replace(text, tv.getTextSize()));
    }

    public String queryBigExpressionFilePath(String name) {
        if(mBigEmojiValues == null)
            return null;
        return mBigEmojiValues.get(name);
    }

    private void setupEmojiValues(List<EmojiModel> models) {
        if (models == null || models.isEmpty())
            return;
        if (mSmallEmojiValues == null)
            mSmallEmojiValues = new HashMap<>();
        else
            mSmallEmojiValues.clear();
        if (mBigEmojiValues == null)
            mBigEmojiValues = new HashMap<>();
        else
            mBigEmojiValues.clear();
        for (EmojiModel model : models) {
            if (model == null)
                continue;
            if (model.type == EmojiModel.TYPE_SMALL) {
                mSmallEmojiValues.put(model.alias, model.icon);
            } else {
                mBigEmojiValues.put(model.alias, model.icon);
            }
        }
    }

    public static void release() {
        if (mExpressionParser == null)
            return;
        if (mExpressionParser.mSmallEmojiValues != null)
            mExpressionParser.mSmallEmojiValues.clear();
        if (mExpressionParser.mBigEmojiValues != null)
            mExpressionParser.mSmallEmojiValues.clear();
        if (mExpressionParser.memoryCache != null)
            mExpressionParser.memoryCache.clear();
        mExpressionParser.mSmallEmojiValues = null;
        mExpressionParser.mBigEmojiValues = null;
        mExpressionParser.memoryCache = null;
        mExpressionParser = null;
    }

    public static List<EmojiCategoryModel> loadNormalEmojiList() {
        return MyEditor.getDefault().getEmojiRepo().queryNormalEmojiList();
    }

    public static class EmojiDownLoadDoneEvent{
    }

}
