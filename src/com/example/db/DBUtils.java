package com.example.db;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBUtils extends SQLiteOpenHelper {

	public DBUtils(Context context) {//升级版本就改下面这个数字，一般1.2.3.4.5这样升级就行
		super(context, "xiaoting", null, 1);//第一个参数是程序的上下文对象，第二个参数是数据库名，第三个参数一般不用设，是一个工厂方法，第四个是数据库版本，葱1开始，sqlite是一个文件型数据库，不用像sql server一样还有服务器
		// TODO 自动生成的构造函数存根
	}

	@Override
	public void onCreate(SQLiteDatabase arg0) {//这个方法在第一次使用数据库的时候调用，也就是还没有数据库的时候，一般在这进行创建表的操作
		// TODO 自动生成的方法存根
		//写个简单的,jiu an ni zhiqian nage person d en
		//创建跟标准的sql语句一样
		//需要注意的是，由于你使用listview+sqlite的时候，android默认需要有一个字段    _id，所以，一般创建数据库的时候就把主键设为   _id
		//sqlite 是弱类型数据库，你在数据库里指明是integer，实际中你也可以插入字符串类型，它是不报错的，但为了树勇方便，推荐还是区分下类型
		arg0.execSQL("create table person (_id integer primary key autoincrement, name text,age integer,sex integer,work integer,far text,pic text,style text);");
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int oldversion, int newversion) {//这个方法在检测到数据库需要升级的 时候调用，比如现在你的数据库版本是1，一个月后，你程序升级了，数据库版本改成2了，那么在第一次使用时候就会触发这个方法
		// TODO 自动生成的方法存根
		//升级时一般常见的有两种升级方式，一种简单粗暴一点，直接把原表删掉，重新建，这样的好处是简单，坏处就是之前的数据就丢失了，适用于存储的信息不敏感不重要的时候
		//另一种方式，就是算出每次要升级的地方，然后依次执行，因为你还要考虑客户可能还没把数据库升级到2，你就发布数据版本为3的情况了，所以这种方式麻烦点，但好处是不丢失数据
		//第一种方式：
//		arg0.execSQL("drop table person");//假設用第二種方式，這個我先注釋掉。擦，咋還是繁體字了
//		onCreate(arg0);
//		
		
		//第二种方式，当你发布的版本很多了 的时候，你不可能为所有版本分布情况都写if  else，那样的亲狂要有  n*(n-1)/2  种情况，太麻烦了，所以，一般每次升级吧sql都存起来，循环去执行
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
	private List<String> version1(){//我写个杨力，以后你怎么管理，看你习惯
		List<String> list = new ArrayList<String>();
		list.add("create table test(_id integer primary key arutincrement);");
		return list;
	}
	private List<String> version2(){//sqlite 支持的修改操作有限，仅支持添加表字段，不支持删除
		List<String> list = new ArrayList<String>();
		list.add("alter table test add name text");
		return list;
	}
}
