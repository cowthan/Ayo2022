
package org.ayo.mall.view.xlistview;


import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Scroller;

import org.ayo.log.Trace;
import org.ayo.mall.R;

import java.lang.ref.WeakReference;

/**
 * 自定义ListView控件，实现下拉和更多处理
 * <HR>
 * 作者: 孙博
 * <p/>
 * 时间: 2013年11月7日 下午2:43:42
 */
public class XListView extends ListView implements OnScrollListener {

    public final static int STATE_REFRESHING = 2;

    public final static int STATE_NORMAL = 0;

    public final static int STATE_READY = 1;

    public final static int STATE_LOADING = 2;

    public final static int STATE_NOTDATA = 3;

    public final static int STATE_REFRESH = 4;


    /**
     * FooterView可以显示
     */
    public static final int FOOTER_SHOW = 0x01;

    /**
     * FooterView可以隐藏
     */
    public static final int FOOTER_HIDE = 0x02;

    /**
     * FooterView保持显示，但不可点击
     */
    public static final int FOOTER_RETAIN = 0x03;

    /**
     * FooterView保持显示，可点击
     */
    public static final int FOOTER_WAIT = 0x04;

    /**
     * FooterView保持显示，可点击
     */
    public static final int FOOTER_REFRESH = 0x05;

    private final static int SCROLLBACK_HEADER = 0;

    private final static int SCROLL_DURATION = 400; // scroll back duration

    // private final static int PULL_LOAD_MORE_DELTA = 50; // when pull kit_up >=
    // 50px
    // at bottom, trigger
    // load more.
    private final static float OFFSET_RADIO = 1.8f; // support iOS like pull

    private float mLastY = -1; // save event y

    private Scroller mScroller; // used for scroll back

    private OnScrollListener mScrollListener; // user's scroll listener

    // the interface to trigger refresh and load more.
    private OnXListViewListener mListViewListener;

    // -- header view
    private XListViewHeader mHeaderView;

    // header view content, use it to calculate the Header's height. And hide it
    // when disable pull refresh.
    private View mHeaderViewContent;

    private int mHeaderViewHeight; // header view's height

    private static class MyOnGlobalLayoutListener implements
            ViewTreeObserver.OnGlobalLayoutListener {
        private WeakReference<XListView> mRef;

        public MyOnGlobalLayoutListener(XListView view) {
            mRef = new WeakReference<>(view);
        }

        @Override
        public void onGlobalLayout() {
            if (mRef == null || mRef.get() == null)
                return;
            if (mRef.get().mHeaderView.getViewTreeObserver().isAlive())
                mRef.get().mHeaderView.getViewTreeObserver().removeGlobalOnLayoutListener(
                        mRef.get().mOnGlobalLayoutListener);
            mRef.get().mHeaderViewHeight = mRef.get().mHeaderViewContent.getHeight();
        }
    }

    private MyOnGlobalLayoutListener mOnGlobalLayoutListener = new MyOnGlobalLayoutListener(this);


    // total lists items, used to detect is at the bottom of listview.
    // private int mTotalItemCount;
    private boolean mEnablePullRefresh = true;
    private boolean mPullRefreshing = false; // is refreashing.
    // private final static int SCROLLBACK_FOOTER = 1;
    // -- footer view
    private XListViewFooter mFooterView;
    private RelativeLayout mFooterViewContent;
    // feature.
    private int mPullLoadState;
    private boolean mPullLoading;
    private boolean mIsFooterReady = false;
    // for mScroller, scroll back from header or footer.
    private int mScrollBack;

    /**
     * @param context
     */
    public XListView(Context context) {
        super(context);
        this.initWithContext(context);
    }

    public XListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initWithContext(context);
    }

    public XListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.initWithContext(context);
    }

    private void initWithContext(Context context) {
        mScroller = new Scroller(context, new DecelerateInterpolator());
        // XListView need the scroll event, and it will dispatch the event to
        // user's listener (as a proxy).
        super.setOnScrollListener(this);

        // init header view
        mHeaderView = new XListViewHeader(context);
        mHeaderViewContent = mHeaderView.findViewById(R.id.xlistview_header_content);
        this.addHeaderView(mHeaderView);

        // init footer view
        mFooterView = new XListViewFooter(context, true);
        mFooterViewContent = (RelativeLayout)mFooterView
                .findViewById(R.id.xlistview_footer_content);

        // init header height
        mHeaderView.getViewTreeObserver().addOnGlobalLayoutListener(mOnGlobalLayoutListener);
    }

    @Override
    public void setAdapter(ListAdapter adapter) {
        if (adapter != null)
            // make sure XListViewFooter is the last footer view, and only add
            // once.
            if (mIsFooterReady) {
                mIsFooterReady = false;
                this.addFooterView(mFooterView);
            }
        super.setAdapter(adapter);
    }

    public void setFooterReady(boolean mIsFooterReady) {
        this.mIsFooterReady = mIsFooterReady;
    }

    /**
     * 为列表设置适配数据集
     *
     * @param adapter The ListAdapter which is responsible for maintaining the
     *            data backing this lists and for producing a view to represent
     *            an item in that data set.
     * @param isAnimation 是否为动画模式显示
     * @see #setAdapter(ListAdapter)
     */
    public void setAdapter(BaseAdapter adapter, boolean isAnimation) {
        if (isAnimation) {
            // SwingBottomInAnimationAdapter swingBottomInAnimationAdapter = new
            // SwingBottomInAnimationAdapter(adapter);
            // swingBottomInAnimationAdapter.setListView(this);
            // this.setAdapter(swingBottomInAnimationAdapter);
            return;
        }
        this.setAdapter(adapter);
    }

    /**
     * enable or disable pull kit_down refresh feature.
     *
     * @param enable
     */
    public void setPullRefreshEnable(boolean enable) {
        mEnablePullRefresh = enable;
        if (!mEnablePullRefresh) {
            // disable, hide the content
            mHeaderViewContent.setVisibility(View.INVISIBLE);
        } else {
            mHeaderViewContent.setVisibility(View.VISIBLE);
        }
    }

    /**
     * enable or disable pull kit_up load more feature.
     */
    public void setPullLoadEnable(int enableState) {
        mPullLoadState = enableState;
        switch (mPullLoadState) {
            case FOOTER_SHOW:
                mFooterViewContent.setVisibility(View.VISIBLE);
                mPullLoading = false;
                mFooterView.setState(XListViewFooter.STATE_NORMAL);
                // both "pull kit_up" and "click" will invoke load more.
                mFooterView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startLoadMore();
                    }
                });
                break;
            case FOOTER_HIDE:
                mFooterViewContent.setVisibility(View.GONE);
                mFooterView.setOnClickListener(null);
                break;
            case FOOTER_RETAIN:
                mFooterViewContent.setVisibility(View.VISIBLE);
                mFooterView.setState(XListViewFooter.STATE_NOTDATA);
                mFooterView.setOnClickListener(null);
                break;
            case FOOTER_WAIT:
                mFooterViewContent.setVisibility(View.VISIBLE);
                mPullLoading = false;
                mFooterView.setState(XListViewFooter.STATE_WAIT);
                // both "pull kit_up" and "click" will invoke load more.
                mFooterView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startLoadMore();
                    }
                });
                break;
            case FOOTER_REFRESH:
                mFooterViewContent.setVisibility(View.VISIBLE);
                mPullLoading = false;
                mFooterView.setState(XListViewFooter.STATE_REFRESH);
                // both "pull kit_up" and "click" will invoke load more.
                mFooterView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startLoadMore();
                    }
                });
                break;
        }
    }

    public int getPullLoadState() {
        return mPullLoadState;
    }

    public View getHeaderView() {
        return mHeaderView;
    }

    public View getFooterView() {
        return mFooterView;
    }

    /**
     * stop refresh, reset header view.
     */
    public void stopRefresh() {
        Trace.e("stopRefresh", "mPullRefreshing:" + mPullRefreshing);
        mPullRefreshing = false;
        this.resetHeaderHeight();
    }

    /**
     * stop load more, reset footer view.
     */
    public void stopLoadMore() {
        if (mPullLoading) {
            mPullLoading = false;
            mFooterView.setState(STATE_NORMAL);
        }
    }

    /**
     * set last refresh time
     *
     * @param time
     */
    public void setRefreshTime(String time) {
    }

    private void invokeOnScrolling() {
        if (mScrollListener instanceof OnXScrollListener) {
            OnXScrollListener l = (OnXScrollListener)mScrollListener;
            l.onXScrolling(this);
        }
    }

    private void updateHeaderHeight(float delta) {
        mHeaderView.setVisiableHeight((int)delta + mHeaderView.getVisiableHeight());
        if (mEnablePullRefresh && !mPullRefreshing)
            // 未处于刷新状态，更新箭头
            if (mHeaderView.getVisiableHeight() > mHeaderViewHeight)
                mHeaderView.setState(STATE_READY);
            else
                mHeaderView.setState(STATE_NORMAL);
        this.setSelection(0); // scroll to top each time
    }

    /**
     * reset header view's height.
     */
    private void resetHeaderHeight() {
        int height = mHeaderView.getVisiableHeight();
        if (height == 0) // not visible.
            return;
        // refreshing and header isn't shown fully. do nothing.
        if (mPullRefreshing && height <= mHeaderViewHeight)
            return;
        int finalHeight = 0; // default: scroll back to dismiss header.
        // is refreshing, just scroll back to show all the header.
        if (mPullRefreshing && height > mHeaderViewHeight)
            finalHeight = mHeaderViewHeight;
        mScrollBack = SCROLLBACK_HEADER;
        mScroller.startScroll(0, height, 0, finalHeight - height, SCROLL_DURATION);
        // trigger computeScroll
        this.invalidate();
    }

    private void startLoadMore() {
        if (mListViewListener != null && !mPullLoading) {
            mFooterView.setState(STATE_LOADING);
            mListViewListener.onLoadMore();
            mPullLoading = true;
        }
    }

    public void setFooterLoading() {
        mPullLoading = true;
        mFooterViewContent.setVisibility(View.VISIBLE);
        mFooterView.setState(STATE_LOADING);
    }

    /**
     * 一开始刷新
     */
    public void refresh(OnXListViewListener mListViewListener) {
        // this.updateHeaderHeight(mHeaderViewContent.getLayoutParams().height);
        // this.invokeOnScrolling();
        mPullRefreshing = true;
        mHeaderView.setState(STATE_REFRESHING);
        if (mListViewListener != null)
            mListViewListener.onRefresh();

    }

    /**
     * 一开始刷新
     */
    public void startRefresh() {
        Trace.e("XListView", "startRefresh");
        this.updateHeaderHeight(mHeaderViewContent.getLayoutParams().height);
        this.invokeOnScrolling();
        mPullRefreshing = true;
        mHeaderView.setState(STATE_REFRESHING);

    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mLastY == -1)
            mLastY = ev.getRawY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastY = ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                final float deltaY = ev.getRawY() - mLastY;
                mLastY = ev.getRawY();
                if (this.getFirstVisiblePosition() == 0 && mEnablePullRefresh
                        && (mHeaderView.getVisiableHeight() > 0 || deltaY > 0)) {
                    // the first item is showing, header has shown or pull kit_down.
                    this.updateHeaderHeight(deltaY / OFFSET_RADIO);
                    this.invokeOnScrolling();
                }
                break;
            default:
                mLastY = -1; // reset
                if (this.getFirstVisiblePosition() == 0) {
                    // invoke refresh
                    if (mEnablePullRefresh && mHeaderView.getVisiableHeight() > mHeaderViewHeight) {
                        mPullRefreshing = true;
                        mHeaderView.setState(STATE_REFRESHING);
                        if (mListViewListener != null)
                            mListViewListener.onRefresh();
                    }
                    this.resetHeaderHeight();
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            if (mScrollBack == SCROLLBACK_HEADER) {
                mHeaderView.setVisiableHeight(mScroller.getCurrY());
            } else {
                mFooterView.setBottomMargin(mScroller.getCurrY());
            }
            this.postInvalidate();
            this.invokeOnScrolling();
        }
        super.computeScroll();
    }

    @Override
    public void setOnScrollListener(OnScrollListener l) {
        mScrollListener = l;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        // if (App.ISAUTOLOADERMORE)
        // {
        switch (scrollState) {
            case OnScrollListener.SCROLL_STATE_IDLE:
                if ((view.getLastVisiblePosition() == (view.getCount() - 1))
                        && (mPullLoadState == FOOTER_SHOW || mPullLoadState == FOOTER_WAIT || mPullLoadState == FOOTER_REFRESH))
                    this.startLoadMore();
                break;
        }
        // }
        if (mScrollListener != null)
            mScrollListener.onScrollStateChanged(view, scrollState);
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                         int totalItemCount) {
        // send to user's listener
        // mTotalItemCount = totalItemCount;
        if (mScrollListener != null)
            mScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
    }

    public void setXListViewListener(OnXListViewListener l) {
        mListViewListener = l;
    }

    /**
     * you can listen ListView.OnScrollListener or this one. it will invoke
     * onXScrolling when header/footer scroll back.
     */
    public interface OnXScrollListener extends OnScrollListener {
        void onXScrolling(View view);
    }

    /**
     * implements this interface to get refresh/load more event.
     */
    public interface OnXListViewListener {
        void onRefresh();

        void onLoadMore();
    }

//    public void setHintText(String text)
//    {
//        if(! TextUtils.isEmpty (text)&&mFooterView!=null)
//        {
//            mFooterView.setHintText(text);
//        }
//    }

}
