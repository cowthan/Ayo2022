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
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.ContextThemeWrapper;

import org.ayo.hover.wrapper.R;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.LinkedHashMap;
import java.util.Map;

import io.mattcarroll.hover.HoverMenuAdapter;
import io.mattcarroll.hover.NavigatorContent;
import io.mattcarroll.hover.defaulthovermenu.window.HoverMenuService;
import io.mattcarroll.hover.hoverdemo.DemoHoverMenuAdapter;
import io.mattcarroll.hover.hoverdemo.theming.HoverTheme;

/**
 * Demo {@link HoverMenuService}.
 */
public class AssistantHoverService extends HoverMenuService {

    private static final String TAG = "DemoHoverMenuService";

    public static void showFloatingMenu(Context context) {
        context.startService(new Intent(context, AssistantHoverService.class));
    }

    private DemoHoverMenuAdapter mDemoHoverMenuAdapter;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected Context getContextForHoverMenu() {
        return new ContextThemeWrapper(this, R.style.AppTheme);
    }

    @Override
    protected HoverMenuAdapter createHoverMenuAdapter() {

        Map<String, NavigatorContent> demoMenu = new LinkedHashMap<>();
        demoMenu.put("log view", new AssistLogView(this));
        demoMenu.put("action view", new AssistMonitorView(this));
        demoMenu.put("monitor view", new AssistBlockView(this));
        demoMenu.put("setting view", new AssistLogView(this));
        demoMenu.put("custom view", new AssistLogView(this));

        return new AssistHoverMenuAdapter(this, demoMenu);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(@NonNull HoverTheme newTheme) {
        mDemoHoverMenuAdapter.setTheme(newTheme);
    }

}
