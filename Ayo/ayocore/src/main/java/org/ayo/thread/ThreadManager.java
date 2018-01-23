package org.ayo.thread;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.SparseArray;
import android.view.View;
import android.widget.PopupWindow;

import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by qiaoliang on 2017/5/2.
 */

public class ThreadManager {

    //------------------------------------------------------------
    //  单例
    //------------------------------------------------------------
    private final static class Holder{
        private static final ThreadManager instance = new ThreadManager();
    }

    private ThreadManager(){

    }

    public static ThreadManager getDefault(){
        return Holder.instance;
    }

    //------------------------------------------------------------
    //  任务id生成
    //------------------------------------------------------------
    private static AtomicInteger taskId = new AtomicInteger(0);

    public static int createTaskId(){
        return taskId.incrementAndGet();
    }

    //------------------------------------------------------------
    //  主线程post系列
    //------------------------------------------------------------
    private static SparseArray<Handler> handlerMap = new SparseArray<>();

    /** 这不和组件绑定，无法自动取消，只能自己通过返回的disposal取消 */
    public Disposable runOnUiThread(final Task task, long delayMillis){

        Handler h = new Handler(Looper.getMainLooper());
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                task.run();
            }
        }, delayMillis);
        Disposable d = DisposableHandler.from(h);
        return d;
    }

    public void runOnUiThread(Object lifeCircleComopent, final Task task){
        if(lifeCircleComopent == null){
            throw new RuntimeException("异步任务必须和一个有生命周期的组件绑定");
        }
        final WeakReference<Object> containerRef = new WeakReference<>(lifeCircleComopent);
        Handler h = handlerMap.get(lifeCircleComopent.hashCode());
        if(h == null){
            h = new Handler(Looper.getMainLooper());
            handlerMap.put(lifeCircleComopent.hashCode(), h);
        }

        h.post(new Runnable() {
            @Override
            public void run() {
                if(isComponentGood(containerRef)) task.run();  //这里会持有组件实例的引用，所以可能造成内存泄漏，所以必须在onDestroy类里，handler.removeXXX(null)
            }
        });
    }

    public void runOnUiThread(Object lifeCircleComponent, final Task task, final long delayMillis){
        if(lifeCircleComponent == null){
            throw new RuntimeException("异步任务必须和一个有生命周期的组件绑定");
        }
        final WeakReference<Object> containerRef = new WeakReference<>(lifeCircleComponent);

        Handler h = handlerMap.get(lifeCircleComponent.hashCode());
        if(h == null){
            h = new Handler(Looper.getMainLooper());
            handlerMap.put(lifeCircleComponent.hashCode(), h);
        }

        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(isComponentGood(containerRef)) task.run();
            }
        }, delayMillis);
    }

    //------------------------------------------------------------
    //  在子线程运行任务
    //------------------------------------------------------------
    ExecutorService exec;
    private SparseArray<Set<DisposableAsyncTask>> runningTasks = new SparseArray<>();

    public DisposableAsyncTask runOnAsyncThread(Object lifeCircleComponent, final Task task){
        if(lifeCircleComponent == null){
            throw new RuntimeException("必须在一个带生命周期的组件里运行异步任务");
        }
        final WeakReference<Object> containerRef = new WeakReference<>(lifeCircleComponent);

        if(exec == null){
            exec = (ExecutorService) InternalAsyncTask.DUAL_THREAD_EXECUTOR; //Executors.newFixedThreadPool();
        }

        InternalAsyncTask<Void, Void, Object> taskWrapper = new InternalAsyncTask<Void, Void, Object>() {
            @Override
            protected Object doInBackground(Void... params) {
                if(!isComponentGood(containerRef)){
                    return null;
                }else{
                    return task.run();
                }
            }

            @Override
            protected void onPostExecute(Object o) {
                if(isComponentGood(containerRef) && task.getCallback() != null) task.onFinished(task.getCallback(), o);
            }

            @Override
            protected void onCancelled() {
                if(isComponentGood(containerRef) && task.getCallback() != null) task.onCanceled(task.getCallback());
            }
        };

        DisposableAsyncTask disposable = DisposableAsyncTask.from(taskWrapper);

        Set<DisposableAsyncTask> tasks = runningTasks.get(lifeCircleComponent.hashCode());
        if(tasks == null) tasks = new HashSet<>();
        runningTasks.put(lifeCircleComponent.hashCode(), tasks);
        tasks.add(disposable);
        taskWrapper.executeOnExecutor(exec);
        return disposable;
    }



    //------------------------------------------------------------
    //  取消所有可能的任务
    //------------------------------------------------------------
    public void cancelAllTask(Object lifeCircleComponent){
        Handler h = handlerMap.get(lifeCircleComponent.hashCode());
        if(h != null){
            h.removeCallbacksAndMessages(null);
            handlerMap.remove(lifeCircleComponent.hashCode());
        }

//        if(exec != null) {
//            exec.shutdownNow();
//            exec = null;
//        }

        Set<DisposableAsyncTask> tasks = runningTasks.get(lifeCircleComponent.hashCode());
        if(tasks != null) {
            for(DisposableAsyncTask disposable: tasks){
                if(disposable != null) disposable.dispose();
            }
            runningTasks.remove(lifeCircleComponent.hashCode());
        }
    }

    //------------------------------------------------------------
    //  HandlerThread：此处任务不可取消
    //------------------------------------------------------------


    //------------------------------------------------------------
    //  IntentService支持，实际上也是HandlerThread，而且优化更合理
    //  唯一的问题就是：给IntentService传入的任务如果又开子线程，IntentService会直接stopSelf，子线程又脱管了
    //------------------------------------------------------------
//    private final SparseArray<Task> serviceTasks = new SparseArray<>();
//    private final SparseArray<TaskCallback> serviceCallbacks = new SparseArray<>();
//    public static final String ACTION_POST_TASK = "com.ai.hai.hai.task";
//    public static final String EXTRA_TASK_ID = "com.ai.hai.hai.extra.taskid";
//
//    public SparseArray<Task> getServiceTasks(){
//        return serviceTasks;
//    }
//
//    public SparseArray<TaskCallback> getServiceCallbacks(){
//        return serviceCallbacks;
//    }
//
//    public interface TaskProvider{
//        Task createTask();
//    }
//
//    public interface TaskCallback{
//        void onFinish(Object result);
//    }
//
//    public int runOnIntentService(Context context, final TaskProvider taskProvider){
//        final Task task = taskProvider.createTask();
//        TaskCallback callback = new TaskCallback() {
//            @Override
//            public void onFinish(Object result) {
//                task.onFinished(null, result);
//            }
//        };
//        int newTaskId = ThreadManager.createTaskId();
//        serviceTasks.put(newTaskId, task);
//        serviceCallbacks.put(newTaskId, callback);
//
//        Intent intent = new Intent(context, FuckInternalService.class);
//        intent.setAction(ACTION_POST_TASK);
//        intent.putExtra(EXTRA_TASK_ID, taskId);
//        context.startService(intent);
//
//        return newTaskId;
//    }
//
//
//    public void removeServiceTask(int taskId){
//        serviceTasks.remove(taskId);
//        serviceCallbacks.remove(taskId);
//    }



    //------------------------------------------------------------
    //  工具
    //------------------------------------------------------------
    public static boolean isComponentGood(WeakReference<?> obj){
        if(obj == null) return false;
        if(obj.get() == null) return false;
        if(obj.get() instanceof Activity) {
            return true;
        }else if(obj.get() instanceof Fragment){
            return ((Fragment)obj.get()).getActivity() != null && !((Fragment)obj.get()).isDetached();
        }else if(obj.get() instanceof View){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                return ((View)obj.get()).isAttachedToWindow();
            }else{
                return true;
            }
        }else if(obj.get() instanceof Dialog){
            return ((Dialog)obj.get()).isShowing();
        }else if(obj.get() instanceof PopupWindow){
            return ((PopupWindow)obj.get()).isShowing();
        }else{
            return true;
        }
    }

}
