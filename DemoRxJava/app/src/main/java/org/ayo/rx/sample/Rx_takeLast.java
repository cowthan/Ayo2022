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

public class Rx_takeLast extends BaseRxDemo {

    @Override
    protected String getTitle() {
        return "take";
    }
    @Override
    protected String getImageName() {
        return "takeLast";
    }

    private Disposable task;

    @Override
    protected String getCodeNormal() {
        return "Flowable.just(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L)\n" +
                "        .takeLast(5)";
    }

    protected void runOk(){
        /*
        - empty
            - 直接调用complete
         */
        task = Flowable.just(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L)
                .takeLast(5)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long s) throws Exception {
                        notifyy(s + "");
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


}
