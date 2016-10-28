package com.example.padandphonedemo;

import com.example.newsinfo.R;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
/**
 * 
 *****************************************************************************************************************************************************************************
 * pad phone适配页面
 * @author :fengguangjing
 * @createTime:2016-10-28上午10:36:30
 * @version:4.2.4
 * @modifyTime:
 * @modifyAuthor:
 * @description:
 *****************************************************************************************************************************************************************************
 */
public class MainActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Check that the activity is using the layout version with
		// the fragment_container FrameLayout
		if (findViewById(R.id.menu_fragment) != null) {

			// However, if we're being restored from a previous state,
			// then we don't need to do anything and should return or else
			// we could end up with overlapping fragments.
			if (savedInstanceState != null) {
				return;
			}

			// Create an instance of ExampleFragment
			MenuFragment firstFragment = new MenuFragment();
			// In case this activity was started with special instructions from
			// an Intent,
			// pass the Intent's extras to the fragment as arguments
			firstFragment.setArguments(getIntent().getExtras());
			// Add the fragment to the 'fragment_container' FrameLayout
			getSupportFragmentManager().beginTransaction().add(R.id.menu_fragment, firstFragment).commit();
		}
	}

}
