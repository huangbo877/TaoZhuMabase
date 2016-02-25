package com.ruiyu.taozhuma.even;


public class CartChildEven {
	private Integer type;
	private Integer tag;
	private Boolean check;

	public CartChildEven(Integer type, Integer tag, Boolean check) {
		this.type = type;
		this.tag = tag;
		this.check = check;
	}

	public Integer getType() {
		return type;
	}

	public Integer getTag() {
		return tag;
	}

	public Boolean getCheck() {
		return check;
	}

}
