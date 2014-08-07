package com.example.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class UserInfoActivity extends Activity {
	static String server_ip = "192.168.1.103";
	Intent intent;  
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.userinfo);
		intent=this.getIntent();  
		TextView textView = (TextView)findViewById(R.id.infoText);
		TextView workView = (TextView)findViewById(R.id.work);
		//ImageView picBox = (ImageView)findViewById(R.id.infoHead);
		Button btn = (Button)findViewById(R.id.returnBtn);
		btn.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
		btn.setOnClickListener(new Button.OnClickListener(){//��������    
            public void onClick(View v) {  
        		finish();  
            }
        });
		
		Intent intent = getIntent();
		String name = intent.getStringExtra("name");
		String work = intent.getStringExtra("work");
		//String pic = intent.getStringExtra("pic");
		long id = intent.getLongExtra("id",0);//�ڶ���������ʾĬ��ֵ������˵���Ȣ���������صڶ����������ֵ�����������˵���Ȣ������˭�ܹ���id��long��ֵ���ͷ���0
		textView.setText(name+"     id="+id);
		workView.setText(work);
		//String picUrl = "http://"+server_ip+":8080/images/"+pic;
	}
	
}
