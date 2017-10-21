package org.ayo.component.indicator;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.ayo.component.R;

import java.util.ArrayList;
import java.util.List;


/**
 * 定制的 组件
 *  描述：
 *  	横向排列N个条目组件
 *  关联Resource:
 *  	layout: 	custom_radio_button.xml
 *  	drawable: 	page_indicator_focused.png		
 */

public class TypicalIndicator extends LinearLayout implements PageIndicator<TypicalPageInfo>{
	/**
	 * IMAGE/TEXT 	:	条目的图片/文字  
	 * START/DIF	: 	初始值/和目标的差值
	 * R/G/B		：  	目标颜色RGB格式下的R/G/B
	 */
	private static final int
			TEXT_START_COLOR = Color.GRAY,  ///icon起始颜色，即未选中时颜色
			END_COLOR = Color.parseColor("#45c01a"), ///icon选中颜色
			TEXT_START_R = Color.red(TEXT_START_COLOR),
			TEXT_START_G = Color.green(TEXT_START_COLOR),
			TEXT_START_B = Color.blue(TEXT_START_COLOR),
			TEXT_DIF_R = Color.red(END_COLOR)-TEXT_START_R,  ///onScroll时，颜色red分量的变化范围
			TEXT_DIF_G = Color.green(END_COLOR)-TEXT_START_G,
			TEXT_DIF_B = Color.blue(END_COLOR)-TEXT_START_B;

	private final int
			ID_LAYOUT = R.layout.typical_indicator,
			ID_IMAGE_TOP = R.id.custom_radio_button_image_top,
			ID_IMAGE_BOTTOM = R.id.custom_radio_button_image_botom,
			ID_TEXT = R.id.custom_radio_button_text,
			ID_NEWS = R.id.custom_radio_button_news;

	//条目变更监听
	private OnTabSelectedListener onItemChangedListener;
	//条目的LinearLayout.LayoutParams
	private LayoutParams itemLayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
	private LayoutInflater inflater;
	//当前选择的条目
	private int checkedIndex=0;
	//条目列表
	private List<RadioButton> lists=new ArrayList<RadioButton>();
	public TypicalIndicator(Context c) {
		super(c);
		init();
	}
	public TypicalIndicator(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();	
	}
	/**
	 * 构造函数里适用的初始化部分
	 */
	private void init() {
		inflater=LayoutInflater.from(getContext());
		itemLayoutParams.weight=1;
		setOrientation(HORIZONTAL);

	}
	/**
	 * @param f 		颜色渐变参考值。
	 */
	private static int getNewColor(float f){
		int newR,newG,newB;
		newR=(int)(TEXT_DIF_R*f)+TEXT_START_R;
		newG=(int)(TEXT_DIF_G*f)+TEXT_START_G;
		newB=(int)(TEXT_DIF_B*f)+TEXT_START_B;
		return Color.rgb(newR, newG, newB);
	}

	/**
	 * 两个item 改变透明度
	 * @param leftIndex 	左边的条目索引
	 * @param rightIndex	右边的条目索引
	 * @param alpha			[0,1)透明度
	 */
	private void itemChangeChecked(int leftIndex,int rightIndex,float alpha){
		if (leftIndex<0||leftIndex>=lists.size()||rightIndex<0||rightIndex>=lists.size()) {
			return ;
		}
		RadioButton a=lists.get(leftIndex);
		RadioButton b=lists.get(rightIndex);
		a.top.setAlpha(alpha);
		a.bottom.setAlpha(1-alpha);
		b.top.setAlpha(1-alpha);
		b.bottom.setAlpha(alpha);
		int aColor=getNewColor(1-alpha);
		int bColor=getNewColor(alpha);
		a.text.setTextColor(aColor);
		b.text.setTextColor(bColor);
	}
	/**
	 * 设置指定索引的条目的消息数量
	 * @param index 条目的索引 
	 * @param count	消息的数量
	 */
	public void setItemNewsCount(int index,int count){
		lists.get(index).setNewsCount(count);
	}

	@Override
	public void addTab(TypicalPageInfo tabInfo) {
		RadioButton rb=new RadioButton(tabInfo.resNormal, tabInfo.resPressed, tabInfo.title);
		final int i = lists.size();
		rb.v.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				setCurrentItem(i, true);
			}
		});
		addView(rb.v);
		lists.add(rb);
	}

	@Override
	public void setOnTabSelectListener(OnTabSelectedListener onTabSelectedListener) {
		this.onItemChangedListener = onTabSelectedListener;
	}

	@Override
	public void setCurrentItem(int position) {
		setCurrentItem(position, false);
	}
	private void setCurrentItem(int position, boolean clickMySelf) {
		for (int i = 0; i < lists.size(); i++) {
			if (i == position) {
				lists.get(i).setCheceked(true);
				lists.get(i).text.setTextColor(END_COLOR);
			}else{
				lists.get(i).setCheceked(false);
				lists.get(i).text.setTextColor(TEXT_START_COLOR);
			}
		}
		int pre = this.checkedIndex;
		this.checkedIndex = position;
		if (this.onItemChangedListener!=null) {
			if(clickMySelf) onItemChangedListener.onTabSelected(checkedIndex, pre);
		}
	}

	@Override
	public void setupWithViewPager(ViewPager pager) {
		pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
				if (positionOffset != 0.0f) {
					int right, left;
					if (position == getCurrentItemPosition()) {
						// 方向向右
						left = getCurrentItemPosition();
						right = getCurrentItemPosition() + 1;
					} else {
						// 方向向左
						left = getCurrentItemPosition() - 1;
						right = getCurrentItemPosition();

					}
					itemChangeChecked(left, right, positionOffset);
				} else {
					setCurrentItem(position);
				}
			}

			@Override
			public void onPageSelected(int position) {

			}

			@Override
			public void onPageScrollStateChanged(int state) {

			}
		});
	}

	public int getCurrentItemPosition() {
		return checkedIndex;
	}

	/**
	 * 自定义的RadioButton
	 */
    private	class RadioButton {
		View v;				//条目样式
		ImageView top,bottom;	//条目的图片
		TextView text,news;	//条目的文字，消息
		public RadioButton(int unSelected,int selected,String string) {
			v=inflater.inflate(ID_LAYOUT, null);
			top=(ImageView)v.findViewById(ID_IMAGE_TOP);
			bottom=(ImageView)v.findViewById(ID_IMAGE_BOTTOM);
			text=(TextView)v.findViewById(ID_TEXT);
			news=(TextView)v.findViewById(ID_NEWS);
			
			top.setImageResource(unSelected);
			top.setAlpha(1.0f);
			bottom.setImageResource(selected);
			bottom.setAlpha(0.0f);
			text.setText(string);
			news.setVisibility(INVISIBLE);
			v.setLayoutParams(itemLayoutParams);
		}
		/**
		 * 设置消息数量
		 */
		void setCheceked(boolean b){
			if (b) {
				top.setAlpha(0.0f);
				bottom.setAlpha(1.0f);
			}else{
				top.setAlpha(1.0f);
				bottom.setAlpha(0.0f);
			}
		}
		
		void setNewsCount(int count){
			if (count<=0) {
				news.setVisibility(INVISIBLE);
			}else {
				news.setText(count+"");
				news.setVisibility(VISIBLE);
			}
		}
	}
}
