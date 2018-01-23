package org.ayo.fresco;

import android.content.Context;

import com.facebook.imagepipeline.core.ImagePipelineConfig;

import okhttp3.OkHttpClient;

/**
 * Created by hujinghui on 16/11/15.
 */

public class OkHttpImagePipelineConfigFactory {
    public static ImagePipelineConfig.Builder newBuilder(Context context, OkHttpClient okHttpClient) {
        return ImagePipelineConfig.newBuilder(context)
                .setNetworkFetcher(new OkHttpNetworkFetcher(okHttpClient));
    }
}
