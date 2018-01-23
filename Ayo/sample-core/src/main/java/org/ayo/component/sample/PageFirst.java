package org.ayo.component.sample;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;

import org.ayo.component.MasterFragment;
import org.ayo.component.StateModel;
import org.ayo.sample.menu.notify.ToasterDebug;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import me.yokeyword.sample.demo_wechat.adapter.ChatAdapter;
import me.yokeyword.sample.demo_wechat.entity.Chat;
import me.yokeyword.sample.demo_wechat.event.TabSelectedEvent;
import me.yokeyword.sample.demo_wechat.listener.OnItemClickListener;
import me.yokeyword.sample.demo_wechat.ui.fragment.MainFragment;

/**
 * Created by Administrator on 2017/1/5.
 */

public class PageFirst extends MasterFragment {

    private static final String TAG = "Fragmentation";

    private Toolbar mToolbar;
    private SwipeRefreshLayout mRefreshLayout;
    private RecyclerView mRecy;

    private boolean mInAtTop = true;
    private int mScrollTotal;

    private ChatAdapter mAdapter;


    @Override
    protected int getLayoutId() {
        return R.layout.wechat_fragment_tab_first;
    }

    @Override
    protected StateModel createStateModel() {
        return null;
    }

    @Override
    protected void onCreate2(View view, @Nullable Bundle savedInstanceState) {
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh_layout);
        mRecy = (RecyclerView) view.findViewById(R.id.recy);

        EventBus.getDefault().register(this);

        mToolbar.setTitle("首页1");
        initToolbarMenu(mToolbar);

        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                onRefresh2();
            }
        });

        mRecy.setLayoutManager(new LinearLayoutManager(_mActivity));
        mRecy.setHasFixedSize(true);
        final int space = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0.5f, getResources().getDisplayMetrics());
        mRecy.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.set(0, 0, 0, space);
            }
        });
        mAdapter = new ChatAdapter(_mActivity);
        mRecy.setAdapter(mAdapter);

        mRecy.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mScrollTotal += dy;
                if (mScrollTotal <= 0) {
                    mInAtTop = true;
                } else {
                    mInAtTop = false;
                }
            }
        });

        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position, View view, RecyclerView.ViewHolder vh) {
                // 因为启动的MsgFragment是MainFragment的兄弟Fragment,所以需要MainFragment.start()

                // 这里我使用EventBus通知父MainFragment处理跳转(接耦),
                //EventBus.getDefault().post(new StartBrotherEvent(MsgFragment.newInstance(mAdapter.getMsg(position))));
                ToasterDebug.toastShort("click");
                // 也可以像使用getParentFragment()的方式,拿到父Fragment的引用来操作 (不建议)
//              ((MainFragment) getParentFragment()).startMsgBrother(MsgFragment.newInstance());
            }
        });

    }

    protected void initToolbarMenu(Toolbar toolbar) {
        toolbar.inflateMenu(R.menu.hierarchy);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_hierarchy:
                        _mActivity.showFragmentStackHierarchyView();
                        _mActivity.logFragmentStackHierarchy(TAG);
                        break;
                }
                return true;
            }
        });
    }
    @Override
    protected void onDestroy2() {
        mRecy.setAdapter(null);
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onPageVisibleChanged(boolean visible, boolean isFirstTimeVisible, @Nullable Bundle savedInstanceState) {
        Log.i(getClass().getSimpleName(), "visible = " + visible + ", isFirstTime = " + isFirstTimeVisible + ", saveInstanceState = " + savedInstanceState);
        if(visible){
            if(isFirstTimeVisible){
                List<Chat> chatList = getDatas();
                mAdapter.setDatas(chatList);
                ToasterDebug.toastShort(getClass().getSimpleName() + " first 可见了");
            }else{
                ToasterDebug.toastShort(getClass().getSimpleName() + " 又 可见了");
            }
        }else {

        }
    }


    private List<Chat> getDatas() {
        List<Chat> msgList = new ArrayList<>();

        String[] name = new String[]{"小黄", "小李", "小张", "老王", "小马"};
        String[] chats = new String[]{"小黄的消息", "小李的消息", "小张的消息", "老王的消息", "小马的消息"};

        for (int i = 0; i < 15; i++) {
            int index = (int) (Math.random() * 5);
            Chat chat = new Chat();
            chat.name = name[index];
            chat.message = chats[index];
            msgList.add(chat);
        }
        return msgList;
    }

    public void onRefresh2() {
        mRefreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                mRefreshLayout.setRefreshing(false);
            }
        }, 2500);
    }

    /**
     * Reselected Tab
     */
    @Subscribe
    public void onTabSelectedEvent(TabSelectedEvent event) {
        if (event.position != MainFragment.FIRST) return;

        if (mInAtTop) {
            mRefreshLayout.setRefreshing(true);
            onRefresh2();
        } else {
            scrollToTop();
        }
    }

    private void scrollToTop() {
        mRecy.smoothScrollToPosition(0);
    }

}
