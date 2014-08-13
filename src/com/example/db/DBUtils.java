package com.example.db;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBUtils extends SQLiteOpenHelper {

	public DBUtils(Context context) {//�����汾�͸�����������֣�һ��1.2.3.4.5������������
		super(context, "xiaoting", null, 1);//��һ�������ǳ���������Ķ��󣬵ڶ������������ݿ���������������һ�㲻���裬��һ���������������ĸ������ݿ�汾����1��ʼ��sqlite��һ���ļ������ݿ⣬������sql serverһ�����з�����
		// TODO �Զ����ɵĹ��캯�����
	}

	@Override
	public void onCreate(SQLiteDatabase arg0) {//��������ڵ�һ��ʹ�����ݿ��ʱ����ã�Ҳ���ǻ�û�����ݿ��ʱ��һ��������д�����Ĳ���
		// TODO �Զ����ɵķ������
		//д���򵥵�,jiu an ni zhiqian nage person d en
		//��������׼��sql���һ��
		//��Ҫע����ǣ�������ʹ��listview+sqlite��ʱ��androidĬ����Ҫ��һ���ֶ�    _id�����ԣ�һ�㴴�����ݿ��ʱ��Ͱ�������Ϊ   _id
		//sqlite �����������ݿ⣬�������ݿ���ָ����integer��ʵ������Ҳ���Բ����ַ������ͣ����ǲ�����ģ���Ϊ�����·��㣬�Ƽ���������������
		arg0.execSQL("create table person (_id integer primary key autoincrement, name text,age integer,sex integer,work integer,far text,pic text,style text);");
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int oldversion, int newversion) {//��������ڼ�⵽���ݿ���Ҫ������ ʱ����ã���������������ݿ�汾��1��һ���º�����������ˣ����ݿ�汾�ĳ�2�ˣ���ô�ڵ�һ��ʹ��ʱ��ͻᴥ���������
		// TODO �Զ����ɵķ������
		//����ʱһ�㳣����������������ʽ��һ�ּ򵥴ֱ�һ�㣬ֱ�Ӱ�ԭ��ɾ�������½��������ĺô��Ǽ򵥣���������֮ǰ�����ݾͶ�ʧ�ˣ������ڴ洢����Ϣ�����в���Ҫ��ʱ��
		//��һ�ַ�ʽ���������ÿ��Ҫ�����ĵط���Ȼ������ִ�У���Ϊ�㻹Ҫ���ǿͻ����ܻ�û�����ݿ�������2����ͷ������ݰ汾Ϊ3������ˣ��������ַ�ʽ�鷳�㣬���ô��ǲ���ʧ����
		//��һ�ַ�ʽ��
//		arg0.execSQL("drop table person");//���O�õڶ��N��ʽ���@������עጵ�������զ߀�Ƿ��w����
//		onCreate(arg0);
//		
		
		//�ڶ��ַ�ʽ�����㷢���İ汾�ܶ��� ��ʱ���㲻����Ϊ���а汾�ֲ������дif  else���������׿�Ҫ��  n*(n-1)/2  �������̫�鷳�ˣ����ԣ�һ��ÿ��������sql����������ѭ��ȥִ��
		for(int i = oldversion;i<newversion;i++){
			List<String> sqlList = getUpdateSqlList().get(i);
			for(String sql : sqlList){
				arg0.execSQL(sql);
			}
		}
	}
	private List<List<String>> getUpdateSqlList(){
		List<List<String>> list = new ArrayList<List<String>>();
		list.add(version1());
		list.add(version2());
		
		return list;
	}
	private List<String> version1(){//��д���������Ժ�����ô��������ϰ��
		List<String> list = new ArrayList<String>();
		list.add("create table test(_id integer primary key arutincrement);");
		return list;
	}
	private List<String> version2(){//sqlite ֧�ֵ��޸Ĳ������ޣ���֧����ӱ��ֶΣ���֧��ɾ��
		List<String> list = new ArrayList<String>();
		list.add("alter table test add name text");
		return list;
	}
}
