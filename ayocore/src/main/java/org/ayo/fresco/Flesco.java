package org.ayo.fresco;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.AbsListView;
import android.widget.ListView;

import com.facebook.binaryresource.BinaryResource;
import com.facebook.binaryresource.FileBinaryResource;
import com.facebook.cache.common.CacheKey;
import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.common.internal.Supplier;
import com.facebook.common.memory.MemoryTrimType;
import com.facebook.common.memory.MemoryTrimmable;
import com.facebook.common.memory.NoOpMemoryTrimmableRegistry;
import com.facebook.common.soloader.SoLoaderShim;
import com.facebook.common.util.ByteConstants;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.cache.DefaultCacheKeyFactory;
import com.facebook.imagepipeline.cache.MemoryCacheParams;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.core.ImagePipelineFactory;
import com.facebook.imagepipeline.decoder.ProgressiveJpegConfig;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.image.ImmutableQualityInfo;
import com.facebook.imagepipeline.image.QualityInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.getkeepsafe.relinker.ReLinker;

import org.ayo.AppCore;
import org.ayo.core.Lang;
import org.ayo.core.R;
import org.ayo.file.FilePath;
import org.ayo.http.OkHttp3BuilderManager;
import org.ayo.http.dns.HttpDnsUtils;

import java.io.File;
import java.util.concurrent.Executor;

import okhttp3.OkHttpClient;

/**
 * Created by qiaoliang on 2017/6/23.
 */

public class Flesco {

    public static void initFresco(Context context) {
        ProgressiveJpegConfig pjpegConfig = new ProgressiveJpegConfig() {
            @Override
            public int getNextScanNumberToDecode(int scanNumber) {
                return scanNumber + 2;
            }

            public QualityInfo getQualityInfo(int scanNumber) {
                boolean isGoodEnough = (scanNumber >= 5);
                return ImmutableQualityInfo.of(scanNumber, isGoodEnough, false);
            }
        };

        // 内存配置
        final MemoryCacheParams bitmapCacheParams = new MemoryCacheParams(20 * 1024 * 1024, // 内存缓存中总图片的最大大小,以字节为单位。
                Integer.MAX_VALUE, // 内存缓存中图片的最大数量。
                20 * 1024 * 1024, // 内存缓存中准备清除但尚未被删除的总图片的最大大小,以字节为单位。
                Integer.MAX_VALUE, // 内存缓存中准备清除的总图片的最大数量。
                Integer.MAX_VALUE); // 内存缓存中单个图片的最大大小。

        // 修改内存图片缓存数量，空间策略（这个方式有点恶心）
        Supplier<MemoryCacheParams> mSupplierMemoryCacheParams = new Supplier<MemoryCacheParams>() {
            @Override
            public MemoryCacheParams get() {
                return bitmapCacheParams;
            }
        };
        NoOpMemoryTrimmableRegistry.getInstance().registerMemoryTrimmable(new MemoryTrimmable() {
            @Override
            public void trim(MemoryTrimType trimType) {
                final double suggestedTrimRatio = trimType.getSuggestedTrimRatio();
                if (MemoryTrimType.OnCloseToDalvikHeapLimit
                        .getSuggestedTrimRatio() == suggestedTrimRatio
                        || MemoryTrimType.OnSystemLowMemoryWhileAppInBackground
                        .getSuggestedTrimRatio() == suggestedTrimRatio
                        || MemoryTrimType.OnSystemLowMemoryWhileAppInForeground
                        .getSuggestedTrimRatio() == suggestedTrimRatio
                        || MemoryTrimType.OnAppBackgrounded
                        .getSuggestedTrimRatio() == suggestedTrimRatio) {
                    ImagePipelineFactory.getInstance().getImagePipeline().clearMemoryCaches();
                }
            }
        });
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        if (OkHttp3BuilderManager.isDnsEnabled) {
            clientBuilder.addInterceptor(HttpDnsUtils.getDnsInterceptor());
        }
        //if (BaseApplication.DEBUG)
        //StethoHelper.addNetworkInterceptor(clientBuilder);
        final ImagePipelineConfig config = OkHttpImagePipelineConfigFactory.newBuilder(context, clientBuilder.build())
                .setProgressiveJpegConfig(pjpegConfig)
//                .setNetworkFetcher(new HttpUrlConnectionNetworkFetcher())
                .setBitmapMemoryCacheParamsSupplier(mSupplierMemoryCacheParams)
                .setMainDiskCacheConfig(
                        DiskCacheConfig.newBuilder(context).setBaseDirectoryName("image_cache")
                                .setBaseDirectoryPath(new File(FilePath.getPicturePath()))
                                .setMaxCacheSize(100 * ByteConstants.MB)
                                .setMaxCacheSizeOnLowDiskSpace(50 * ByteConstants.MB)
                                .setMaxCacheSizeOnVeryLowDiskSpace(20 * ByteConstants.MB).build())
                .build();
        // 尝试解决动态库加载失败问题
        // bug：java.lang.NoClassDefFoundError: com/facebook/imagepipeline/memory/NativeMemoryChunk
        SoLoaderShim.setHandler(new SoLoaderShim.Handler() {
            @Override
            public void loadLibrary(String libraryName) {
                ReLinker.loadLibrary(AppCore.app(), libraryName);
            }
        });
        Fresco.initialize(context, config);
    }

    public static void shutDown() {
        Fresco.shutDown();
    }

    public static void clearMemoryCache() {
        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        imagePipeline.clearMemoryCaches();
    }

    public static boolean hasDiskCache(String url) {
        if (Lang.isEmpty(url)) {
            return false;
        }
        CacheKey cacheKey = DefaultCacheKeyFactory.getInstance().getEncodedCacheKey(
                ImageRequest.fromUri(toUri(url)),null);
        return ImagePipelineFactory.getInstance().getMainFileCache().hasKey(cacheKey)
                || ImagePipelineFactory.getInstance().getSmallImageFileCache()
                .hasKey(cacheKey);
    }

    public static File getDiskCache(String url) {
        File localFile = null;
        if (Lang.isNotEmpty(url)) {
            CacheKey cacheKey = DefaultCacheKeyFactory.getInstance().getEncodedCacheKey(
                    ImageRequest.fromUri(toUri(url)),null);
            if (ImagePipelineFactory.getInstance().getMainFileCache().hasKey(cacheKey)) {
                BinaryResource resource = ImagePipelineFactory.getInstance()
                        .getMainFileCache().getResource(cacheKey);
                localFile = ((FileBinaryResource) resource).getFile();
            } else if (ImagePipelineFactory.getInstance().getSmallImageFileCache()
                    .hasKey(cacheKey)) {
                BinaryResource resource = ImagePipelineFactory.getInstance()
                        .getSmallImageFileCache().getResource(cacheKey);
                localFile = ((FileBinaryResource) resource).getFile();
            }
        }
        return localFile;
    }



    ///=============================================================
    ///
    ///=============================================================
    private static Uri toUri(String url) {
        if (TextUtils.isEmpty(url))
            return Uri.parse("");
        return Uri.parse(url);
    }

    public static DraweeController getGifController(String url) {
        return Fresco.newDraweeControllerBuilder().setUri(toUri(url))
                .setAutoPlayAnimations(true).build();
    }

    public static void setImageUri(SimpleDraweeView iv, String uri){
        iv.setController(getGifController(uri));
    }

    /**
     <com.dongqiudi.news.view.fresco.zoomable.ZoomableDraweeView xmlns:android="http://schemas.android.com/apk/res/android"
         xmlns:fresco="http://schemas.android.com/apk/res-auto"
         android:id="@+id/picShow"
         android:layout_width="match_parent"
         android:layout_height="match_parent"
         fresco:actualImageScaleType="fitCenter"
         fresco:backgroundImage="@color/white"
         fresco:failureImage="@drawable/message_pic_bg" />
     * @param zoomView
     * @param rawUri
     * @param thumbUri
     */
    public static void setGalleryImageUri(SimpleDraweeView zoomView, String rawUri, String thumbUri){
        PipelineDraweeControllerBuilder builder = Fresco.newDraweeControllerBuilder();
        ImageRequestBuilder imageRequest = ImageRequestBuilder.newBuilderWithSource(toUri(rawUri));
        builder.setOldController(zoomView.getController()).setTapToRetryEnabled(true)
                .setImageRequest(imageRequest.build()).setAutoPlayAnimations(false)
                .setControllerListener(new BaseControllerListener<ImageInfo>() {
                    @Override
                    public void onFinalImageSet(String id, ImageInfo imageInfo,
                                                Animatable animatable) {
                        super.onFinalImageSet(id, imageInfo, animatable);
                        if (animatable != null)
                            animatable.start();
                    }
                });
        if (Lang.isNotEmpty(thumbUri)){
            builder.setLowResImageRequest(ImageRequestBuilder.newBuilderWithSource(toUri(thumbUri)).build());
        }

        DraweeController controller = builder.build();

        zoomView.setController(controller);
        GenericDraweeHierarchy hierarchy = new GenericDraweeHierarchyBuilder(AppCore.app().getResources())
                .setActualImageScaleType(ScalingUtils.ScaleType.FIT_CENTER)
                .setProgressBarImage(getImageLoadProgress())
                .setRetryImage(Lang.rdrawable(R.drawable.load_failed_retry))
                .setFailureImage(Lang.rdrawable(R.drawable.load_failed)).build();

        zoomView.setHierarchy(hierarchy);
//        zoomView.setOnSingleClickListener(new ZoomableDraweeView.onSingleClickListener() {
//            @Override
//            public void onSingleClick(View v) {
//                ShowPicActivity.this.finish();
//            }
//        });
    }

    public static CircularProgressDrawable getImageLoadProgress() {
        CircularProgressDrawable progressDrawable = new CircularProgressDrawable.Builder()
                .setRingWidth(Lang.dip2px(6)).setOutlineColor(0x33FFFFFF)
                .setRingColor(0xffffffff)
                .setCenterColor(Color.TRANSPARENT)
                .setWidth(Lang.dip2px(50)).setHeight(Lang.dip2px(50))
                .setPadding(Lang.dip2px(6)).create();
        progressDrawable.setIndeterminate(false);
        progressDrawable.setProgress(0);
        return progressDrawable;
    }

    //===================================
    public static boolean isGifImage(String path) {
        if (TextUtils.isEmpty(path)) {
            return false;
        }
        String p = path.toLowerCase();
        return p.endsWith(".gif") || p.contains(".gif!") || p.contains(".gif-")
                || p.contains(".gif_");
    }


    //===================================
    // 需要暂停网络请求时调用
    public static void pause(){
        Fresco.getImagePipeline().pause();
    }

    // 需要恢复网络请求时调用
    public static void resume(){
        Fresco.getImagePipeline().resume();
    }

    public static void handleFrescoWithListView(ListView listView){
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch(scrollState){
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE://空闲状态
                        resume();
                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_FLING://滚动状态
                        pause();
                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL://触摸后滚动
                        break;
                }
            }
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }


    private static final Executor UiExecutor = new Executor() {
        final Handler sHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(Runnable runnable) {
            sHandler.post(runnable);
        }
    };


    public static void handleFrescoWithListView(RecyclerView r){
        r.addOnScrollListener(new RecyclerView.OnScrollListener() {

            private boolean sIsScrolling = false;
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING || newState == RecyclerView.SCROLL_STATE_SETTLING) {
                    sIsScrolling = true;
                    pause();
                } else if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (sIsScrolling == true) {
                        resume();
                    }
                    sIsScrolling = false;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }
}
