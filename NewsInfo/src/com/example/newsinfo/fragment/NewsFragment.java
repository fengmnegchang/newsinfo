package com.example.newsinfo.fragment;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.newsinfo.R;
import com.example.newsinfo.adapter.NewsAdapter;
import com.example.newsinfo.bean.NewsBean;

public final class NewsFragment extends Fragment {
    private static final String KEY_CONTENT = "TestFragment:Content";

    public static NewsFragment newInstance(String content) {
    	NewsFragment fragment = new NewsFragment();
        fragment.mContent = content;
        return fragment;
    }

    private String mContent = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if ((savedInstanceState != null) && savedInstanceState.containsKey(KEY_CONTENT)) {
            mContent = savedInstanceState.getString(KEY_CONTENT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_list, null);
        ListView listview = (ListView) view.findViewById(R.id.listview);
        ArrayList<NewsBean> list = new ArrayList<NewsBean>();
        list.add(new NewsBean());
        list.add(new NewsBean());
        list.add(new NewsBean());
        NewsAdapter mNewsAdapter = new NewsAdapter(getActivity(),list);
        listview.setAdapter(mNewsAdapter);
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_CONTENT, mContent);
    }
}
