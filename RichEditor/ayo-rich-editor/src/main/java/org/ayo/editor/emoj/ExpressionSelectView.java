package org.ayo.editor.emoj;


import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import org.ayo.editor.MyEmojiLoader;
import org.ayo.editor.MyEventBus;
import org.ayo.editor.MyImageLoader;
import org.ayo.editor.R;
import org.ayo.editor.emoj.engine.ExpressionParser;
import org.ayo.editor.emoj.model.EmojiCategoryModel;
import org.ayo.editor.emoj.model.EmojiModel;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


/**
 * 表情选择view Created by lishuai on 2015/11/06.
 */
public class ExpressionSelectView extends FrameLayout implements ViewPager.OnPageChangeListener {
    public static final int MAX_COUNT_SMALL_EXPRESSION=21;

    public static final int MAX_COUNT_BIG_EXPRESSION=8;

    public static final String EXPRESSION_TYPE_SMALL="small";

    public static final String EXPRESSION_TYPE_BIG="big";

    private Context mContext;

    // 表情容器
    private ViewPager mVpExpressionContainer;

    // tab 容器
    // private RadioGroup mRgTabContainer;

    private LinearLayout mLLTabContainer;

    private IndicatorView mIndicatorView;

    private HorizontalScrollView mHorizontalScrollView;

    // view pager所有的gridview列表
    private LinkedList<View> mAllGridViewList;

    private ExpressionPagerAdapter mExpressionPagerAdapter;

    private OnExpressionClickListener mOnExpressionClickListener;

    private List<EmojiModel> resList1;

    private String mType=EXPRESSION_TYPE_SMALL;

    public int mCurrentItem;

    public ExpressionSelectView(Context context) {
        this(context, null);
    }

    public ExpressionSelectView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        MyEmojiLoader.startCheckExpressions(getContext());
        MyEventBus.getDefault().register(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        MyEventBus.getDefault().unregister(this);
    }

    private void init() {
        LayoutInflater.from(mContext).inflate(R.layout.layout_expression, this, true);
        mVpExpressionContainer = (ViewPager)findViewById(R.id.vp_expression);
        // mRgTabContainer = (RadioGroup)findViewById(R.id.rg_tab_container);
        mLLTabContainer = (LinearLayout)findViewById(R.id.ll_tab_container);
        mIndicatorView = (IndicatorView)findViewById(R.id.indicator_view);
        mHorizontalScrollView = (HorizontalScrollView)findViewById(R.id.horizontal_scrollview);
        mVpExpressionContainer.addOnPageChangeListener(this);
        mAllGridViewList = new LinkedList<>();
        mExpressionPagerAdapter = new ExpressionPagerAdapter(mAllGridViewList);
        resList1 = new ArrayList<>();
        mVpExpressionContainer.setAdapter(mExpressionPagerAdapter);

    }


    /**
     * 初始化表情
     * @return
     */
    public boolean initExpressionPackages() {
        List<EmojiCategoryModel> data = ExpressionParser.loadNormalEmojiList();
        if (data == null || data.isEmpty())
            return false;
        for (int i = 0; i < data.size(); i++) {
            addExpressionPackage(data.get(i), i == 0);
        }
        return true;
    }

    public void addExpressionPackage(EmojiCategoryModel model, boolean isSelected) {
        if (model == null) {
            return;
        }
        ExpressionTabModel expressionTabModel = getExpressionModel(model);
        if (model.data == null || model.data.isEmpty()) {
            ExpressionEmptyView emptyView = (ExpressionEmptyView) LayoutInflater.from(getContext())
                    .inflate(R.layout.view_expression_empty, null);
            emptyView.setTag(expressionTabModel);
            emptyView.setData(model);
            mAllGridViewList.add(emptyView);
        } else {
            this.mType = model.type == 0 ? EXPRESSION_TYPE_SMALL : EXPRESSION_TYPE_BIG;
            mAllGridViewList.addAll(getGridChildViewList(model.data, expressionTabModel));
        }
        mExpressionPagerAdapter.notifyDataSetChanged();
        if (isSelected) {
            expressionTabModel.ev.setSelected(true);
            mIndicatorView.setCount(expressionTabModel.pageCount);
            mIndicatorView.setSelect(0);
        }
    }

    private ExpressionTabModel getExpressionModel(EmojiCategoryModel model) {
        ExpressionTabModel expressionTabModel = new ExpressionTabModel();
        expressionTabModel.name = model.name;
        expressionTabModel.childExpression = model.data;
        expressionTabModel.icon = model.icon;
        expressionTabModel.ev = getTabView2(expressionTabModel, mAllGridViewList.size());
        expressionTabModel.startPos = mAllGridViewList.size();
        expressionTabModel.endPos = mAllGridViewList.size() + expressionTabModel.pageCount - 1;
        mLLTabContainer.addView(expressionTabModel.ev);
        return expressionTabModel;
    }

    private ExpressionTabView getTabView2(ExpressionTabModel model, final int startPos) {
        final ExpressionTabView tv = new ExpressionTabView(mContext);
        tv.getTextView().setText(model.name);
        MyImageLoader.setImageUri(tv.getImageView(), model.icon);

        tv.setOnTabClickListener(new ExpressionTabView.OnClickListener() {

            @Override
            public void onclick() {
                mVpExpressionContainer.setCurrentItem(startPos);
                mIndicatorView.setSelect(0);
            }
        });
        return tv;
    }

    public int getViewPagerCount() {
        return mAllGridViewList == null || mAllGridViewList.isEmpty() ? 0 : mAllGridViewList.size();
    }

    /**
     * 得到单个表情包gridview的列表,处理小表情
     *
     * @param resList
     * @param expressionTabModel
     * @return
     */
    private List<View> getGridChildViewList(List<EmojiModel> resList,
                                            ExpressionTabModel expressionTabModel) {
        List<View> list = new ArrayList<>();
        if(EXPRESSION_TYPE_BIG.equals(mType)){
            resList1.addAll (resList);
            expressionTabModel.pageCount = (int) Math.ceil(resList.size() / (float)MAX_COUNT_BIG_EXPRESSION);
            for (int i = 1; i <= expressionTabModel.pageCount; i++) {
                ArrayList<EmojiModel> subList = new ArrayList<>();
                for (int j = MAX_COUNT_BIG_EXPRESSION * (i - 1); j < MAX_COUNT_BIG_EXPRESSION * i && j < resList.size(); j++) {
                    subList.add(resList.get(j));
                }
                list.add(getGridChildView(subList, expressionTabModel));
            }
        }else if(EXPRESSION_TYPE_SMALL.equals(mType)){
            int temp = 1;
            int size = resList.size();
            while (temp * 20 < size) {
                EmojiModel emojiModel = new EmojiModel();
                emojiModel.alias = "icon_gv_delete";
                emojiModel.filename = "icon_gv_delete";
                resList.add(20 + MAX_COUNT_SMALL_EXPRESSION * (temp - 1), emojiModel);
                temp++;
            }

            if (resList.size() % MAX_COUNT_SMALL_EXPRESSION != 0) {
                EmojiModel emojiModel = new EmojiModel();
                emojiModel.filename = "icon_gv_delete";
                emojiModel.alias = "icon_gv_delete";
                resList.add(resList.size(), emojiModel);
            }
            resList1.addAll (resList);
            expressionTabModel.pageCount = (int) Math.ceil(resList.size() / (float)MAX_COUNT_SMALL_EXPRESSION);
            for (int i = 1; i <= expressionTabModel.pageCount; i++) {
                ArrayList<EmojiModel> subList = new ArrayList<>();
                for (int j = MAX_COUNT_SMALL_EXPRESSION * (i - 1); j < MAX_COUNT_SMALL_EXPRESSION * i && j < resList.size(); j++) {
                    subList.add(resList.get(j));
                }
                list.add(getGridChildView(subList, expressionTabModel));
            }
        }
        return list;
    }

    /**
     * @param list
     * @return
     */
    private View getGridChildView(final ArrayList<EmojiModel> list,
                                  ExpressionTabModel expressionTabModel) {
        View view = View.inflate(mContext, R.layout.expression_gridview, null);
        // 关键代码,让每个页面都保存ExpressionModel的引用,当viewpager页面变化与tab view进行联动.
        view.setTag(expressionTabModel);
        ExpandGridView gv = (ExpandGridView)view.findViewById(R.id.gridview);
        final ExpressionAdapter expressionAdapter;
        if (EXPRESSION_TYPE_SMALL.equals(mType)) {
            gv.setNumColumns(7);
            expressionAdapter = new ExpressionAdapter(mContext, 1, list, EXPRESSION_TYPE_SMALL);
        } else if (EXPRESSION_TYPE_BIG.equals(mType)) {
            expressionAdapter = new ExpressionAdapter(mContext, 1, list, EXPRESSION_TYPE_BIG);
            gv.setNumColumns(4);
        } else {
            expressionAdapter = new ExpressionAdapter(mContext, 1, list);
        }
        gv.setAdapter(expressionAdapter);
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mOnExpressionClickListener != null) {
                    String filename = expressionAdapter.getItem(position).alias;
                    if (filename.equals("icon_gv_delete")) {
                        mOnExpressionClickListener.OnExpressionClickDelete();
                    } else {
                        mOnExpressionClickListener.OnExpressionClick(expressionAdapter
                                .getItem(position));
                    }

                }
            }
        });
        return view;
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(final int pos) {
        final ExpressionTabModel expressionTabModel = (ExpressionTabModel)mAllGridViewList.get(pos)
                .getTag();
        expressionTabModel.ev.setSelected(true);
        mIndicatorView.setCount(expressionTabModel.pageCount);

        mIndicatorView.setSelect(pos - expressionTabModel.startPos);

        calcScroll1(expressionTabModel.ev);
        mCurrentItem=mVpExpressionContainer.getCurrentItem();
    }

    private void calcScroll(RadioButton tab) {
        float width = tab.getMeasuredWidth();
        float left = tab.getLeft();
        float x = left + width / 2 - getWidth() / 2;
        mHorizontalScrollView.smoothScrollTo((int)x, 0);

    }

    private void calcScroll1(ExpressionTabView tab) {
        float width = tab.getMeasuredWidth();
        float left = tab.getLeft();
        float x = left + width / 2 - getWidth() / 2;
        mHorizontalScrollView.smoothScrollTo((int)x, 0);

    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    /**
     * 表情有刷新的回调
     * @param event
     */
    public void onEventMainThread(ExpressionParser.EmojiDownLoadDoneEvent event) {
        removeAllEmojiPackage();
        init();
        initExpressionPackages();
    }

    public interface OnExpressionClickListener {

        void OnExpressionClick(EmojiModel model);

        void OnExpressionClickDelete();
    }

    static class ExpressionTabModel {

        int startPos;

        int endPos;

        int pageCount;

        // 这里可以表情包的id, 也可以是表情包名, 得根据数据结构来确定
        String name;

        List<EmojiModel> childExpression;// <filename,code>

        // RadioButton tab;
        // TextView tv;
        ExpressionTabView ev;

        public String icon;
    }

    public void setOnExpressionClickListener(OnExpressionClickListener listener) {
        this.mOnExpressionClickListener = listener;
    }

    public void removeAllEmojiPackage(){
        removeAllViews();
    }
}