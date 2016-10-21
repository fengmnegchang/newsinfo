/**
 *****************************************************************************************************************************************************************************
 * 
 * @author :fengguangjing
 * @createTime:2016-10-21下午2:49:37
 * @version:4.2.4
 * @modifyTime:
 * @modifyAuthor:
 * @description:
 *****************************************************************************************************************************************************************************
 */
package com.example.padandphonedemo;

import com.example.newsinfo.R;
import com.example.newsinfo.fragment.HomeFragment;
import com.example.newsinfo.fragment.NewsFragment;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

/**
 ***************************************************************************************************************************************************************************** 
 * 
 * @author :fengguangjing
 * @createTime:2016-10-21下午2:49:37
 * @version:4.2.4
 * @modifyTime:
 * @modifyAuthor:
 * @description:
 ***************************************************************************************************************************************************************************** 
 */
public class NewsActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.news_activity);
		// Check that the activity is using the layout version with
		// the fragment_container FrameLayout
		if (findViewById(R.id.news_fragment) != null) {

			// However, if we're being restored from a previous state,
			// then we don't need to do anything and should return or else
			// we could end up with overlapping fragments.
			if (savedInstanceState != null) {
				return;
			}

			String title = getIntent().getStringExtra("TITLE");
			String url = getIntent().getStringExtra("URL");
			String jsondataurl = getIntent().getStringExtra("JSON_DATA_URL");
			// Create an instance of ExampleFragment
			// In case this activity was started with special instructions from
			// an Intent,
			// pass the Intent's extras to the fragment as arguments
			// Add the fragment to the 'fragment_container' FrameLayout
			if(title.equals("首页")){
				HomeFragment fragment = HomeFragment.newInstance(title,url,jsondataurl);
				fragment.setUserVisibleHint(true);
				getSupportFragmentManager().beginTransaction().add(R.id.news_fragment,fragment ).commit();
			}else{
				NewsFragment fragment = NewsFragment.newInstance(title,url,jsondataurl);
				fragment.setUserVisibleHint(true);
				getSupportFragmentManager().beginTransaction().add(R.id.news_fragment,fragment ).commit();
			}
			
		}
	}

}
