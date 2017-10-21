package org.ayo.component.sample.pager;

import android.os.Bundle;
import android.view.View;

import org.ayo.component.Master;
import org.ayo.sample.menu.DemoMenuFragment;

/**
 * Created by Administrator on 2017/1/5.
 */

public class DemoPagerActivity extends DemoMenuFragment {
    @Override
    public String getDemoName() {
        return "Pager页模板";
    }

    @Override
    public DemoInfo[] getDemoMenus() {
        return new DemoInfo[]{
                new DemoInfo("ViewPage页--内容在上，被indicator覆盖", new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putInt("layout", Master.LAYOUT_PAGER_PAGE_TOP_OVERLAY);  ///默认效果，可以不传参数
                        Master.startPage(getActivity(), DemoPagerFragment.class, bundle);
                    }
                }),
                new DemoInfo("ViewPage页--内容在上，不被indicator覆盖", new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putInt("layout", Master.LAYOUT_PAGER_PAGE_TOP_NONE_OVERLAY);
                        Master.startPage(getActivity(), DemoPagerFragment.class, bundle);
                    }
                }),
                new DemoInfo("ViewPage页--内容在下，被indicator覆盖", new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putInt("layout", Master.LAYOUT_PAGER_PAGE_BOTTOM_OVERLAY);
                        Master.startPage(getActivity(), DemoPagerFragment.class, bundle);
                    }
                }),
                new DemoInfo("ViewPage页--内容在下，不被indicator覆盖", new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putInt("layout", Master.LAYOUT_PAGER_PAGE_BOTTOM_NONE_OVERLAY);
                        Master.startPage(getActivity(), DemoPagerFragment.class, bundle);
                    }
                })
        };
    }
}
