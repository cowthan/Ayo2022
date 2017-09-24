package org.ayo.fringe.ui.photo;

import android.app.Activity;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.imagepipeline.image.ImageInfo;

import org.ayo.core.Lang;
import org.ayo.fringe.R;
import org.ayo.notify.toaster.Toaster;
import org.ayo.fringe.ui.base.BaseFrgFragment;
import org.ayo.fringe.ui.base.Pages;
import org.ayo.fringe.widget.photo.PhotoDraweeView;

import java.util.ArrayList;
import java.util.List;


public class AyoGalleryFragment extends BaseFrgFragment {

    public static void start(Activity c, ArrayList<? extends IImageInfo> images, int currentPosition){
        Bundle b = new Bundle();
        b.putSerializable("images", images);
        b.putInt("currentPosition", currentPosition);
        Pages.startWithSwipeback(c, AyoGalleryFragment.class, b);
    }

    public interface IImageInfo{
        String getUri();
        String getLocalUri();
    }

    private List<IImageInfo> images;


    @Override
    protected int getLayoutId() {
        return R.layout.ayo_frag_gallery;
    }

    @Override
    protected void onCreate2(View view, @Nullable Bundle bundle) {
        images = (List<IImageInfo>) getArguments().getSerializable("images");
        MultiTouchViewPager viewPager = (MultiTouchViewPager) findViewById(R.id.view_pager);
        viewPager.setAdapter(new DraweePagerAdapter(getActivity(), images));

        final TextView page_number = (TextView) findViewById(R.id.page_number);
        page_number.setText(currentPosition + "/" + Lang.count(images));

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentPosition = position + 1;
                page_number.setText(currentPosition + "/" + images.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        ImageView download = (ImageView) findViewById(R.id.download);
        download.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Toaster.toastShort("下载");
            }
        });

        viewPager.setCurrentItem(getArguments().getInt("currentPosition"));
    }

    @Override
    protected void onDestroy2() {

    }

    @Override
    protected void onPageVisibleChanged(boolean b, boolean b1, @Nullable Bundle bundle) {

    }

    int currentPosition = 1;

    public class DraweePagerAdapter extends PagerAdapter {

        private List<IImageInfo> images;
        private Activity mActivity;

        public DraweePagerAdapter(Activity mActivity, List<IImageInfo> images) {
            this.images = images;
            this.mActivity = mActivity;
        }

        @Override public int getCount() {
            return images == null ? 0 : images.size();
        }

        @Override public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override public Object instantiateItem(ViewGroup viewGroup, int position) {

            IImageInfo bean = images.get(position);
            final PhotoDraweeView photoDraweeView = new PhotoDraweeView(viewGroup.getContext());
            PipelineDraweeControllerBuilder controller = Fresco.newDraweeControllerBuilder();
            controller.setUri(Uri.parse(bean.getUri()));
            controller.setOldController(photoDraweeView.getController());
            controller.setControllerListener(new BaseControllerListener<ImageInfo>() {
                @Override
                public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
                    super.onFinalImageSet(id, imageInfo, animatable);
                    if (imageInfo == null) {
                        return;
                    }
                    photoDraweeView.update(imageInfo.getWidth(), imageInfo.getHeight());

                    if(animatable != null){
                        animatable.start();
                    }
                }
            });
            photoDraweeView.setController(controller.build());

            try {
                viewGroup.addView(photoDraweeView, ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return photoDraweeView;
        }
    }
}
