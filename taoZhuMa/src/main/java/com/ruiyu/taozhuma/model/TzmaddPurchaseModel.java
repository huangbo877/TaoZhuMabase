package com.ruiyu.taozhuma.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 直接购买Model
 * 
 * @author fu
 * 
 */

public class TzmaddPurchaseModel implements Serializable {
	public Integer uid;// 会员id
	public Integer shopId;// 店铺id
	public String shopName;// 店铺名称
	public Product product;

	public class Product {
		public Integer productNumber;
		public Float price;
		public String productImage;
		public String productName;
		public Integer productId;
		public Float allPrice;
	}

}
