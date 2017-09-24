package org.ayo.imagepicker.album;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import org.ayo.imagepicker.MyTitleView;
import org.ayo.imagepicker.MyToaster;
import org.ayo.imagepicker.PhotoListActivity;
import org.ayo.imagepicker.R;
import org.ayo.imagepicker.ThumbModel;

import java.util.ArrayList;

/**
 * 图片查看器 wl
 */
public class PhotoShowActivity extends AppCompatActivity implements View.OnClickListener {

    private ArrayList<ThumbModel> mThumbModels;

    private int mPosition;

    /**
     * 当前选中了几张
     */
    private int mNum;

    public static final String EXTRA_ALBUM_PATHS = "extra_album_paths";

    public static final String EXTRA_ALBUM_POSITION = "extra_album_position";

    public static final String EXTRA_ALBUM_NUM = "extra_album_num";

    private ViewPagerFixed mViewPager;

    private TextView mCompleteView;

    private ThumbModel mCurrentModel;

    private int mMaxCount = 6;

    /**
     * @param position 当前位置
     */
    public static Intent getIntent(Context context, ArrayList<ThumbModel> models, int position, int maxCount, int num) {
        Intent intent = new Intent(context, PhotoShowActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(EXTRA_ALBUM_PATHS, models);
        bundle.putInt(EXTRA_ALBUM_POSITION, position);
        bundle.putInt(PhotoListActivity.PIC_TOTAL_FLAG, maxCount);
        bundle.putInt(EXTRA_ALBUM_NUM, num);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.picker_ac_photo_show);
        mThumbModels = getIntent().getParcelableArrayListExtra(EXTRA_ALBUM_PATHS);
        if (mThumbModels != null && mThumbModels.size() > 0)
            mThumbModels.remove(0);
        mPosition = getIntent().getIntExtra(EXTRA_ALBUM_POSITION, 0);
        mNum = getIntent().getIntExtra(EXTRA_ALBUM_NUM, 0);
        mMaxCount = getIntent().getIntExtra(PhotoListActivity.PIC_TOTAL_FLAG, mMaxCount);
        initTitleView();
        mViewPager = (ViewPagerFixed) findViewById(R.id.viewpager);
        mCompleteView = (TextView) findViewById(R.id.tv_bottom_complete);
        mCompleteView.setOnClickListener(this);
        completeViewUpdate();
        mViewPager.setAdapter(new PhotoSlidePagerAdapter(getSupportFragmentManager()));
        mViewPager.setCurrentItem(mPosition);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                mTitleView.setTitle(String.valueOf(position + 1) + "/" + mThumbModels.size());
                mPosition = position;
                mTitleView.setRightButton(mThumbModels.get(mPosition).isSelect());
                mCurrentModel = mThumbModels.get(position);
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void setSelected(boolean selected) {
        mTitleView.setRightButton(selected);
    }

    MyTitleView mTitleView;
    private void initTitleView() {
        mTitleView = (MyTitleView) findViewById(R.id.titlebar_layout);
        mTitleView.setLeftButton(R.drawable.picker_return_btn_style);
        mTitleView.setRightButton(mThumbModels.get(mPosition).isSelect());
        mTitleView.setTitle(String.valueOf(mPosition + 1) + "/" + mThumbModels.size());
        mTitleView.setTitleViewListener(new MyTitleView.BaseTitleViewListener() {
            @Override
            public void onLeftClicked() {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList(EXTRA_ALBUM_PATHS, mThumbModels);
                intent.putExtras(bundle);
                setResult(RESULT_CANCELED, intent);
                finish();
            }

            @Override
            public void onTitleClicked() {
                super.onTitleClicked();

            }

            @Override
            public void onRightClicked() {
                if (mNum >= mMaxCount && !mCurrentModel.isSelect()) {
                    MyToaster.toastShort(getString(R.string.picker_max_pic, mMaxCount));
                    return;
                }
                mCurrentModel.setSelect(!mCurrentModel.isSelect());
                mTitleView.setRightButton(mCurrentModel.isSelect());
                if (mCurrentModel.isSelect()) {
                    mNum++;
                } else {
                    mNum--;
                }
                completeViewUpdate();
            }
        });
    }


    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.tv_bottom_complete){
            if (mNum == 0 && mThumbModels.size() > mPosition) {
                mThumbModels.get(mPosition).setSelect(true);
            }
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList(EXTRA_ALBUM_PATHS, mThumbModels);
            intent.putExtras(bundle);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    private class PhotoSlidePagerAdapter extends FragmentStatePagerAdapter {

        PhotoSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return PhotoSlideFragment.newInstance(mThumbModels.get(position));
        }

        @Override
        public int getCount() {
            return mThumbModels.size();
        }
    }

    public void completeViewUpdate() {
        if (mNum == 0) {
            mCompleteView.setText(getString(R.string.picker_complete));
        } else {
            mCompleteView.setText(getString(R.string.picker_finish_num, mNum + ""));
        }
    }
}
