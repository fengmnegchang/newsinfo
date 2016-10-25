/**
 *****************************************************************************************************************************************************************************
 * 
 * @author :fengguangjing
 * @createTime:2016-10-25下午3:56:42
 * @version:4.2.4
 * @modifyTime:
 * @modifyAuthor:
 * @description:
 *****************************************************************************************************************************************************************************
 */
package com.example.newsinfo.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.TextView;

import com.example.newsinfo.R;
import com.example.newsinfo.bean.NewsBean;
import com.example.newsinfo.fragment.NewsFragment;

/**
 ***************************************************************************************************************************************************************************** 
 * 
 * @author :fengguangjing
 * @createTime:2016-10-25下午3:56:42
 * @version:4.2.4
 * @modifyTime:
 * @modifyAuthor:
 * @description:
 ***************************************************************************************************************************************************************************** 
 */
public class SearchResultActivity extends FragmentActivity {

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_result);
		
		
		// Check that the activity is using the layout version with
		// the fragment_container FrameLayout
		if (findViewById(R.id.search_fragment) != null) {

			// However, if we're being restored from a previous state,
			// then we don't need to do anything and should return or else
			// we could end up with overlapping fragments.
			if (savedInstanceState != null) {
				return;
			}

			 
			NewsBean bean = (NewsBean) getIntent().getSerializableExtra("NEWSBEAN");
			((TextView)findViewById(R.id.search_title)).setText(bean.getTitle());
			// Create an instance of ExampleFragment
			// In case this activity was started with special instructions from
			// an Intent,
			// pass the Intent's extras to the fragment as arguments
			// Add the fragment to the 'fragment_container' FrameLayout

			///home?page=channel&keyword=%E9%9D%92%E4%BA%91%E5%BF%97
			
			String jsondataurl = "http://www.yidianzixun.com/api/q/?path=channel|news-list-for-keyword&word_type=token&fields=docid&fields=category&fields=date&fields=image&fields=image_urls&fields=like&fields=source&fields=title&fields=url&fields=comment_count&fields=summary&fields=up&version=999999&infinite=true";
			jsondataurl = jsondataurl+"&display="+bean.getKeyword();
			NewsFragment fragment = NewsFragment.newInstance(bean.getTitle(), bean.getUrl(), jsondataurl);//&display=%E7%BE%8E%E5%AE%B9
			fragment.setUserVisibleHint(true);
			getSupportFragmentManager().beginTransaction().add(R.id.search_fragment, fragment).commit();

		}

	}
}
