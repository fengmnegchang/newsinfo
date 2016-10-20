package com.example.newsinfo.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.example.newsinfo.R;
import com.example.newsinfo.fragment.NewsFragment;
import com.example.newsinfo.indicator.TabPageIndicator;

public class SampleTabsActivity extends FragmentActivity {
    private static final String[] CONTENT = new String[] {  "热点", "社会", "股票", "美女", "漫画",
    	 "搞笑", "科技", "互联网", "财经", "军事", "体育"};
    private static final String[] URL = new String[] {
    	"http://www.yidianzixun.com/home?page=channel&id=hot", 
    	"http://www.yidianzixun.com/home?page=channel&keyword=%E7%A4%BE%E4%BC%9A",
    	"http://www.yidianzixun.com/home?page=channel&keyword=%E8%82%A1%E7%A5%A8", 
    	"http://www.yidianzixun.com/home?page=channel&keyword=%E7%BE%8E%E5%A5%B3", 
    	"http://www.yidianzixun.com/home?page=channel&keyword=%E6%BC%AB%E7%94%BB",
   	    "http://www.yidianzixun.com/home?page=channel&keyword=%E6%90%9E%E7%AC%91", 
   	    "http://www.yidianzixun.com/home?page=channel&keyword=%E7%A7%91%E6%8A%80", 
   	    "http://www.yidianzixun.com/home?page=channel&keyword=%E4%BA%92%E8%81%94%E7%BD%91", 
   	    "http://www.yidianzixun.com/home?page=channel&keyword=%E8%B4%A2%E7%BB%8F", 
   	    "http://www.yidianzixun.com/home?page=channel&keyword=%E5%86%9B%E4%BA%8B", 
   	    "http://www.yidianzixun.com/home?page=channel&keyword=%E4%BD%93%E8%82%B2"};
    
    private static final String[] JSON_DATA_URL = new String[] {"http://www.yidianzixun.com/api/q/?path=channel|news-list-for-keyword&display=%E7%83%AD%E7%82%B9&word_type=token&fields=docid&fields=category&fields=date&fields=image&fields=image_urls&fields=like&fields=source&fields=title&fields=url&fields=comment_count&fields=summary&fields=up&version=999999&infinite=true", 
    	"http://www.yidianzixun.com/api/q/?path=channel|news-list-for-keyword&display=%E7%A4%BE%E4%BC%9A&word_type=token&fields=docid&fields=category&fields=date&fields=image&fields=image_urls&fields=like&fields=source&fields=title&fields=url&fields=comment_count&fields=summary&fields=up&version=999999&infinite=true",
    	"http://www.yidianzixun.com/api/q/?path=channel|news-list-for-keyword&display=%E8%82%A1%E7%A5%A8&word_type=token&fields=docid&fields=category&fields=date&fields=image&fields=image_urls&fields=like&fields=source&fields=title&fields=url&fields=comment_count&fields=summary&fields=up&version=999999&infinite=true",
    	"http://www.yidianzixun.com/api/q/?path=channel|news-list-for-channel&channel_id=u241&fields=docid&fields=category&fields=date&fields=image&fields=image_urls&fields=like&fields=source&fields=title&fields=url&fields=comment_count&fields=summary&fields=up&version=999999&infinite=true",
    	"http://www.yidianzixun.com/api/q/?path=channel|news-list-for-keyword&display=%E6%BC%AB%E7%94%BB&word_type=token&fields=docid&fields=category&fields=date&fields=image&fields=image_urls&fields=like&fields=source&fields=title&fields=url&fields=comment_count&fields=summary&fields=up&version=999999&infinite=true",
    	"http://www.yidianzixun.com/api/q/?path=channel|news-list-for-keyword&display=%E6%90%9E%E7%AC%91&word_type=token&fields=docid&fields=category&fields=date&fields=image&fields=image_urls&fields=like&fields=source&fields=title&fields=url&fields=comment_count&fields=summary&fields=up&version=999999&infinite=true",
    	"http://www.yidianzixun.com/api/q/?path=channel|news-list-for-keyword&display=%E7%A7%91%E6%8A%80&word_type=token&fields=docid&fields=category&fields=date&fields=image&fields=image_urls&fields=like&fields=source&fields=title&fields=url&fields=comment_count&fields=summary&fields=up&version=999999&infinite=true",
    	"http://www.yidianzixun.com/api/q/?path=channel|news-list-for-keyword&display=%E4%BA%92%E8%81%94%E7%BD%91&word_type=token&fields=docid&fields=category&fields=date&fields=image&fields=image_urls&fields=like&fields=source&fields=title&fields=url&fields=comment_count&fields=summary&fields=up&version=999999&infinite=true",
    	"http://www.yidianzixun.com/api/q/?path=channel|news-list-for-keyword&display=%E8%B4%A2%E7%BB%8F&word_type=token&fields=docid&fields=category&fields=date&fields=image&fields=image_urls&fields=like&fields=source&fields=title&fields=url&fields=comment_count&fields=summary&fields=up&version=999999&infinite=true",
    	"http://www.yidianzixun.com/api/q/?path=channel|news-list-for-keyword&display=%E5%86%9B%E4%BA%8B&word_type=token&fields=docid&fields=category&fields=date&fields=image&fields=image_urls&fields=like&fields=source&fields=title&fields=url&fields=comment_count&fields=summary&fields=up&version=999999&infinite=true",
    	"http://www.yidianzixun.com/api/q/?path=channel|news-list-for-keyword&display=%E4%BD%93%E8%82%B2&word_type=token&fields=docid&fields=category&fields=date&fields=image&fields=image_urls&fields=like&fields=source&fields=title&fields=url&fields=comment_count&fields=summary&fields=up&version=999999&infinite=true"
    	};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.simple_tabs);

        FragmentPagerAdapter adapter = new GoogleMusicAdapter(getSupportFragmentManager());
        ViewPager pager = (ViewPager)findViewById(R.id.pager);
        pager.setAdapter(adapter);
        

        TabPageIndicator indicator = (TabPageIndicator)findViewById(R.id.indicator);
        indicator.setViewPager(pager);
    }

    class GoogleMusicAdapter extends FragmentPagerAdapter {
        public GoogleMusicAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return NewsFragment.newInstance(CONTENT[position % CONTENT.length],URL[position % URL.length],JSON_DATA_URL[position % JSON_DATA_URL.length]);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return CONTENT[position % CONTENT.length].toUpperCase();
        }

        @Override
        public int getCount() {
          return CONTENT.length;
        }
    }
}
