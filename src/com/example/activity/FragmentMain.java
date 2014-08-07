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

import com.example.provider.JSONProvider;

public class FragmentMain extends Fragment implements OnClickListener {
	private TextView tv;
	static String server_ip = "192.168.7.212";
	FirstAdapter adapter;
	ListView listView;
	TextView footView;
	String strjson = "";

	private final int TIME_UP = 1;
	private final int TIME_UP2 = 2;

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == TIME_UP) {
				// handler����ķ��������������̵߳�
				List<Person> data = getDataByJson(strjson);
				adapter = new FirstAdapter(getActivity(), data);// Fragment
																// ��û��getApplicationContext()
																// ���������һ����������getActivity()��������
				adapter.setListView(listView);
				listView.setAdapter(adapter);
				footView = new TextView(getActivity());
				footView.setText("���ظ���");
				footView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
				footView.setGravity(Gravity.CENTER);
				footView.setOnClickListener(FragmentMain.this);// ���������дthis����ָhandler��������Ҫָ��MainActivity��this
				listView.addFooterView(footView);
				listView.setOnItemClickListener(new OnItemClickListener() {// ΪlistView��ÿ��item��������¼�
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						Intent intent = new Intent(getActivity(),UserInfoActivity.class);// ʵ����һ����תҳ��     ����activity�Ļ����������ַ�ʽ  UserInfoActivity�������̫�������ˣ�һ����fragment�Ļ��ͱ����activity������ �ǰ����ָĸ�
						intent.putExtra("name", adapter.getItem(arg2).getName());// ����
						intent.putExtra("work", adapter.getItem(arg2).getWork());// ����
						intent.putExtra("far", adapter.getItem(arg2).getFar());// ����
						intent.putExtra("style", adapter.getItem(arg2).getStyle());// ����
						intent.putExtra("id", arg3);
						startActivity(intent);// ��ת

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
		tv.setText("������");
		new Thread(new Runnable() {// �ղ��Ǹ�������˼�������̷߳�������������android4.0֮�����������������ƣ�Ĭ�������������ʲ��ܷ������߳����Ȼ�ᱨ��
					@Override
					public void run() {
						try {
							strjson = JSONProvider.getJSONData("http://"+server_ip+":888/?method=getUserList");
							
							/*
							 * android�ֻ����൱��һ��Сlinuxϵͳ
							 * д127.0.0.1���൱������ϵͳ�Լ��ĵ�ַ�ˣ�
							 * ��ϵͳ�����ַ�ֲ������ṩ���񣬾�refused��
							 */
							
							Log.i("strjson", strjson);
						} catch (IOException e) {
							e.printStackTrace();
						}
						Message msg = new Message();
						msg.what = TIME_UP;
						handler.sendMessage(msg);
					}// �ұ���Ҳ���ӵĴ���
				}).start();
		return rootView;
	}

	static class FirstAdapter extends BaseAdapter {// һ������£�ʹ��listview��gridview,gallery�ȣ���ѧҪ�Զ���һ�����������������̳�BaseAdaper,��Щֱ�Ӵ����ݿ�ȡ����չʾ�ģ����ܻ��õ�SimpleCursorAdapter������һ���õ��٣��̳�BaseAdapter�ıȽ϶�
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
		public int getCount() {// ���������������listviewһ���ж�������
			return dataList.size();
		}

		@Override
		public Person getItem(int arg0) {// ������������ض�λ�õ�����

			return dataList.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {// ������������ض�λ�����ݵ�id��һ��ֱ�ӷ���λ�ü��ɣ�����Ǵ����ݿ�ȡ�����ݣ�������simpleCursorAdapter�ȵĻ������ﷵ�ص������ݿ������ID

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
		public View getView(int arg0, View arg1, ViewGroup arg2) {// ����������Ҫ���ܣ��������ʼ������
			if (arg1 == null) {
				// arg1 = new TextView(context);
				arg1 = View.inflate(context, R.layout.listitem, null);// ��һ��xml�ļ�������һ��view�������һ��������ʾ
																		// ����
			}
			TextView tv = (TextView) arg1.findViewById(R.id.tvName);
			TextView far = (TextView) arg1.findViewById(R.id.far);
			TextView work = (TextView) arg1.findViewById(R.id.work);
			TextView score = (TextView) arg1.findViewById(R.id.tvScore);
			ImageView ivHeader = (ImageView) arg1.findViewById(R.id.ivHeader);
			Person p = getItem(arg0);
			tv.setText(p.getName());
			far.setText(p.getFar());
			work.setText(p.getWork());
			score.setText(p.getScore());
			
			String url = "http://"+ server_ip +":8080/images/" + p.getPic();
			Log.i("url",url);
			ivHeader.setTag(arg1 + url);
			new DownloadBitmapTask(listView, arg1 + url).execute(url);
			return arg1;
		}

	}

	static class DownloadBitmapTask extends AsyncTask<String, Void, Bitmap> {// AsyncTask��android���һ���첽������
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
															// ��ʾ���Խ��ղ��޸�����String
															// �� ������Ͱ���������ȡ�Ϳ�����
			// doinBackground��������������ں�̨�߳����
			String url = arg0[0];
			Bitmap bmp = returnBitMap(url);
			return bmp;
		}

		@Override
		protected void onPostExecute(Bitmap result) {// ������������������̵߳ģ����Ĳ�������diInBackground�����ķ���ֵ
			super.onPostExecute(result);
			if (result == null) {
				Toast.makeText(listView.getContext(), "ͼƬ����ʧ��",
						Toast.LENGTH_SHORT).show();
				return;
			}
			ImageView imageView = (ImageView) listView.findViewWithTag(tag);
			if (imageView != null) {
				imageView.setImageBitmap(result);
			}

		}

	}

	// ����url��ȡ����ͼƬ��������
	private static Bitmap returnBitMap(String url) { // ������������⣬�������ͨ����������ͼƬ�ˣ�����������adapter�Ĭ��adapter��ķ���Ҳ�������������̵߳ģ����Ա�����
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
	// public boolean onCreateOptionsMenu(Menu menu) {//���һ�㲻�ã�ֱ��ע�͵���
	// // Inflate the menu; this adds items to the action bar if it is present.
	// getMenuInflater().inflate(R.menu.main, menu);
	// return true;
	// }

	@Override
	public void onClick(View arg0) {
		if (adapter.getCount() > 100) {// д����һ������100�����ݾͲ���ʾ���ظ�����
			if (listView.getFooterViewsCount() > 0) {
				listView.removeFooterView(footView);
			}
		} else {
			new Thread(new Runnable() {// �ղ��Ǹ�������˼�������̷߳�������������android4.0֮�����������������ƣ�Ĭ�������������ʲ��ܷ������߳����Ȼ�ᱨ��
						@Override
						public void run() {
							try {
								strjson = JSONProvider
										.getJSONData("http://"+server_ip+":888/?method=getUserList");
								/*
								 * android�ֻ����൱��һ��Сlinuxϵͳ
								 * д127.0.0.1���൱������ϵͳ�Լ��ĵ�ַ��
								 * ����ϵͳ�����ַ�ֲ������ṩ���񣬾�refused��
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
	}

	private List<Person> getDataByJson(String json) {// ����json�ַ�����װlist
		List<Person> persons = new ArrayList<Person>();
		JSONArray jsonArray;
		try {
			//ȡcode��
			jsonArray = new JSONArray(json);
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				long id = jsonObject.getLong("id");
				String name = jsonObject.getString("name");
				int age = jsonObject.getInt("age");
				String pic = jsonObject.getString("pic");
				int work = jsonObject.getInt("work");
				String style = jsonObject.getString("style");
				//String score = jsonObject.getString("score");
				Person person = new Person(id, name, age, pic,work,style);
				persons.add(person);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return persons;
	}
	
	static class Person {// ���������װ����
		private long id;
		private int age;
		private String name;
		private String pic;
		private String far;
		private int work;
		private String style;
		//private String score;
		

		public Person(long id, String name, int age, String pic,int work,String style) {
			super();
			this.id = id;
			this.name = name;
			this.age = age;
			this.pic = pic;
			this.work = work;
			this.style = style;
		}

		
		public String getScore() {
			return "Ӱ������200";
		}


		public long getId() {
			return id;
		}

		public void setId(long id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public long getAge() {
			return age;
		}

		public void setAge(int age) {
			this.age = age;
		}

		public String getPic() {
			return pic;
		}

		public void setPic(String pic) {
			this.pic = pic;
		}

		public String getFar() {
			return "100m";
		}

		public void setFar(String far) {
			this.far = far;
		}

		public String getWork() {
			String[]  workArray;
			workArray = new String[6];
			workArray[0] = "��ҵ";
			workArray[1] = "����";
			workArray[2] = "ѧ��";
			workArray[3] = "����Ա";
			workArray[4] = "�˶�Ա";
			workArray[5] = "����";
			return workArray[work];
		}

		public String getStyle() {
			return style;
		}
		
		
		public void setWork(int work) {
			this.work = work;
		}
	}

}