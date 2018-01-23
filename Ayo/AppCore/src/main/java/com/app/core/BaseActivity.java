package com.app.core;

import org.ayo.component.MasterActivity;

/**
 * Created by qiaoliang on 2018/1/4.
 */

public abstract class BaseActivity extends MasterActivity {
    protected <T> T fid(int id){
        return (T) findViewById(id);
    }
}
