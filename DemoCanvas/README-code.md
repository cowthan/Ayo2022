# Demo代码结构

DemoBase是每个Demo Activity的基类，主要负责：

```java
1 创建Test View，基类是BaseView
@Override
protected BaseView createTestView() {
    //初始化notify条
    setNotify("");
    
    //是否显示左右TouchBoard
    enableLeftTouchBoard(false);
    enableRightTouchBoard(false);
    
    //设置Test View的背景，其实设置的父控件FrameLayout的背景，默认透明，但有时透明看效果不太好
    setTestViewBackgroundStroke(); //给个线框包围四周
    setTestViewBackgroundFill(int bgColor); //设置背景填充
    //这俩冲突
    
    //返回Test View
    return new ColorView(getActivity());
}

2 控制Test View的点击，滑动，左右TOuchBoard的点击，滑动


3 获取Test View
getTestView();

4 横条提示
setNotify(String s)

5 观察Test View的事件变化，配合setNotify

getTestView().getObservable().addObserver(new Observer(){
    
});



```