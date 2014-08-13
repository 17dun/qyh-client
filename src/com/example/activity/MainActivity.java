package com.example.activity;

import com.example.activity.R;

import android.graphics.Color;
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
	private RadioButton rbOne, rbTwo, rbThree, rbFour,rbFive;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		mFragments = new Fragment[5];
		fragmentManager = getSupportFragmentManager();
		mFragments[0] = fragmentManager.findFragmentById(R.id.fragement_main);
		mFragments[1] = fragmentManager.findFragmentById(R.id.fragement_addr);
		mFragments[2] = fragmentManager.findFragmentById(R.id.fragement_add);
		mFragments[3] = fragmentManager.findFragmentById(R.id.fragement_message);
		mFragments[4] = fragmentManager.findFragmentById(R.id.fragement_setting);
		fragmentTransaction = fragmentManager.beginTransaction()
				.hide(mFragments[0]).hide(mFragments[1]).hide(mFragments[2]).hide(mFragments[3]).hide(mFragments[4]);
		fragmentTransaction.show(mFragments[0]).commit();
		rbOne = (RadioButton) findViewById(R.id.rbOne);
		rbOne.setTextColor(Color.parseColor("#389cff"));
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
		rbFour = (RadioButton) findViewById(R.id.rbFour);
		rbFive = (RadioButton) findViewById(R.id.rbFive);

		bottomRg.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				fragmentTransaction = fragmentManager.beginTransaction()
						.hide(mFragments[0]).hide(mFragments[1])
						.hide(mFragments[2]).hide(mFragments[3]).hide(mFragments[4]);

				rbOne.setTextColor(Color.parseColor("#000000"));
				rbTwo.setTextColor(Color.parseColor("#000000"));
				rbThree.setTextColor(Color.parseColor("#000000"));
				rbFour.setTextColor(Color.parseColor("#000000"));
				rbFive.setTextColor(Color.parseColor("#000000"));
				switch (checkedId) {
				case R.id.rbOne:
					rbOne.setTextColor(Color.parseColor("#389cff"));
					fragmentTransaction.show(mFragments[0]).commit();
					break;

				case R.id.rbTwo:
					rbTwo.setTextColor(Color.parseColor("#389cff"));
					fragmentTransaction.show(mFragments[1]).commit();
					break;

				case R.id.rbThree:
					rbThree.setTextColor(Color.parseColor("#389cff"));
					fragmentTransaction.show(mFragments[2]).commit();
					break;

				case R.id.rbFour:
					rbFour.setTextColor(Color.parseColor("#389cff"));
					fragmentTransaction.show(mFragments[3]).commit();
					break;
				case R.id.rbFive:
					rbFive.setTextColor(Color.parseColor("#389cff"));
					fragmentTransaction.show(mFragments[3]).commit();
					break;
				default:
					break;
				}
			}
		});
	}

}
