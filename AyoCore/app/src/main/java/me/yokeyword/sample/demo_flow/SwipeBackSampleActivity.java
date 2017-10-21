package me.yokeyword.sample.demo_flow;

import android.os.Bundle;

import org.ayo.component.sample.R;
import org.ayo.fragmentation.SwipeBackActivity;
import org.ayo.fragmentation.SwipeBackLayout;
import org.ayo.fragmentation.anim.DefaultHorizontalAnimator;
import org.ayo.fragmentation.anim.FragmentAnimator;

import me.yokeyword.sample.demo_flow.ui.fragment_swipe_back.FirstSwipeBackFragment;

/**
 * Created by YoKeyword on 16/4/19.
 */
public class SwipeBackSampleActivity extends SwipeBackActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_back);

        if (savedInstanceState == null) {
            loadRootFragment(R.id.fl_container, FirstSwipeBackFragment.newInstance());
        }

        getSwipeBackLayout().setEdgeOrientation(SwipeBackLayout.EDGE_ALL);
    }

    /**
     * 限制SwipeBack的条件,默认栈内Fragment数 <= 1时 , 优先滑动退出Activity , 而不是Fragment
     *
     * @return true: Activity可以滑动退出, 并且总是优先;  false: Activity不允许滑动退出
     */
    @Override
    public boolean swipeBackPriority() {
        return super.swipeBackPriority();
    }

    public FragmentAnimator onCreateFragmentAnimator() {
        return new DefaultHorizontalAnimator();
    }
}
