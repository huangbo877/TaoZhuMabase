package com.ruiyu.taozhuma.even;

import java.util.List;

import com.ruiyu.taozhuma.model.TzmMyCartModel;

/**
 * 购物车v3 事件传递
 * 
 * @author Fu
 * 
 */
public class ShoppingCartEven {
	private Integer type;
	private List<TzmMyCartModel> list;

	public int types;// 1单选2多选3增减
	public int cartType;// 所属店铺标志

	public ShoppingCartEven(Integer type, List<TzmMyCartModel> list) {
		this.type = type;
		this.list = list;
	}

	public ShoppingCartEven(int cartType) {
		this.cartType = cartType;
	}

	public Integer getType() {
		return type;
	}

	public List<TzmMyCartModel> getList() {
		return list;
	}

}
