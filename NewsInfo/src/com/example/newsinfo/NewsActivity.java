/**
 *****************************************************************************************************************************************************************************
 * 
 * @author :fengguangjing
 * @createTime:2016-10-17下午5:01:53
 * @version:4.2.4
 * @modifyTime:
 * @modifyAuthor:
 * @description:
 *****************************************************************************************************************************************************************************
 */
package com.example.newsinfo;

import java.util.List;

import android.os.Bundle;

 

/**
 *****************************************************************************************************************************************************************************
 * 
 * @author :fengguangjing
 * @createTime:2016-10-17下午5:01:53
 * @version:4.2.4
 * @modifyTime:
 * @modifyAuthor:
 * @description:
 *****************************************************************************************************************************************************************************
 */
public class NewsActivity extends BaseFragmentActivity {
	public static final int FRAGMENT_ONE = 0;
    public static final int FRAGMENT_TWO = 1;
    public static final int FRAGMENT_THREE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int supplyTabs(List<TabInfo> tabs) {
        tabs.add(new TabInfo(FRAGMENT_ONE, "首页",
        		NewsFragment.class));
        tabs.add(new TabInfo(FRAGMENT_TWO, "热点",
        		NewsFragment.class));
        tabs.add(new TabInfo(FRAGMENT_THREE, "社会",
        		NewsFragment.class));
        tabs.add(new TabInfo(FRAGMENT_THREE, "股票",
        		NewsFragment.class));
        tabs.add(new TabInfo(FRAGMENT_THREE, "美女",
        		NewsFragment.class));
        tabs.add(new TabInfo(FRAGMENT_THREE, "漫画",
        		NewsFragment.class));
        tabs.add(new TabInfo(FRAGMENT_THREE, "搞笑",
        		NewsFragment.class));
        tabs.add(new TabInfo(FRAGMENT_THREE, "科技",
        		NewsFragment.class));
        tabs.add(new TabInfo(FRAGMENT_THREE, "互联网",
        		NewsFragment.class));
        tabs.add(new TabInfo(FRAGMENT_THREE, "财经",
        		NewsFragment.class));
        tabs.add(new TabInfo(FRAGMENT_THREE, "军事",
        		NewsFragment.class));
        tabs.add(new TabInfo(FRAGMENT_THREE, "体育",
        		NewsFragment.class));
        return FRAGMENT_ONE;
    }

}
