package com.example.activity;

import com.example.activity.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

public class MainActivity extends FragmentActivity {
	private Fragment[] mFragments;
	private RadioGroup bottomRg;
	private FragmentManager fragmentManager;
	private FragmentTransaction fragmentTransaction;
	private RadioButton rbOne, rbTwo, rbThree, rbFour;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		mFragments = new Fragment[3];
		fragmentManager = getSupportFragmentManager();
		mFragments[0] = fragmentManager.findFragmentById(R.id.fragement_main);
		mFragments[1] = fragmentManager.findFragmentById(R.id.fragement_addr);
		mFragments[2] = fragmentManager
				.findFragmentById(R.id.fragement_setting);
		fragmentTransaction = fragmentManager.beginTransaction()
				.hide(mFragments[0]).hide(mFragments[1]).hide(mFragments[2]);
		fragmentTransaction.show(mFragments[0]).commit();
		setFragmentIndicator();
	}
	public void addFragment(Fragment fragment){
		//fragmentManager.beginTransaction().add(R.id.fragmentContainer,fragment, "").addToBackStack("").commit();
	}
	public void back(Fragment fragment){
		fragmentManager.beginTransaction().remove(fragment);
	}
	@Override
	public void onBackPressed() {
		int count  = fragmentManager.getBackStackEntryCount();
		Toast.makeText(getApplicationContext(), ""+count, 0).show();
		if(count > 0){
			fragmentManager.popBackStack();
		}else{
			super.onBackPressed();
		}
	}
	private void setFragmentIndicator() {

		bottomRg = (RadioGroup) findViewById(R.id.bottomRg);
		rbOne = (RadioButton) findViewById(R.id.rbOne);
		rbTwo = (RadioButton) findViewById(R.id.rbTwo);
		rbThree = (RadioButton) findViewById(R.id.rbThree);

		bottomRg.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				fragmentTransaction = fragmentManager.beginTransaction()
						.hide(mFragments[0]).hide(mFragments[1])
						.hide(mFragments[2]);
				switch (checkedId) {
				case R.id.rbOne:
					fragmentTransaction.show(mFragments[0]).commit();
					break;

				case R.id.rbTwo:
					fragmentTransaction.show(mFragments[1]).commit();
					break;

				case R.id.rbThree:
					fragmentTransaction.show(mFragments[2]).commit();
					break;

				default:
					break;
				}
			}
		});
	}

}
