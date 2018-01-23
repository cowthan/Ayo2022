package org.ayo.mall.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.ButtonBarLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.core.BaseActivity;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.listener.SimpleMultiPurposeListener;
import com.scwang.smartrefresh.layout.util.DensityUtil;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.UIUtil;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import org.ayo.mall.R;
import org.ayo.mall.adapter.ComFragmentAdapter;
import org.ayo.mall.fragment.ArticleFragment;
import org.ayo.mall.fragment.DynamicFragment;
import org.ayo.mall.fragment.QuestionFragment;
import org.ayo.mall.util.ScreenUtil;
import org.ayo.mall.util.StatusBarUtil;
import org.ayo.mall.view.ColorFlipPagerTitleView;
import org.ayo.mall.view.JudgeNestedScrollView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class HellMainActivity extends BaseActivity {
    public static final String TAG = HellMainActivity.class.getSimpleName();
    ImageView ivHeader;
    CircleImageView ivAvatar;
    TextView tvUsername;
    TextView tvMajor;
    TextView tvGender;
    TextView tvLevelNum;
    TextView tvCompany;
    TextView tvPosition;
    TextView tvFollowNum;
    TextView tvFansNum;
    TextView tvIntroduce;
    TextView tvAuthentication;
    TextView tvEditInfo;
    TextView tvIntegralNum;
    TextView tvJapaneseCurrency;
    TextView tvPrestige;
    TextView tvFriendliness;
    TextView tvLabelOne;
    TextView tvLabelTwo;
    TextView tvLabelThree;
    TextView tvEditLabel;
    CollapsingToolbarLayout collapse;
    ViewPager viewPager;
    JudgeNestedScrollView scrollView;
    SmartRefreshLayout refreshLayout;
    ImageView ivBack;
    CircleImageView toolbarAvatar;
    TextView toolbarUsername;
    ButtonBarLayout buttonBarLayout;
    ImageView ivMenu;
    Toolbar toolbar;
    MagicIndicator magicIndicator;
    MagicIndicator magicIndicatorTitle;
    private int mOffset = 0;
    private int mScrollY = 0;
    int toolBarPositionY = 0;
    private String[] mTitles = new String[]{"动态", "文章", "问答"};
    private List<String> mDataList = Arrays.asList(mTitles);


    private void initView() {

        ivHeader = findViewById(R.id.iv_header);
        ivAvatar = findViewById(R.id.iv_avatar);
        tvUsername = findViewById(R.id.tv_username);
        tvMajor = findViewById(R.id.tv_major);
        tvGender = findViewById(R.id.tv_gender);
        tvLevelNum = findViewById(R.id.tv_level_num);
        tvCompany = findViewById(R.id.tv_company);
        tvPosition = findViewById(R.id.tv_position);
        tvFollowNum = findViewById(R.id.tv_follow_num);
        tvFansNum = findViewById(R.id.tv_fans_num);
        tvIntroduce = findViewById(R.id.tv_introduce);
        tvAuthentication = findViewById(R.id.tv_authentication);
        tvEditInfo = findViewById(R.id.tv_edit_info);
        tvIntegralNum = findViewById(R.id.tv_integral_num);
        tvJapaneseCurrency = findViewById(R.id.tv_japanese_currency);
        tvPrestige = findViewById(R.id.tv_prestige);
        tvFriendliness = findViewById(R.id.tv_friendliness);
        tvLabelOne = findViewById(R.id.tv_label_one);
        tvLabelTwo = findViewById(R.id.tv_label_two);
        tvLabelThree = findViewById(R.id.tv_label_three);
        tvEditLabel = findViewById(R.id.tv_edit_label);
        collapse = findViewById(R.id.collapse);
        viewPager = findViewById(R.id.view_pager);
        scrollView = findViewById(R.id.scrollView);
        refreshLayout = findViewById(R.id.refreshLayout);
        ivBack = findViewById(R.id.iv_back);
        toolbarAvatar = findViewById(R.id.toolbar_avatar);
        toolbarUsername = findViewById(R.id.toolbar_username);
        buttonBarLayout = findViewById(R.id.buttonBarLayout);
        ivMenu = findViewById(R.id.iv_menu);
        toolbar = findViewById(R.id.toolbar);
        magicIndicator = findViewById(R.id.magic_indicator);
        magicIndicatorTitle = findViewById(R.id.magic_indicator_title);
        StatusBarUtil.immersive(this);
        StatusBarUtil.setPaddingSmart(this, toolbar);


        refreshLayout.setOnMultiPurposeListener(new SimpleMultiPurposeListener() {
            @Override
            public void onHeaderPulling(RefreshHeader header, float percent, int offset, int bottomHeight, int extendHeight) {
                mOffset = offset / 2;
                ivHeader.setTranslationY(mOffset - mScrollY);
                toolbar.setAlpha(1 - Math.min(percent, 1));
            }

            @Override
            public void onHeaderReleasing(RefreshHeader header, float percent, int offset, int bottomHeight, int extendHeight) {
                mOffset = offset / 2;
                ivHeader.setTranslationY(mOffset - mScrollY);
                toolbar.setAlpha(1 - Math.min(percent, 1));
            }
        });

        toolbar.post(new Runnable() {

            @Override
            public void run() {
                toolBarPositionY = toolbar.getHeight();
                Log.d(TAG, "----------------toolBarPositionY------------:" + toolBarPositionY);
                ViewGroup.LayoutParams params = viewPager.getLayoutParams();
//                int screenHeight = getApplicationContext().getResources().getDisplayMetrics().widthPixels;
                int screenHeight = ScreenUtil.getScreenHeightPx(getApplicationContext());
                params.height = ScreenUtil.getScreenHeightPx(getApplicationContext()) - toolBarPositionY - magicIndicator.getHeight() + 1;
                Log.d(TAG, "----------------screenHeight------------:" + screenHeight);
                Log.d(TAG, "----------------magicIndicator.getHeight()------------:" + magicIndicator.getHeight());
                viewPager.setLayoutParams(params);
            }
        });
        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            int lastScrollY = 0;
            int h = DensityUtil.dp2px(170);
            int color = ContextCompat.getColor(getApplicationContext(), R.color.mainWhite) & 0x00ffffff;

            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                int[] location = new int[2];
                magicIndicator.getLocationOnScreen(location);
                int xPosition = location[0];
                int yPosition = location[1];
                if (yPosition < toolBarPositionY) {
                    magicIndicatorTitle.setVisibility(View.VISIBLE);
                    scrollView.setNeedScroll(false);
                    Log.d(TAG, "---------magicIndicatorTitle----:" + false);
                } else {
                    magicIndicatorTitle.setVisibility(View.GONE);
                    Log.d(TAG, "---------magicIndicatorTitle----:" + true);
                    scrollView.setNeedScroll(true);

                }

                if (lastScrollY < h) {
                    scrollY = Math.min(h, scrollY);
                    mScrollY = scrollY > h ? h : scrollY;
                    buttonBarLayout.setAlpha(1f * mScrollY / h);
                    toolbar.setBackgroundColor(((255 * mScrollY / h) << 24) | color);
                    ivHeader.setTranslationY(mOffset - mScrollY);
                }
                if (scrollY == 0) {
                    ivBack.setImageResource(R.drawable.pn_back_white);
                    ivMenu.setImageResource(R.drawable.pn_icon_menu_white);
                } else {
                    ivBack.setImageResource(R.drawable.pn_back_black);
                    ivMenu.setImageResource(R.drawable.pn_icon_menu_black);
                }

                lastScrollY = scrollY;
            }
        });
        buttonBarLayout.setAlpha(0);
        toolbar.setBackgroundColor(0);

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(DynamicFragment.getInstance());
        fragments.add(ArticleFragment.getInstance());
        fragments.add(QuestionFragment.getInstance());
        viewPager.setAdapter(new ComFragmentAdapter(getSupportFragmentManager(), fragments));
        viewPager.setOffscreenPageLimit(10);
        initMagicIndicator();
        initMagicIndicatorTitle();
    }

    private void initMagicIndicator() {
        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setScrollPivotX(0.65f);
        commonNavigator.setAdjustMode(true);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return mDataList == null ? 0 : mDataList.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                SimplePagerTitleView simplePagerTitleView = new ColorFlipPagerTitleView(context);
                simplePagerTitleView.setText(mDataList.get(index));
                simplePagerTitleView.setNormalColor(ContextCompat.getColor(HellMainActivity.this, R.color.mainBlack));
                simplePagerTitleView.setSelectedColor(ContextCompat.getColor(HellMainActivity.this, R.color.mainBlack));
                simplePagerTitleView.setTextSize(16);
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewPager.setCurrentItem(index, false);
                    }
                });
                return simplePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setMode(LinePagerIndicator.MODE_EXACTLY);
                indicator.setLineHeight(UIUtil.dip2px(context, 2));
                indicator.setLineWidth(UIUtil.dip2px(context, 20));
                indicator.setRoundRadius(UIUtil.dip2px(context, 3));
                indicator.setStartInterpolator(new AccelerateInterpolator());
                indicator.setEndInterpolator(new DecelerateInterpolator(2.0f));
                indicator.setColors(ContextCompat.getColor(HellMainActivity.this, R.color.mainRed));
                return indicator;
            }
        });
        magicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(magicIndicator, viewPager);
    }

    private void initMagicIndicatorTitle() {
        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setScrollPivotX(0.65f);
        commonNavigator.setAdjustMode(true);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return mDataList == null ? 0 : mDataList.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                SimplePagerTitleView simplePagerTitleView = new ColorFlipPagerTitleView(context);
                simplePagerTitleView.setText(mDataList.get(index));
                simplePagerTitleView.setNormalColor(ContextCompat.getColor(HellMainActivity.this, R.color.mainBlack));
                simplePagerTitleView.setSelectedColor(ContextCompat.getColor(HellMainActivity.this, R.color.mainBlack));
                simplePagerTitleView.setTextSize(16);
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewPager.setCurrentItem(index, false);
                    }
                });
                return simplePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setMode(LinePagerIndicator.MODE_EXACTLY);
                indicator.setLineHeight(UIUtil.dip2px(context, 2));
                indicator.setLineWidth(UIUtil.dip2px(context, 20));
                indicator.setRoundRadius(UIUtil.dip2px(context, 3));
                indicator.setStartInterpolator(new AccelerateInterpolator());
                indicator.setEndInterpolator(new DecelerateInterpolator(2.0f));
                indicator.setColors(ContextCompat.getColor(HellMainActivity.this, R.color.mainRed));
                return indicator;
            }
        });
        magicIndicatorTitle.setNavigator(commonNavigator);
        ViewPagerHelper.bind(magicIndicatorTitle, viewPager);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.pn_ac_home;
    }

    @Override
    protected void onCreate2(View contentView, @Nullable Bundle savedInstanceState) {
        initView();
    }

    @Override
    protected void onDestroy2() {

    }

    @Override
    protected void onPageVisibleChanged(boolean visible, boolean isFirstTimeVisible, @Nullable Bundle savedInstanceState) {

    }
}
