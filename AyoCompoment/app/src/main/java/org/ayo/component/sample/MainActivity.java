package org.ayo.component.sample;

import org.ayo.component.sample.master.DemoPage;
import org.ayo.component.sample.pager.DemoPagerActivity;
import org.ayo.component.sample.permission.system.PermissionMainActivity;
import org.ayo.component.sample.samplepage.FakeStandardActivity;
import org.ayo.component.sample.tab.DemoTabActivity;
import org.ayo.sample.menu.Leaf;
import org.ayo.sample.menu.MainPagerActivity;
import org.ayo.sample.menu.Menu;
import org.ayo.sample.menu.MenuItem;

import java.util.ArrayList;
import java.util.List;

import me.yokeyword.sample.EnterActivity;

public class MainActivity extends MainPagerActivity {

    @Override
    public List<Menu> getMenus() {
        List<Menu> arr = new ArrayList<>();

        Menu menu = new Menu("UI框架", R.drawable.typical_ic_weixin_normal, R.drawable.typical_ic_weixin_pressed);
        arr.add(menu);
        {
            MenuItem menuItem = new MenuItem("Master", R.drawable.typical_ic_weixin_normal, R.drawable.typical_ic_weixin_pressed);
            menu.addMenuItem(menuItem);
            {
                menuItem.addLeaf(new Leaf("原始Activity和Fragment", "", FakeStandardActivity.class, 1));
                menuItem.addLeaf(new Leaf("Fragmentation原生demo", "", EnterActivity.class));
                menuItem.addLeaf(new Leaf("免manifest模式--一个MasterFragment就是一个Activity", "注意模板Activity和MasterFragment的生命周期关系有问题，只能激活一个", DemoPage.class));
                menuItem.addLeaf(new Leaf("MasterFragment既可以当Activity用，也可以当Fragment用", "", null));
                menuItem.addLeaf(new Leaf("在此基础上，使用主MasterFragment管理子MasterFragment", "", null));
                menuItem.addLeaf(new Leaf("MasterFragment：Tab模式-无回退栈", "", DemoTabActivity.class));
                menuItem.addLeaf(new Leaf("MasterFragment：ViewPager模式--无回退栈", "", DemoPagerActivity.class));
                menuItem.addLeaf(new Leaf("MasterFragment：Nest模式--带回退栈", "", null));
                menuItem.addLeaf(new Leaf("MasterFragment：Compose模式--同时显示，无回退栈", "", null));
                menuItem.addLeaf(new Leaf("MasterFragment的Schema支持--必须配合模板Activity", "", null));
                menuItem.addLeaf(new Leaf("MasterFragment的内存重启支持", "", null));
                menuItem.addLeaf(new Leaf("Swipeback支持", "", null));
                menuItem.addLeaf(new Leaf("过场动画支持", "", null));
                menuItem.addLeaf(new Leaf("Transition动画支持", "", null));
            }
        }

        menu = new Menu("权限", R.drawable.typical_ic_weixin_normal, R.drawable.typical_ic_weixin_pressed);
        arr.add(menu);
        {
            MenuItem menuItem = new MenuItem("原生代码", R.drawable.typical_ic_weixin_normal, R.drawable.typical_ic_weixin_pressed);
            menu.addMenuItem(menuItem);
            {
                menuItem.addLeaf(new Leaf("谷歌动态权限demo", "", PermissionMainActivity.class, 1));
            }

            menuItem = new MenuItem("工具类", R.drawable.typical_ic_weixin_normal, R.drawable.typical_ic_weixin_pressed);
            menu.addMenuItem(menuItem);
            {
                menuItem.addLeaf(new Leaf("一个挺好用的小工具类", "", PermissionMainActivity.class, 1));
            }
        }

        return arr;
    }

}
