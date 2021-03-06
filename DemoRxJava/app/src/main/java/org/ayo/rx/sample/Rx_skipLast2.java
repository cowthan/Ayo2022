package org.ayo.rx.sample;

import java.util.concurrent.TimeUnit;

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

public class Rx_skipLast2 extends BaseRxDemo {

    @Override
    protected String getTitle() {
        return "take";
    }
    @Override
    protected String getImageName() {
        return "skipLast2";
    }

    private Disposable task;

    @Override
    protected String getCodeNormal() {
        return "Flowable.interval(1, 1, TimeUnit.SECONDS)\n" +
                "                .take(10)\n" +
                "                .skipLast(4, TimeUnit.SECONDS)\n" +
                "这个有点意思，会取前10条（也就是10秒），跳过最后4秒（4条）\n" +
                "然后一下发射出来";
    }

    protected void runOk(){
        /*
        - empty
            - 直接调用complete
         */
        task = Flowable.interval(1, 1, TimeUnit.SECONDS)
                .take(10)
                .skipLast(4, TimeUnit.SECONDS)
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
