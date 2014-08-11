package com.example.model;

public class Addr {
	private long id;
	private String name;
	private String addr;
	private String tel;
	private String point;
	private String city;

	public Addr(long id, String name,String addr,String tel,String point,String city) {
		super();
		this.id = id;
		this.name = name;
		this.addr = addr;
		this.tel = point;
		this.city = city;
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

	public String getAddr() {
		return addr;
	}


	public void setAddr(String addr) {
		this.addr = addr;
	}
	
	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getPoint() {
		return point;
	}

	public void setPoint(String point) {
		this.point = point;
	}
	
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}
}
