package org.ayo.rx.sample;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.functions.Functions;
import io.reactivex.internal.operators.flowable.FlowableInternalHelper;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/2/14 0014.
 */

public class Rx_ofType extends BaseRxDemo {

    @Override
    protected String getTitle() {
        return "ofType";
    }

    private Disposable task;

    protected void runOk(){
        /*
        - empty
            - 直接调用complete
         */
        task = Flowable.just(1, "a", 2, "b")
                .ofType(String.class)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object s) throws Exception {
                        notifyy(s.toString());
                    }
                }, Functions.ERROR_CONSUMER,
                        new Action() {
                            @Override
                            public void run() throws Exception {
                                notifyy("onComplete---结束了！@@");
                            }
                        },
                        FlowableInternalHelper.RequestMax.INSTANCE);

    }

    protected void runError(){
    }

    @Override
    protected void onDestroy2() {
        super.onDestroy2();
        if(task != null) task.dispose();
    }

    public static boolean isPrime(Long a) {
        boolean flag = true;
        if (a < 2) {// 素数不小于2
            return false;
        } else {
            for (int i = 2; i <= Math.sqrt(a); i++) {
                if (a % i == 0) {// 若能被整除，则说明不是素数，返回false
                    flag = false;
                    break;// 跳出循环
                }
            }
        }
        return flag;
    }

}
