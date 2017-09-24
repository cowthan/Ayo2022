摸索过程中的项目标配
---------------------

1 ayo几个module，每个module内都有doc.md文档

2 ButterKnife

ButterKnife 8需要sdk 23，我们的项目基于22，所以用7

文档地址：
https://github.com/JakeWharton/butterknife/
http://jakewharton.github.io/butterknife/


3 常用动画

还没配上

4 App全局类


5 Config全局类


6 沉浸式标题栏

SystemBarTintManager

文档：https://github.com/jgilfelt/SystemBarTint

--------内容占不占用status bar和navigate bar

取决于layout根布局的这俩属性：
android:fitsSystemWindows="true"   true则content view会给stutus bar和navigate bar留出padding
android:clipToPadding="false"

--------设置颜色

SystemBarTintManager tintManager = new SystemBarTintManager(getActivity());
tintManager.setStatusBarTintEnabled(true);
tintManager.setNavigationBarTintEnabled(true);
tintManager.setStatusBarTintColor(Color.parseColor("#55ff0000"));
tintManager.setNavigationBarTintColor(Color.parseColor("#55ff0000"));


具体看有道云笔记里截图

