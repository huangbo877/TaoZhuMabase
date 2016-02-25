package com.ruiyu.taozhuma.even;

import java.util.List;

import com.ruiyu.taozhuma.model.TzmMyCartModel;


public class CartFatherEven {
	private Integer fatherPosition;
	private List<TzmMyCartModel> list;

	public CartFatherEven(Integer fatherPosition, List<TzmMyCartModel> list) {
		this.fatherPosition = fatherPosition;
		this.list = list;
	}

	public Integer getFatherPosition() {
		return fatherPosition;
	}

	public List<TzmMyCartModel> getList() {
		return list;
	}
}
