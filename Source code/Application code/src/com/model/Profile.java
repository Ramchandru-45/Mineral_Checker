package com.model;


public class Profile {

    private String name;
    private int age;
    private String gender;
    private float height;
    private float weight;
	private float[] mineralList;
    private String selectedMineral;

    public Profile(String name, int age, String gender, float height, float weight, float[] mineralList) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.height = height;
        this.weight = weight;
        this.mineralList = mineralList;
        this.selectedMineral = null;
    }


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public float getWeight() {
		return weight;
	}

	public void setWeight(float weight) {
		this.weight = weight;
	}

	public float[] getMinerals(){
		return mineralList;
	}

    public String getSelectedMineral() {
        return selectedMineral;
    }

    public void setSelectedMineral(String selectedMineral) {
        this.selectedMineral = selectedMineral;
    }

    @Override
    public String toString() {
    	return name;
    }

}
