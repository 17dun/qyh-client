package com.example.model;

public class Person {// 创建对象封装数据
	private int id;
	private int age;
	private int sex;
	private String name;
	private String pic;
	private String far;
	private int work;
	private String style;
	//private String score;
	

	public Person(int id, String name, int age,int sex, String pic,int work,String style) {
		super();
		this.id = id;
		this.name = name;
		this.age = age;
		this.setSex(sex);
		this.pic = pic;
		this.work = work;
		this.style = style;
	}

	
	public String getScore() {
		return "影响力：200";
	}


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		if(name.length()>7){
			return name.substring(0,7);
		}else{
			return name;
		}
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
		workArray[0] = "无业";
		workArray[1] = "白领";
		workArray[2] = "学生";
		workArray[3] = "公务员";
		workArray[4] = "运动员";
		workArray[5] = "其它";
		return workArray[work];
	}

	public String getStyle() {
		return style;
	}
	
	
	public void setWork(int work) {
		this.work = work;
	}


	public int getSex() {
		return sex;
	}


	public void setSex(int sex) {
		this.sex = sex;
	}
}
