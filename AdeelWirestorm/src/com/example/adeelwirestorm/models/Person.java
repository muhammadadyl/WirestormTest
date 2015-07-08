package com.example.adeelwirestorm.models;

public class Person {
	private String Name;
	private String Position;
	private String SmallPicUrl;
	private String LrgPicUrl;

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getPosition() {
		return Position;
	}

	public void setPosition(String position) {
		Position = position;
	}

	public String getSmallPicUrl() {
		return SmallPicUrl;
	}

	public void setSmallPicUrl(String smallPicUrl) {
		SmallPicUrl = smallPicUrl;
	}

	public String getLrgPicUrl() {
		return LrgPicUrl;
	}

	public void setLrgPicUrl(String lrgPicUrl) {
		LrgPicUrl = lrgPicUrl;
	}
}
