package org.ayo.ui.sample.statusbar;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.ayo.sample.R;
import org.ayo.statusbar.StatusBarCompat;

/**
 * Compat CollapsingToolbarLayout
 * Created by qiu on 7/27/16.
 */
public class CollapsingToolbarFragment extends Fragment {

    public CollapsingToolbarFragment() {

    }

    private AppBarLayout mAppBarLayout;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private Toolbar mToolbar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.status_bar_fragment_collapsing_tool_bar, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);

        mAppBarLayout = (AppBarLayout) view.findViewById(R.id.appbar);
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) view.findViewById(R.id.collapsing_toolbar);
        mCollapsingToolbarLayout.setTitle(getString(R.string.app_name));
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        StatusBarCompat.setStatusBarColorForCollapsingToolbar(getActivity(), mAppBarLayout, mCollapsingToolbarLayout, mToolbar, Color.YELLOW);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            StatusBarCompat.setStatusBarColorForCollapsingToolbar(getActivity(), mAppBarLayout, mCollapsingToolbarLayout, mToolbar, Color.YELLOW);
        }
    }
}
