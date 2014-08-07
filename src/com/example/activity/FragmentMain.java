package com.example.activity;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.model.Person;
import com.example.provider.JSONProvider;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;


public class FragmentMain extends Fragment implements OnClickListener {
	private TextView tv;
	static String server_ip = "192.168.1.103";
	FirstAdapter adapter;
	ListView listView;
	String strjson = "";

	private final int TIME_UP = 1;
	private final int TIME_UP2 = 2;

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == TIME_UP) {
				// handler里面的方法是运行在主线程的
				List<Person> data = getDataByJson(strjson);
				adapter = new FirstAdapter(getActivity(), data);// Fragment
																// 里没有getApplicationContext()
																// 这个方法，一般在这里用getActivity()方法代理
				adapter.setListView(listView);
				listView.setAdapter(adapter);
				listView.setOnItemClickListener(new OnItemClickListener() {// 为listView的每个item创建点击事件
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						Intent intent = new Intent(getActivity(),UserInfoActivity.class);// 实例化一个调转页面     不是activity的话不能用这种方式  UserInfoActivity这个名字太有误导性了，一般是fragment的话就别起带activity的名字 那把名字改改
						intent.putExtra("name", adapter.getItem(arg2).getName());// 传参
						intent.putExtra("work", adapter.getItem(arg2).getWork());// 传参
						intent.putExtra("far", adapter.getItem(arg2).getFar());// 传参
						intent.putExtra("style", adapter.getItem(arg2).getStyle());// 传参
						intent.putExtra("id", arg3);
						startActivity(intent);// 跳转

					}
				});
			} else if (msg.what == TIME_UP2) {
				List<Person> data = getDataByJson(strjson);
				adapter.addData(data);
			}
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_main, container,false);
		listView = (ListView) rootView.findViewById(R.id.listView);
		tv = (TextView) rootView.findViewById(R.id.titleTv);
		tv.setText("找球友");
		new Thread(new Runnable() {// 刚才那个错的意思是在主线程发起了网络请求，android4.0之后对网络访问做了限制，默认情况下网络访问不能放在主线程里，不然会报错
					@Override
					public void run() {
						try {
							strjson = JSONProvider.getJSONData("http://"+server_ip+":888/?method=getUserList");
							
							/*
							 * android手机就相当于一个小linux系统
							 * 写127.0.0.1，相当于请求到系统自己的地址了，
							 * 但系统这个地址又不对外提供服务，就refused了
							 */
							
							Log.i("strjson", strjson);
						} catch (IOException e) {
							e.printStackTrace();
						}
						Message msg = new Message();
						msg.what = TIME_UP;
						handler.sendMessage(msg);
					}// 我本地也连接的代理
				}).start();
		return rootView;
	}

	static class FirstAdapter extends BaseAdapter {// 一般情况下，使用listview，gridview,gallery等，都学要自定义一个数据适配器，并继承BaseAdaper,有些直接从数据库取数据展示的，可能会用到SimpleCursorAdapter，不过一般用的少，继承BaseAdapter的比较多
		private Context context;
		private List<Person> dataList;
		private ListView listView;

		public FirstAdapter(Context context, List<Person> dataList) {
			this.context = context;
			this.dataList = dataList;
		}

		public void setListView(ListView listView) {
			this.listView = listView;
		}

		@Override
		public int getCount() {// 这个方法用来告诉listview一共有多少数据
			return dataList.size();
		}

		@Override
		public Person getItem(int arg0) {// 这个方法返回特定位置的数据

			return dataList.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {// 这个方法返回特定位置数据的id，一般直接返回位置即可，如果是从数据库取的数据，如用了simpleCursorAdapter等的话，这里返回的是数据库的主键ID

			return dataList.get(arg0).getId();
		}

		public void addData(Person tx) {
			dataList.add(tx);
			notifyDataSetChanged();
		}

		public void addData(List<Person> datas) {
			dataList.addAll(datas);
			notifyDataSetChanged();
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {// 适配器的主要功能，在这里初始化数据
			if (arg1 == null) {
				// arg1 = new TextView(context);
				arg1 = View.inflate(context, R.layout.listitem, null);// 把一个xml文件解析成一个view对象，最后一个参数标示
																		// 父类
			}
			TextView tv = (TextView) arg1.findViewById(R.id.tvName);
			TextView far = (TextView) arg1.findViewById(R.id.far);
			TextView work = (TextView) arg1.findViewById(R.id.tvWork);
			TextView score = (TextView) arg1.findViewById(R.id.tvScore);
			ImageView ivHeader = (ImageView) arg1.findViewById(R.id.ivHeader);
			ImageView ivSex = (ImageView) arg1.findViewById(R.id.ivSex);
			Person p = getItem(arg0);
			tv.setText(p.getName());
			far.setText(p.getFar());
			work.setText(p.getWork());
			score.setText(p.getScore());
			if(p.getSex()==0){
				ivSex.setBackgroundResource(R.drawable.girl);
			}
			
			String url = "http://"+ server_ip +":8080/images/" + p.getPic();
			Log.i("url",url);
			ivHeader.setTag(arg1 + url);
			new DownloadBitmapTask(listView, arg1 + url).execute(url);
			return arg1;
		}

	}

	static class DownloadBitmapTask extends AsyncTask<String, Void, Bitmap> {// AsyncTask是android里的一个异步加载类
		ListView listView;
		private String tag = null;

		public DownloadBitmapTask(ListView listView, String tag) {
			this.listView = listView;
			this.tag = tag;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			ImageView imageView = (ImageView) listView.findViewWithTag(tag);
			if (imageView != null) {
				imageView.setImageResource(R.drawable.ic_launcher);
			}
		}

		@Override
		protected Bitmap doInBackground(String... arg0) {// String...arg0
															// 表示可以接收不限个数的String
															// 嗯 程序里就把他当数组取就可以了
			// doinBackground这个方法是运行在后台线程里的
			String url = arg0[0];
			Bitmap bmp = returnBitMap(url);
			return bmp;
		}

		@Override
		protected void onPostExecute(Bitmap result) {// 这个方法是运行在主线程的，他的参数就是diInBackground方法的返回值
			super.onPostExecute(result);
			if (result == null) {
				Toast.makeText(listView.getContext(), "图片下载失败",
						Toast.LENGTH_SHORT).show();
				return;
			}
			ImageView imageView = (ImageView) listView.findViewWithTag(tag);
			if (imageView != null) {
				imageView.setImageBitmap(result);
			}

		}

	}

	// 根据url获取网络图片的数据流
	private static Bitmap returnBitMap(String url) { // 这个方法的问题，这个方法通过网络下载图片了，你又用在了adapter里，默认adapter里的方法也都是运行在主线程的，所以报错了
		URL myFileUrl = null;
		Bitmap bitmap = null;
		try {
			myFileUrl = new URL(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		try {
			HttpURLConnection conn = (HttpURLConnection) myFileUrl
					.openConnection();
			conn.setDoInput(true);
			conn.connect();
			InputStream is = conn.getInputStream();
			bitmap = BitmapFactory.decodeStream(is);
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bitmap;
	}

	// @Override
	// public boolean onCreateOptionsMenu(Menu menu) {//这个一般不用，直接注释掉，
	// // Inflate the menu; this adds items to the action bar if it is present.
	// getMenuInflater().inflate(R.menu.main, menu);
	// return true;
	// }

	@Override
	public void onClick(View arg0) {
		
			new Thread(new Runnable() {// 刚才那个错的意思是在主线程发起了网络请求，android4.0之后对网络访问做了限制，默认情况下网络访问不能放在主线程里，不然会报错
						@Override
						public void run() {
							try {
								strjson = JSONProvider
										.getJSONData("http://"+server_ip+":888/?method=getUserList");
								/*
								 * android手机就相当于一个小linux系统
								 * 写127.0.0.1，相当于请求到系统自己的地址了
								 * ，但系统这个地址又不对外提供服务，就refused了
								 */
								Log.i("strjson", strjson);
							} catch (IOException e) {
								e.printStackTrace();
							}
							Message msg = new Message();
							msg.what = TIME_UP2;
							handler.sendMessage(msg);
						}
					}).start();
	}

	private List<Person> getDataByJson(String json) {// 根据json字符串组装list
		List<Person> persons = new ArrayList<Person>();
		JSONArray jsonArray;
		try {
			//取code，
			jsonArray = new JSONArray(json);
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				long id = jsonObject.getLong("id");
				String name = jsonObject.getString("name");
				int age = jsonObject.getInt("age");
				int sex = jsonObject.getInt("sex");
				String pic = jsonObject.getString("pic");
				int work = jsonObject.getInt("work");
				String style = jsonObject.getString("style");
				//String score = jsonObject.getString("score");
				Person person = new Person(id, name, age,sex, pic,work,style);
				persons.add(person);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return persons;
	}
	


}
