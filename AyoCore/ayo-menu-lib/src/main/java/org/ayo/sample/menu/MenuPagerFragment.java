package org.ayo.sample.menu;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import org.ayo.component.MasterFragment;
import org.ayo.component.StateModel;
import org.ayo.sample.menu.widget.CustomRadioGroup;

import java.util.ArrayList;


/**
 *
 * @author cowthan
 *
 */
public class MenuPagerFragment extends MasterFragment implements View.OnClickListener{

	private CustomRadioGroup footer;
	private ViewPager body;

	private ArrayList<Fragment> fragments = new ArrayList<Fragment>();
	private Menu menu;

	public void setMenu(Menu menu){
		this.menu = menu;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putSerializable("menu", menu);
	}

	@Override
	protected StateModel createStateModel() {
		return null;
	}

	@Override
	protected int getLayoutId() {
		return R.layout.frag_memu_pager;
	}


	@Override
	protected void onCreate2(View contentView,  @Nullable Bundle savedInstanceState) {
		if(savedInstanceState != null){
			menu = (Menu) savedInstanceState.getSerializable("menu");
		}
		// 底部
		footer = (CustomRadioGroup) findViewById(R.id.main_footer);
		footer.setBackgroundColor(Color.parseColor("#94d8b6"));
		int count = menu != null && menu.subMenus != null ? menu.subMenus.size() : 0;
		for (int i = 0; i < count; i++) {
			footer.addItem(menu.subMenus.get(i).iconNormal,
					menu.subMenus.get(i).iconPressed,
					menu.subMenus.get(i).name);
		}
		// 主体
		body = (ViewPager) findViewById(R.id.main_body);

		final MainBodyPageChangeListener bodyChangeListener = new MainBodyPageChangeListener(footer);
		body.addOnPageChangeListener(bodyChangeListener);

		initFragments();
		body.setOffscreenPageLimit(4);
		MainPagerAdapter mAdapetr = new MainPagerAdapter(getChildFragmentManager(), fragments);
		body.setAdapter(mAdapetr);
		body.setCurrentItem(0);

		footer.setCheckedIndex(body.getCurrentItem());
		footer.setOnItemChangedListener(new CustomRadioGroup.OnItemChangedListener() {
			public void onItemChanged() {
				body.setCurrentItem(footer.getCheckedIndex(), false);
			}
		});

		//footer.setItemNewsCount(1, 10);// 设置消息数量
	}

	@Override
	protected void onDestroy2() {

	}

	@Override
	protected void onPageVisibleChanged(boolean visible, boolean isFirstTimeVisible, @Nullable Bundle savedInstanceState) {

	}


	private void initFragments() {
		fragments.clear();
		for(MenuItem menuItem: menu.subMenus){
			MenuItemFragment frag1 = new MenuItemFragment();
			frag1.setMenu(menuItem);
			fragments.add(frag1);

		}

	}

	class MainBodyPageChangeListener implements ViewPager.OnPageChangeListener {
		private CustomRadioGroup customRadioGroup;

		public MainBodyPageChangeListener(CustomRadioGroup c) {
			this.customRadioGroup = c;
		}

		public void onPageScrollStateChanged(int arg0) {
		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {
			if (arg1 != 0.0f) {
				int right, left;
				if (arg0 == customRadioGroup.getCheckedIndex()) {
					// 方向向右
					left = customRadioGroup.getCheckedIndex();
					right = customRadioGroup.getCheckedIndex() + 1;
				} else {
					// 方向向左
					left = customRadioGroup.getCheckedIndex() - 1;
					right = customRadioGroup.getCheckedIndex();

				}
				customRadioGroup.itemChangeChecked(left, right, arg1);
			} else {
				customRadioGroup.setCheckedIndex(arg0);
			}
		}

		public void onPageSelected(int arg0) {
			//titlebar.title(menu.subMenus.get(arg0).name);
		}

	}


	@Override
	public void onClick(View v) {

	}

	class MainPagerAdapter extends FragmentStatePagerAdapter {
		private ArrayList<Fragment> fragments;
		private FragmentManager fm;

		public MainPagerAdapter(FragmentManager fm) {
			super(fm);
			this.fm = fm;
		}

		public MainPagerAdapter(FragmentManager fm,
								ArrayList<Fragment> fragments) {
			super(fm);
			this.fm = fm;
			this.fragments = fragments;
		}

		@Override
		public int getCount() {
			return fragments.size();
		}

		@Override
		public Fragment getItem(int position) {
			return fragments.get(position);
		}


		@Override
		public void finishUpdate(ViewGroup container) {
			super.finishUpdate(container);
		}

	}
}
