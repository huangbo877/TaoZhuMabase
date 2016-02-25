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
public class TzmOrderProduct1Model implements Serializable {
	public Integer uid;// 会员id
	public Integer shopId;// 店铺id
	public String shopName;// 店铺名称
	public ProductModel product;
	public Integer allCount;//Product总数量
	public Float sumPrice;// 总价
	public String shopLogo;// 店铺logo
	public Float espressPrice;//运费
	public List<ProductModel> products;
	//public String isWallet;//0为普通商品或者 普通商品与钱包商品2  1为钱包商品
	//本地用
	public String content;//留言


}
