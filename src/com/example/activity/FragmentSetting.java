package com.example.activity;

import com.example.activity.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;

public class FragmentSetting extends Fragment {
	private TextView tv;

	@SuppressWarnings("unused")
	private static final String LTAG = FragmentSetting.class.getSimpleName();
	private MapView mMapView;
	private BaiduMap mBaiduMap;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View arg1 = inflater.inflate(R.layout.listitem, null);
//		mMapView = new MapView(getActivity(), new BaiduMapOptions());
//		
//		mBaiduMap = mMapView.getMap();
		return arg1;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
//		tv = (TextView) getView().findViewById(R.id.titleTv);
//		tv.setText("…Ë÷√");
	}
}
