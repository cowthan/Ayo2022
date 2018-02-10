package org.ayo.template.recycler.condition;

/**
 * 查询列表的条件，可能需要传入一堆参数，而不仅仅是page
 * @author cowthan
 *
 */
public class AyoCondition {

	public int page;
	public int pageStart = 0;

	public AyoCondition(int pageStart){
		this.pageStart = pageStart;
		page = pageStart;
	}

	public void onPullDown() {
		page = pageStart;
	}

	public void onPullUp() {
		page++;
	}

	public void reset() {
		page = pageStart;
	}
}
