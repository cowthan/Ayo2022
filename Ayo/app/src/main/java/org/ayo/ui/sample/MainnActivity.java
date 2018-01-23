package org.ayo.ui.sample;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.app.core.prompt.Toaster;
import com.app.core.utils.permission.DialogHelper;
import com.app.core.utils.permission.PermissionConstants;
import com.app.core.utils.permission.PermissionUtils;
import com.ayo.sdk.demo.DemoJobActivity;
import com.ayo.sdk.sample.DemoThreadManager;
import com.worse.more.breaker.LoginActivity;
import com.zebdar.tom.chat.ChatActivity;

import net.yrom.screenrecorder.MainRecorderActivity;

import org.ayo.JsonUtils;
import org.ayo.editor.CreateThreadActivity;
import org.ayo.mall.activity.HellMainActivity;
import org.ayo.sample.R;
import org.ayo.sample.menu.Leaf;
import org.ayo.sample.menu.MainPagerActivity;
import org.ayo.sample.menu.Menu;
import org.ayo.sample.menu.MenuItem;
import org.ayo.ui.sample.avloding.AVLoadingSampleActivity;
import org.ayo.ui.sample.db.DemoDbActivity;
import org.ayo.ui.sample.dialog.ui.SimpleHomeActivity;
import org.ayo.ui.sample.flow.MyActivity;
import org.ayo.ui.sample.log.DemoLogReporterActivity;
import org.ayo.ui.sample.master.DemoAccount;
import org.ayo.ui.sample.master.DemoPage;
import org.ayo.ui.sample.material.DemoCoordinator_1_Activity;
import org.ayo.ui.sample.material.DemoCoordinator_2_Activity;
import org.ayo.ui.sample.material.DemoCoordinator_3_Activity;
import org.ayo.ui.sample.material.DemoCoordinator_4_Activity;
import org.ayo.ui.sample.material.DemoCoordinator_5_Activity;
import org.ayo.ui.sample.material.DemoCoordinator_6_Activity;
import org.ayo.ui.sample.material.DemoCoordinator_7_Activity;
import org.ayo.ui.sample.material.DemoTabLayoutActivity;
import org.ayo.ui.sample.material.DemoToolbarActivity;
import org.ayo.ui.sample.material.ElevationDemoActivity;
import org.ayo.ui.sample.material.PaletteDemoActivity;
import org.ayo.ui.sample.nano.NanoHttpServerDemo;
import org.ayo.ui.sample.notify.DemoActionSheetActivity;
import org.ayo.ui.sample.notify.DemoLoadingDialogActivity;
import org.ayo.ui.sample.notify.DemoNiftyActivity;
import org.ayo.ui.sample.notify.DemoPopupWindowActivity;
import org.ayo.ui.sample.notify.DemoSweetAlertActivity;
import org.ayo.ui.sample.notify.DemoToastActivity;
import org.ayo.ui.sample.picker.DemoPickerActivity;
import org.ayo.ui.sample.recycler.RecyclerViewDemo;
import org.ayo.ui.sample.recycler.RecyclerViewDemo2;
import org.ayo.ui.sample.recycler.RecyclerViewDemo_XRecyclerView;
import org.ayo.ui.sample.recycler.RecyclerViewDemo_header;
import org.ayo.ui.sample.recycler.pinnedsection.PinnedMainActivity;
import org.ayo.ui.sample.scrollview.DemoScrollView;
import org.ayo.ui.sample.scrollview.DemoScrollView2;
import org.ayo.ui.sample.scrollview.DemoScrollView3;
import org.ayo.ui.sample.social.DemoSocialShareActivity;
import org.ayo.ui.sample.spinner.DemoSpinnerPage;
import org.ayo.ui.sample.statusbar.DemoStatusBarActivity;
import org.ayo.ui.sample.template.recycler.DemoRecyclerViewActivity;
import org.ayo.ui.sample.textview.badge.BadgeViewActivity;
import org.ayo.ui.sample.textview.badge.TabActivity;
import org.ayo.ui.sample.textview.html.DemoAyoHtmlActivity;
import org.ayo.ui.sample.textview.html.DemoOldHtmlActivity;
import org.ayo.ui.sample.textview.span.AwesomeTextViewActivity;
import org.ayo.ui.sample.update.UpdateTestActivity;
import org.ayo.ui.sample.verticalbanner.VerticalBannerActivity;
import org.ayo.ui.sample.video.DemoVideoComporessActivity;
import org.ayo.ui.sample.view_learn.BlinkTextViewDemo;
import org.ayo.ui.sample.view_learn.BlockLayoutDemo;
import org.ayo.ui.sample.view_learn.CircleLoadingViewDemo;
import org.ayo.ui.sample.view_learn.CircleViewDemo;
import org.ayo.ui.sample.view_learn.CircleViewDemo2;
import org.ayo.ui.sample.view_learn.FakeHorizontalScrollViewDemo;
import org.ayo.ui.sample.view_learn.FakeScrollViewDemo;
import org.ayo.ui.sample.view_learn.MusicWaveViewDemo;
import org.ayo.ui.sample.view_learn.RichBackgroundTextViewDemo;
import org.ayo.ui.sample.view_learn.ViewDragHelperDemoView2Demo;
import org.ayo.ui.sample.view_learn.ViewDragHelperDemoViewDemo;
import org.ayo.ui.sample.view_learn.sticky.PinnedHeaderExpandableListViewDemo;
import org.ayo.ui.sample.webview.DemoWebView;
import org.ayo.ui.sample.wheel.WheelPickerctivity;
import org.ayo.update.IUpdateParser;
import org.ayo.update.UpdateInfo;
import org.ayo.update.UpdateManager;

import java.util.ArrayList;
import java.util.List;


public class MainnActivity extends MainPagerActivity {


    @Override
    public List<Menu> getMenus() {
        List<Menu> menus = new ArrayList<>();

        ///--------------------------菜单1：View
        Menu m1 = new Menu("View", R.drawable.weixin_normal, R.drawable.weixin_pressed);
        menus.add(m1);
        {
            MenuItem menuItem1 = new MenuItem("基本", R.drawable.weixin_normal, R.drawable.weixin_pressed);
            m1.addMenuItem(menuItem1);
            {
                menuItem1.addLeaf(new Leaf("智能机器人", "", ChatActivity.class, 1));
                menuItem1.addLeaf(new Leaf("登录模板", "", LoginActivity.class, 1));
                menuItem1.addLeaf(new Leaf("商城模板", "", HellMainActivity.class, 1));
                menuItem1.addLeaf(new Leaf("录屏", "", MainRecorderActivity.class, 1));
                menuItem1.addLeaf(new Leaf("----TextView系列----", "", null));
                menuItem1.addLeaf(new Leaf("html标签处理：原生，生成span", "", DemoOldHtmlActivity.class, 1));
                menuItem1.addLeaf(new Leaf("awesome：span高级使用", "", AwesomeTextViewActivity.class, 1));
                menuItem1.addLeaf(new Leaf("html标签处理：AppCore，可以取出属性", "", DemoAyoHtmlActivity.class, 1));
                menuItem1.addLeaf(new Leaf("BadgeView：小红点", "", BadgeViewActivity.class, 1));
                menuItem1.addLeaf(new Leaf("BadgeView：小红点", "", TabActivity.class));

                menuItem1.addLeaf(new Leaf("----ImageView系列----", "", null));
                menuItem1.addLeaf(new Leaf("CircleImageView", "", null));
                menuItem1.addLeaf(new Leaf("GifDrawable", "", null));

                menuItem1.addLeaf(new Leaf("----ProgressBar系列----", "", null));
                menuItem1.addLeaf(new Leaf("原生", "", null));
                menuItem1.addLeaf(new Leaf("AVLoadingView", "", AVLoadingSampleActivity.class));

                menuItem1.addLeaf(new Leaf("----Flipper系列----", "", null));
                menuItem1.addLeaf(new Leaf("ViewFliper", "", null));
                menuItem1.addLeaf(new Leaf("PageFliper", "", null));
                menuItem1.addLeaf(new Leaf("ViewPager", "", null));

                menuItem1.addLeaf(new Leaf("----其他----", "", null));
                menuItem1.addLeaf(new Leaf("Nice Spinner", "", DemoSpinnerPage.class));
            }

            menuItem1 = new MenuItem("高级", R.drawable.weixin_normal, R.drawable.weixin_pressed);
            m1.addMenuItem(menuItem1);
            {
                menuItem1.addLeaf(new Leaf("----scroll研究----", "", null));
                menuItem1.addLeaf(new Leaf("onTouch和MotionEvent", "", DemoSpinnerPage.class));
            }

        }

        ///--------------------------菜单1：开源
        Menu m3 = new Menu("layout", R.drawable.find_normal, R.drawable.find_pressed);
        menus.add(m3);
        {
            MenuItem menuItem = new MenuItem("layout", R.drawable.weixin_normal, R.drawable.weixin_pressed);
            m3.addMenuItem(menuItem);
            {
                menuItem.addLeaf(new Leaf("AutoLayout", "", null));
                menuItem.addLeaf(new Leaf("Persentage", "", null));
                menuItem.addLeaf(new Leaf("Constain", "", null));
                menuItem.addLeaf(new Leaf("FlowLayout", "", MyActivity.class, 1));
                menuItem.addLeaf(new Leaf("SwipeLayout", "", null));
                menuItem.addLeaf(new Leaf("SwipeBackLayout", "", null));
                menuItem.addLeaf(new Leaf("BlockLayout", "", null));
                menuItem.addLeaf(new Leaf("NineGridLayout", "", null));
            }
            menuItem = new MenuItem("RecyclerViewDemo", R.drawable.weixin_normal, R.drawable.weixin_pressed);
            m3.addMenuItem(menuItem);
            {
                menuItem.addLeaf(new Leaf("RecyclerView全配置系列", "", RecyclerViewDemo.class));
                menuItem.addLeaf(new Leaf("RecyclerView-ItemAnimator--notify一家", "", RecyclerViewDemo2.class));
                menuItem.addLeaf(new Leaf("RecyclerView-header实现-不稳定", "", RecyclerViewDemo_header.class));
                menuItem.addLeaf(new Leaf("XRecyclerView-header支持-稳定", "", RecyclerViewDemo_XRecyclerView.class));
                menuItem.addLeaf(new Leaf("RecyclerView-scrollToPostion", "", null));
                menuItem.addLeaf(new Leaf("RecyclerView-scroller-弹性滑动", "", null));
                menuItem.addLeaf(new Leaf("RecyclerView-ItemView重用原理", "", null));
                menuItem.addLeaf(new Leaf("Pinned Section RecyclerView", "", PinnedMainActivity.class, 1));
            }

            MenuItem menuItem4 = new MenuItem("design", R.drawable.weixin_normal, R.drawable.weixin_pressed);
            m3.addMenuItem(menuItem4);
            {
                menuItem4.addLeaf(new Leaf("Metetial：Toolbar", "", DemoToolbarActivity.class, 1));
                menuItem4.addLeaf(new Leaf("Metetial：CoordinatorLayout + AppBarLayout", "", DemoCoordinator_1_Activity.class, 1));
                menuItem4.addLeaf(new Leaf("Metetial：CoordinatorLayout", "", DemoCoordinator_2_Activity.class, 1));
                menuItem4.addLeaf(new Leaf("Metetial：CoordinatorLayout + FloatActionButton", "", DemoCoordinator_3_Activity.class, 1));
                menuItem4.addLeaf(new Leaf("Metetial：CollapsingToolbarLayout", "", DemoCoordinator_4_Activity.class, 1));
                menuItem4.addLeaf(new Leaf("Metetial：自定义Behavior：FloatingActionButton", "", DemoCoordinator_5_Activity.class, 1));
                menuItem4.addLeaf(new Leaf("Metetial：自定义Behavior：FloatingActionButton", "", DemoCoordinator_6_Activity.class, 1));
                menuItem4.addLeaf(new Leaf("Metetial：自定义Behavior：自定义View", "", DemoCoordinator_7_Activity.class, 1));
                menuItem4.addLeaf(new Leaf("Metetial：TabLayout", "", DemoTabLayoutActivity.class, 1));
                menuItem4.addLeaf(new Leaf("titlebar", "", null));
                menuItem4.addLeaf(new Leaf("MenuItem", "", null));
                menuItem4.addLeaf(new Leaf("Ripple", "", null));
                menuItem4.addLeaf(new Leaf("IconTextGroupView", "", null));
                menuItem4.addLeaf(new Leaf("WheelPicker", "", WheelPickerctivity.class, 1));
                menuItem4.addLeaf(new Leaf("---Pallete---", "", null, 1));
                menuItem4.addLeaf(new Leaf("从Bitmap提取颜色，并渲染状态栏", "", PaletteDemoActivity.class, 1));
                menuItem4.addLeaf(new Leaf("---视图和阴影---", "", null, 1));
                menuItem4.addLeaf(new Leaf("elevation支持：阴影", "", ElevationDemoActivity.class, 1));
            }
        }

        ///--------------------------菜单1：开源
        m3 = new Menu("成品", R.drawable.find_normal, R.drawable.find_pressed);
        menus.add(m3);
        {
            MenuItem  menuItem = new MenuItem("控件", R.drawable.weixin_normal, R.drawable.weixin_pressed);
            m3.addMenuItem(menuItem);
            {

                menuItem.addLeaf(new Leaf("VerticalBanner", "", VerticalBannerActivity.class, 1));
                //menuItem.addLeaf(new Leaf("Google的Hover--可当副UI框架", "", HoverMainActivity.class, 1));
                menuItem.addLeaf(new Leaf("Measure实例：BlockLayout", "", BlockLayoutDemo.class));
                menuItem.addLeaf(new Leaf("ScrollView研究--仿商城详情", "", DemoScrollView.class));
                menuItem.addLeaf(new Leaf("ScrollView研究--仿商城详情2", "", DemoScrollView2.class));
                menuItem.addLeaf(new Leaf("ScrollView研究--配合webview", "", DemoScrollView3.class));
                menuItem.addLeaf(new Leaf("WebView研究", "", DemoWebView.class));
                menuItem.addLeaf(new Leaf("仿ScrollView", "", FakeScrollViewDemo.class));

                menuItem.addLeaf(new Leaf("入门级", "", RichBackgroundTextViewDemo.class));
                menuItem.addLeaf(new Leaf("CircleView：最简版本", "", CircleViewDemo.class));
                menuItem.addLeaf(new Leaf("CircleView：padding和wrap_content版本", "", CircleViewDemo2.class));
                menuItem.addLeaf(new Leaf("TextView文字闪烁", "", BlinkTextViewDemo.class));
                menuItem.addLeaf(new Leaf("圆形进度框", "", CircleLoadingViewDemo.class));
                menuItem.addLeaf(new Leaf("音乐频率波形", "", MusicWaveViewDemo.class));

                menuItem.addLeaf(new Leaf("仿ViewPager：简化版", "", FakeHorizontalScrollViewDemo.class));
                menuItem.addLeaf(new Leaf("StickyLayout", "", PinnedHeaderExpandableListViewDemo.class));
                menuItem.addLeaf(new Leaf("ViewDragHelper入门", "", ViewDragHelperDemoViewDemo.class));
                menuItem.addLeaf(new Leaf("ViewDragHelper：通用模板", "", ViewDragHelperDemoView2Demo.class));
            }

            menuItem = new MenuItem("组件", R.drawable.weixin_normal, R.drawable.weixin_pressed);
            m3.addMenuItem(menuItem);
            {
                menuItem.addLeaf(new Leaf("页面框架", "", DemoPage.class));
                menuItem.addLeaf(new Leaf("状态栏一体化", "", DemoStatusBarActivity.class, 1));
                menuItem.addLeaf(new Leaf("复杂列表：模板", "", DemoRecyclerViewActivity.class, 1));
                menuItem.addLeaf(new Leaf("列表模板", "", DemoRecyclerViewActivity.class, 1));
                menuItem.addLeaf(new Leaf("账号管理模板", "", DemoAccount.class));
                menuItem.addLeaf(new Leaf("自动升级模块", "", UpdateTestActivity.class));
                menuItem.addLeaf(new Leaf("调试日志", "", DemoLogReporterActivity.class, 1));
                menuItem.addLeaf(new Leaf("富文本编辑器", "", CreateThreadActivity.class, 1));
                menuItem.addLeaf(new Leaf("选图，选视频", "", DemoPickerActivity.class, 1));
                menuItem.addLeaf(new Leaf("视频压缩", "", DemoVideoComporessActivity.class, 1));

            }

        }

        ///--------------------------菜单1：开源
        m3 = new Menu("notify", R.drawable.find_normal, R.drawable.find_pressed);
        menus.add(m3);
        {
            MenuItem menuItem = new MenuItem("Dialog", R.drawable.weixin_normal, R.drawable.weixin_pressed);
            m3.addMenuItem(menuItem);
            {
                menuItem.addLeaf(new Leaf("----Dialog----", "", null));
                menuItem.addLeaf(new Leaf("通用Dialog", "", SimpleHomeActivity.class, 1));
                menuItem.addLeaf(new Leaf("ActionSheet", "", DemoActionSheetActivity.class));
                menuItem.addLeaf(new Leaf("SweetAlert", "", DemoSweetAlertActivity.class, 1));
                menuItem.addLeaf(new Leaf("LoadingDialog", "", DemoLoadingDialogActivity.class));

                menuItem.addLeaf(new Leaf("----Popup----", "", null));
                menuItem.addLeaf(new Leaf("弹框", "", DemoPopupWindowActivity.class, 1));

                menuItem.addLeaf(new Leaf("----Toaster----", "", null));
                menuItem.addLeaf(new Leaf("Toaster", "", DemoToastActivity.class));

                menuItem.addLeaf(new Leaf("----其他----", "", null));
                menuItem.addLeaf(new Leaf("nifty", "", DemoNiftyActivity.class));
                menuItem.addLeaf(new Leaf("SnackBar", "", null));
            }

        }


        ///--------------------------菜单1：开源
        m3 = new Menu("SDK", R.drawable.find_normal, R.drawable.find_pressed);
        menus.add(m3);
        {
            MenuItem menuItem4 = new MenuItem("sdk", R.drawable.weixin_normal, R.drawable.weixin_pressed);
            m3.addMenuItem(menuItem4);
            {
                menuItem4.addLeaf(new Leaf("ThreadManager", "", DemoThreadManager.class));
                menuItem4.addLeaf(new Leaf("定时任务", "", DemoJobActivity.class, 1));
                menuItem4.addLeaf(new Leaf("手机端http服务器", "", NanoHttpServerDemo.class));
                menuItem4.addLeaf(new Leaf("DB", "", DemoDbActivity.class, 1));
                menuItem4.addLeaf(new Leaf("Social", "", DemoSocialShareActivity.class, 1));
            }
        }
        return menus;
    }



    @Override
    protected void onCreate2(View contentView, @Nullable Bundle savedInstanceState) {
        super.onCreate2(contentView, savedInstanceState);
        //check(true, 234);

        PermissionUtils.permission(PermissionConstants.CALENDAR)
                .rationale(new PermissionUtils.OnRationaleListener() {
                    @Override
                    public void rationale(final ShouldRequest shouldRequest) {
                        Toaster.toastShort("拒绝过了都");
                        DialogHelper.showRationaleDialog(getActivity(), shouldRequest);
                    }
                })
                .callback(new PermissionUtils.FullCallback() {
                    @Override
                    public void onGranted(List<String> permissionsGranted) {
                        Toaster.toastShort("好");
                    }
                    @Override
                    public void onDenied(List<String> permissionsDeniedForever,
                                         List<String> permissionsDenied) {
//                        if (!permissionsDeniedForever.isEmpty()) {
//                            DialogHelper.showOpenAppSettingDialog(getActivity());
//                        }
                        DialogHelper.showOpenAppSettingDialog(getActivity());
                        Toaster.toastShort("完蛋");
                    }
                })
                .theme(new PermissionUtils.ThemeCallback() {
                    @Override
                    public void onActivityCreate(Activity activity) {
                        setFullScreen(activity);// 设置全屏
                    }
                })
                .request();

//        PermissionHelper.request(this, new PermissionHelper.OnPermissionGrantedListener() {
//            @Override
//            public void onPermissionGranted() {
//                Toaster.toastShort("好");
//            }
//        }, new PermissionHelper.OnPermissionDeniedListener() {
//            @Override
//            public void onPermissionDenied() {
//                Toaster.toastShort("完蛋");
//            }
//        }, PermissionConstants.STORAGE);
    }

    /**
     * 设置屏幕为全屏
     *
     * @param activity activity
     */
    public static void setFullScreen(@NonNull final Activity activity) {
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN
                | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

    @Override
    protected void onDestroy2() {
        super.onDestroy2();
    }

    void check(boolean isManual, final int notifyId) {
        UpdateManager.create(getActivity())
//                .setChecker(new IUpdateChecker() {
//                    @Override
//                    public void check(ICheckAgent agent, String url) {
//                        Log.e("ezy.update", "checking");
//                        agent.setInfo("");
//                    }
//                })
                .setUrl("http://114.215.81.196/service/apps/export/version?appid=2&lang=0").setManual(isManual).setNotifyId(notifyId).setParser(new IUpdateParser() {
            @Override
            public UpdateInfo parse(String source) throws Exception {
                Log.e("update", "返回：" + source);
                MyUpdateInfo m = JsonUtils.getBean(source, MyUpdateInfo.class);
                UpdateInfo info = new UpdateInfo();

                info.hasUpdate = m.result == 0;
                info.updateContent = m.context; //"• 支持文字、贴纸、背景音乐，尽情展现欢乐气氛；\n• 两人视频通话支持实时滤镜，丰富滤镜，多彩心情；\n• 图片编辑新增艺术滤镜，一键打造文艺画风；\n• 资料卡新增点赞排行榜，看好友里谁是魅力之王。";
                info.versionCode = m.code; // 587;
                info.versionName = m.version;// "v5.8.7";
                info.url = m.url; //mUpdateUrl;
                info.md5 = "update-app"; //"56cf48f10e4cf6043fbf53bbbc4009e3";
                info.size = m.file_size; // 10149314;
                info.isForce = m.compel != 0; //isForce;
                info.isIgnorable = false; //isIgnorable;
                info.isSilent = false; //isSilent;
                return info;
            }
        }).check();
    }
    private static class MyUpdateInfo{
        public int result;
        public int code;
        public String version;
        public String url;
        public long file_size;
        public String context;
        public int compel;
    }

    boolean overlayOpened = false;

    @Override
    protected void onResume() {

//        if(!overlayOpened) Toaster.toastShort("强烈建议您打开悬浮窗的权限，very酷炫");
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (!Settings.canDrawOverlays(getActivity())) {
//                Intent drawOverlaysSettingsIntent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
//                drawOverlaysSettingsIntent.setData(Uri.parse("package:" + getPackageName()));
//                startActivity(drawOverlaysSettingsIntent);
//            } else {
//                if(!overlayOpened) {
//                    AssistantHoverService.showFloatingMenu(getActivity());
//                    overlayOpened = true;
//                }
//            }
//        }else{
//            if(!overlayOpened) {
//                AssistantHoverService.showFloatingMenu(getActivity());
//                overlayOpened = true;
//            }
//        }

        super.onResume();
    }

}
