/*
 * Copyright 2016 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.ayo.appsist;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.View;

import org.ayo.hover.wrapper.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.mattcarroll.hover.HoverMenuAdapter;
import io.mattcarroll.hover.NavigatorContent;
import io.mattcarroll.hover.hoverdemo.hovermenu.ui.DemoTabView;

/**
 * Demo implementation of a {@link HoverMenuAdapter}.
 */
public class AssistHoverMenuAdapter implements HoverMenuAdapter {


    private final Context mContext;
    private final List<String> mTabIds;
    private final List<Integer> tabIcons;
    private final Map<String, NavigatorContent> mData;
    private final Set<ContentChangeListener> mContentChangeListeners = new HashSet<>();

    public AssistHoverMenuAdapter(@NonNull Context context, @NonNull Map<String, NavigatorContent> data){
        mContext = context;
        mData = data;

        mTabIds = new ArrayList<>();
        for (String tabId : mData.keySet()) {
            mTabIds.add(tabId);
        }

        tabIcons = new ArrayList<>();
        tabIcons.add(R.drawable.ic_orange_circle);
        tabIcons.add(R.drawable.ic_paintbrush);
        tabIcons.add(R.drawable.ic_stack);
        tabIcons.add(R.drawable.ic_menu);
        tabIcons.add(R.drawable.ic_pen);
    }


    @Override
    public int getTabCount() {
        return mTabIds.size();
    }

    @Override
    public View getTabView(int index) {
        return createTabView(index, tabIcons.get(index));
    }

    @Override
    public long getTabId(int position) {
        return position;
    }

    @Override
    public NavigatorContent getNavigatorContent(int index) {
        String tabId = mTabIds.get(index);
        return mData.get(tabId);
    }

    @Override
    public void addContentChangeListener(@NonNull ContentChangeListener listener) {
        mContentChangeListeners.add(listener);
    }

    @Override
    public void removeContentChangeListener(@NonNull ContentChangeListener listener) {
        mContentChangeListeners.remove(listener);
    }

    protected void notifyDataSetChanged() {
        for (ContentChangeListener listener : mContentChangeListeners) {
            listener.onContentChange(this);
        }
    }

    private View createTabView(int index, @DrawableRes int tabBitmapRes) {

        Resources resources = mContext.getResources();
        int padding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, resources.getDisplayMetrics());

        int backgroundColor = ContextCompat.getColor(mContext, R.color.hover_accent);
        Integer iconColor = (index == 0 ? null : ContextCompat.getColor(mContext, R.color.hover_base));

        DemoTabView view = new DemoTabView(mContext, resources.getDrawable(R.drawable.tab_background), resources.getDrawable(tabBitmapRes));
        view.setTabBackgroundColor(backgroundColor);
        view.setTabForegroundColor(iconColor);
        view.setPadding(padding, padding, padding, padding);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            view.setElevation(padding);
        }
        return view;

//        ImageView view = new ImageView(mContext);
//        view.setImageResource(tabBitmapRes);
//        view.setPadding(16, 16, 16, 16);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            view.setElevation(16);
//        }
//        return view;
    }
}
