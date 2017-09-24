package com.worse.more.breaker.ui.contact;

import com.worse.more.breaker.event.LoginOkEvent;

import org.ayo.component.mvp.AyoPresenter;
import org.ayo.component.mvp.AyoView;

/**
 * Created by Administrator on 2017/1/13 0013.
 */

public class LoginContact {
    public interface View extends AyoView{
        void onLoginOk(LoginOkEvent e);
    }

    public static abstract class Presenter extends AyoPresenter<View>{
        public abstract void doLogin();
    }


}
