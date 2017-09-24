package org.ayo.ui.sample.material;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import org.ayo.notify.toaster.Toaster;
import org.ayo.sample.R;
import org.ayo.ui.sample.base.AyoActivity;

/**
 */

public class DemoToolbarActivity extends AyoActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.ac_demo_m_toolbar;
    }

    @Override
    protected void onCreate2(View view, @Nullable Bundle bundle) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);


        //toolbar.setLogo(R.mipmap.ic_launcher);// App Logo
        toolbar.setTitle("PieceShit");// Title
        //toolbar.setSubtitle("a piece of shit");// Sub Title
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.mipmap.ic_launcher);
        toolbar.setOnMenuItemClickListener(onMenuItemClick);


    }

    @Override
    protected void onDestroy2() {

    }

    @Override
    protected void onPageVisibleChanged(boolean b, boolean b1, @Nullable Bundle bundle) {

    }


    private Toolbar.OnMenuItemClickListener onMenuItemClick = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            String msg = "";
            switch (menuItem.getItemId()) {
                case R.id.action_edit:
                    msg += "Click edit";
                    break;
                case R.id.action_share:
                    msg += "Click share";
                    break;
                case R.id.action_settings:
                    msg += "Click setting";
                    break;
            }

            if(!msg.equals("")) {
                Toaster.toastShort(msg); //.makeText(PinnedHeaderExpandableListViewDemo.this, msg, Toast.LENGTH_SHORT).show();
            }
            return true;
        }
    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
}
