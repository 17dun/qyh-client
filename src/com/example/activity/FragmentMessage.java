package com.example.activity;

import com.example.activity.R;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FragmentMessage extends Fragment {
	private TextView tv;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_message, container,false);
		tv = (TextView) rootView.findViewById(R.id.titleTv);
		tv.setText("ÏûÏ¢");
		
		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}
}
