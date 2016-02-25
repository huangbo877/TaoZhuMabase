package com.ruiyu.taozhuma.model;

import java.io.Serializable;
import java.util.List;


/**
 * 选中提交购物车Model
 * 
 * @author fu
 * 
 */

@SuppressWarnings("serial")
public class TzmOrderProductModel implements Serializable {
	
	public List<TzmOrderProduct1Model> result;
	public String isWallte;//0为普通商品或者 普通商品与钱包商品2  1为钱包商品
	


}
