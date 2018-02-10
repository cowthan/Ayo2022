package org.ayo.template.status;


import android.content.Context;
import android.view.View;

import org.ayo.kit.R;


/**
 * Created by Administrator on 2016/8/21.
 */
public class DefaultStatusProvider {

    public static class DefaultLoadingStatusView extends StatusProvider {

        public DefaultLoadingStatusView(Context context, String status, View contentView, OnStatusViewCreateCallback callback) {
            super(context, status, contentView, callback);
        }

        @Override
        public View getStatusView() {
            return View.inflate(mContext, R.layout.genius_view_loading, null);
        }
    }

    public static class DefaultEmptyStatusView extends StatusProvider{

        public DefaultEmptyStatusView(Context context, String status, View contentView, OnStatusViewCreateCallback callback) {
            super(context, status, contentView, callback);
        }

        @Override
        public View getStatusView() {
            return View.inflate(mContext, R.layout.genius_view_empty, null);
        }
    }

    public static class DefaultServerErrorStatusView extends StatusProvider{

        public DefaultServerErrorStatusView(Context context, String status, View contentView, OnStatusViewCreateCallback callback) {
            super(context, status, contentView, callback);
        }

        @Override
        public View getStatusView() {
            return View.inflate(mContext, R.layout.genius_view_error_server, null);
        }
    }

    public static class DefaultNetOffStatusView extends StatusProvider{

        public DefaultNetOffStatusView(Context context, String status, View contentView, OnStatusViewCreateCallback callback) {
            super(context, status, contentView, callback);
        }

        @Override
        public View getStatusView() {
            return View.inflate(mContext, R.layout.genius_view_error_server, null);
        }
    }

    public static class DefaultLogicFailStatusView extends StatusProvider{

        public DefaultLogicFailStatusView(Context context, String status, View contentView, OnStatusViewCreateCallback callback) {
            super(context, status, contentView, callback);
        }

        @Override
        public View getStatusView() {
            return View.inflate(mContext, R.layout.genius_view_error_local, null);
        }
    }

    public static class DefaultLocalErrorStatusView extends StatusProvider{

        public DefaultLocalErrorStatusView(Context context, String status, View contentView, OnStatusViewCreateCallback callback) {
            super(context, status, contentView, callback);
        }

        @Override
        public View getStatusView() {
            return View.inflate(mContext, R.layout.genius_view_error_local, null);
        }
    }

}
