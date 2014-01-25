package com.apesRise.hotPointer.core.C45.dataset;

public class Attribute {
	private String name;
	private String[] data;
	private String[] nominalValues;
	private boolean isDiscrete = false;
	
	public boolean isDiscrete() {
		return isDiscrete;
	}

	public Attribute(String name, String[] data) {
		this.name = name;
		this.data = data;
	}

	public Attribute(String name, String[] nominalValues, String[] data){
		this.name = name;
		this.data = data;
		this.nominalValues = nominalValues;
		this.isDiscrete = true;
	}

	public String[] getNominalValues(){
		return this.nominalValues;
	}

	public void setNominalValues(String[] nominalValues){
		this.nominalValues = nominalValues;
		if(nominalValues!=null){
			this.isDiscrete = true;
		}else{
			this.isDiscrete = false;
		}
		
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String[] getData() {
		return this.data;
	}

	public void setData(String[] data) {
		this.data = data;
	}

	public int getNominalValuesCount(){
		return nominalValues.length;
	}

	public String toString() {
		return name;
	}
}