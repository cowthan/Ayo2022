# 关于内存

1 一个手机版内存监控组件

https://github.com/Kyson/MemoryMonitor

通过这个呢，你可以在手机上实时查看内存使用量，包括heap，pss，和studio上的monitor差不多

2 更深的道理

http://mp.weixin.qq.com/s?__biz=MzI2OTQxMTM4OQ==&mid=2247484072&idx=1&sn=59f7ff77c686a0c9c2a348876513842e&chksm=eae1f7fadd967eece08a2da75dda1b1b6a428f227602540ff96d50cc9950562fd7dc766a37b9#rd
参考这篇文章，深入的了解一下内存方面的问题，更多的文章在：

https://github.com/hejunlin2013/DriodDeveloper


3 几种内存的说法和获取方法

OOM阈值：通过ActivityManager获取
heap：通过Runtime获取
pss：通过ActivityManager获取


Note: 当系统开始清除LRU缓存中的进程时，尽管它首先按照LRU的顺序来操作，但是它同样会考虑进程的内存使用量。因此消耗越少的进程则越容易被留下来。

4 Service和内存的关系

IntentService对内存优化有促进作用，应该多用，主要问题在于：Service应该用完就释放，不要贪图不死

5 Activtiy类里面的onTrimMemory()回调

所有UI都不可见时，会回调这个
和onStop还是不同的，onStop时，应该释放Activity的网络连接，广播接收者等资源，onTrimMemory时，应该释放UI资源

你应该使用这个方法来监听到TRIM_MEMORY_UI_HIDDEN级别, 它意味着你的UI已经隐藏，你应该释放那些仅仅被你的UI使用的资源

除非接收到onTrimMemory(TRIM_MEMORY_UI_HIDDEN))的回调，否者你不应该释放你的UI资源。这确保了用户从其他activity切回来时，你的UI资源仍然可用，并且可以迅速恢复activity。


6 bitmap

当你加载一个bitmap时，仅仅需要保留适配当前屏幕设备分辨率的数据即可，如果原图高于你的设备分辨率，需要做缩小的动作。请记住，增加bitmap的尺寸会对内存呈现出2次方的增加，因为X与Y都在增加。

7 webview释放


8 高效容器类

例如SparseArray, SparseBooleanArray, 与 LongSparseArray

9 常见消耗：

注意内存开销
对你所使用的语言与库的成本与开销有所了解，从开始到结束，在设计你的app时谨记这些信息。通常，表面上看起来不以为然的事情也许实际上会导致大量的开销。例如：

- Enums的内存消耗通常是static constants的2倍。你应该尽量避免在Android上使用enums。
- 在Java中的每一个类(包括匿名内部类)都会使用大概500 bytes。
- 每一个类的实例花销是12-16 bytes。
- 往HashMap添加一个entry需要额一个额外占用的32 bytes的entry对象。

10 用多进程增加可用内存

当然这个不能被滥用

开新进程的方式：
<service android:name=".PlaybackService"
    android:process=":background" />