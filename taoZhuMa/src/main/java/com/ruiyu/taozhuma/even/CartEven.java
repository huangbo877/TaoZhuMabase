package com.ruiyu.taozhuma.even;

import java.util.List;

import com.ruiyu.taozhuma.model.TzmMyCartModel;


public class CartEven {
	private Integer type;
	private List<TzmMyCartModel> list;

	public CartEven(Integer type, List<TzmMyCartModel> list) {
		this.type = type;
		this.list = list;
	}

	public Integer getType() {
		return type;
	}

	public List<TzmMyCartModel> getList() {
		return list;
	}

}
