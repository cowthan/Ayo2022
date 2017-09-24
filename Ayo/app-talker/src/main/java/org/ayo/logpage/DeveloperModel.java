package org.ayo.logpage;

import org.ayo.notify.spinner.IOptionModel;

/**
 * Created by Administrator on 2017/7/9.
 */

public class DeveloperModel implements IOptionModel{

    public String name;
    public String email;

    @Override
    public String getValue() {
        return name;
    }

    @Override
    public String getKey() {
        return email;
    }

    @Override
    public String toString() {
        return getValue();
    }
}
