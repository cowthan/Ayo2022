package com.worse.more.breaker.ui.contact;


import org.ayo.component.mvp.AyoPresenter;
import org.ayo.component.mvp.AyoView;

/**
 * Created by Administrator on 2017/1/13 0013.
 */

public class ForgetPasswordContact {

    public interface View extends AyoView{
        void onVeryCodeClickedOk(String code);
        void onSubmitOk();
    }

    public static abstract class Presenter extends AyoPresenter<View> {
        public abstract void doGetCode();
        public abstract void doSubmit();
    }

}
