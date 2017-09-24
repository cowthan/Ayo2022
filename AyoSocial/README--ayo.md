# Ayo：狂霸酷拽强，自顾自美丽
项目的基本框架，所有项目都fork这个，便于库的统一更新，然后拉不同的remote
---------------------------------------------------------------------


* git，下面指定的模块都要跑一下这三个命令，并且对应目录得放到gitignore里
    * 开新项目时，fork一下github上的Ayo
    * add一个remote upstream，来拉取库更新，提交库修改
    * add一个remote downstream，负责项目的提交和更新
    * git init
    * git remote add ...
    * git push origin master

* 项目引入：Ayo整体库配置
    * ayo-core：核心库
        * 这里放的是框架级的, ayocore里换东西，应该会影响到项目框架了
        * 区别于sdk，放的应该是常用工具类，如转换，日期，文件，什么的，sdk不会影响项目架构
        * 包括：
            * Activity基类，Activity统计
            * ActivityAttacher辅助框架，免manifest
            * Fragment框架，基于Fragmentation
            * MVP基础
            * EventBus
            * RxBus
            * RxJava轻引入
            * 权限
            * 崩溃日志
            * 调试日志
            * 初始化框架
            * 状态栏一体化
            * ButterKnife和Dagger2引入
            * DB依赖：AyoDB，取自XUtils
            * ACache
    * ayo-lang：基础库
        * 常用工具类，提供工具和语法上的便利，仅此而已
    * ayo-http：基础库
        * 需要指定worker和converter
    * 图片四件套
        * 选择：图库--MediaChooser
        * 选择：拍照--SquareCamera
        * 显示：VanGogh，兼容多套
        * 七牛上传
    * UI五件套：
        * animation：基于daimajia（androidanimations和easing）
        * view
        * list
        * template
        * notify

* 其他模块
    * imageloader
    * progress
    * imagepicker/crop/camera/(mediachooser/wechatchooser)
    * 七牛
    * share
    * umeng
    * video
    * IM系列

* 习惯套路
    * Prompt类，管理所有弹出框，BreakingBad里有提供统一Action接口
    * Dialog通用类：提供弹出方式，动画设置，自定义View等接口，每个项目定制一组主题弹框
    * 现在需要提供顶层框架的有：http，imageloader，EventBus，便于随时切换底层库
    * 提供一个UI类，管理界面的相关事务，例如标题栏，状态栏等的统一管理

------------------------------------------------------------
# 目录

## [资料汇总](./doc/README-opensource.md)

## 基础设施

### 1 [AyoCore（核心库）](./doc/README-ayo-core.md)
### 2 Activity和Fragment基本框架
    * [Activity框架](./doc/README-attacher.md)
    * [Framentation](https://github.com/YoKeyword/Fragmentation)   http://www.jianshu.com/p/38f7994faa6b
    * 重点还是生命周期，何时创建，何时销毁，缓存处理
### 3 [MVP框架](./doc/README-mvp.md)
### 4 [应用初始化](./doc/README-init.md)
### 5 [Crash上报和恢复](./doc/README-recovery.md)
    * 崩溃捕捉和上传
    * 崩溃恢复
### 6 [Permission相关](./doc/README-ask.md)
### 7 [缓存ACache](./doc/aa)
### 8 [Log相关](./doc/README-log.md)
### 9 [SystemBar相关](./doc/README-systembar.md)
### 10 [事件总线Eventbus-3.0](./doc/README-eventbus30.md)  ---- 替代品：RxBus
### 11 [数据库](./doc/README-db.md)   ---- 替代品：Realm（不会拼）
### 12 [ButterKnife](./doc/doc-inject.md)
### 13 [工具库：便利工具](./doc/README-lang.md)
### 14 Http
    * [http](./doc/doc-http-ayo.md)   ----学习参考：OkGo和Retrofit
    * [volly源码解析](./doc/doc-http-volly.md)
### 15 [image-loader：新版还没出来，得参考这个https://github.com/FinalTeam/RxGalleryFinal]()
    * [UIL源码分析](./doc/doc-img-uil.md)
### 16 image
    * [UIL](./doc/README-http.md)
    * [Glide]()
    * [Fresco]()
    * [image picker]
    * [image crop]
    * [camera]
    * [image处理]
### 18 [反射] (https://github.com/cowthan/jOOR)
### 19 [xml](./doc/doc-xml.md)
### 异步
    * [Async](./doc/doc-async.md)
    * [安卓消息机制：源码解析](./doc/doc-looper.md)
### x [dagger]
### y [RxJava]
### ClassLoader：参考JavaAyo   https://github.com/cowthan/JavaAyo
### 集合：参考JavaAyo   https://github.com/cowthan/JavaAyo
### IO, NIO：参考JavaAyo   https://github.com/cowthan/JavaAyo
### 对象管理：参考JavaAyo   https://github.com/cowthan/JavaAyo
### 设计模式：参考JavaAyo   https://github.com/cowthan/JavaAyo
### Java并发基础：参考JavaAyo   https://github.com/cowthan/JavaAyo
### 其他Java技术点


## 几个小花招

### Fragment和Activity状态记录
### IntentBuilder： https://github.com/emilsjolander/IntentBuilder


## UI库

### 1 Dialog
    * [Ayo库精选Dialog](./doc/doc-ui-dialog.md)
    * [原生Dialog](./doc/doc-ui-dialog-1.md)
### 2 [LoadingView](./doc/doc-ui-loading.md)
### 3 [StatusUIManager：页面状态UI切换](./doc/doc-ui-status.md)
### 4 [CordinatorLayout和Toolbar一家]
### 5 [动画]


## UI学习
### [Drawable](./doc/v-drawable.md)
### [TextView](./doc/v-textview.md)
### [measure, layout和draw]
### [自定义控件系列牛逼教程：重点说的draw]
### [滑动]
### [动画]

### 其他

### README目录下是markdown的语法
### [快速开发插件]
### [arr打包和引用]
### [编译加速：freeline]
