package com.ruiyu.taozhuma.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 产品详情Model
 * 
 * @author FU
 * 
 */

@SuppressWarnings("serial")
public class ProductDetailModel implements Serializable {
	public Integer id;
	public String name;// 产品名称
	public String description;// 产品详情
	public Integer store_id;// 企业id
	public String imageName;
	public String store_name;// 企业名称
	public String store_loca;// 企业位置
	public String store_logo;// 企业LOGO
	public String inventory;// 产品总数
	public String price;// 价格
	public String model;// 型号
	public String priceOld;// 原价
	public String oldPrice;// 价格
	public String distance;// 距离
	public String quantity;// 起订量
	public ArrayList<String> images;// 产品焦点图
	public String image;// 产品图
	public String itemNum;
	public String standard;
	public String packing;
	public String cartonSize;
	public String details;
	public String title;
	public Integer subTypeId;
	public Integer typeId;
	public String tradePrice;// 批发价
	public Integer status;// 是否同城（同城须显示绿色）1为同城2为官方3为其他地区
	public String sell_number;// 销量
	public int com;// 评论数量

	public ArrayList<Attribute> attribute;// attribute格式：{“title”:
											// “货号”,”value": "00001"
											// },value为空不返回
	private ArrayList<Attrs> attrs;//

	public class Attribute {
		public String price;// 商品价格
		public String image;// 商品图片
		public String name;// 商品名称
		public int product_id;// 商品ID

	}

	public Integer userid;

	public class Attrs {
		public String time;// 评价时间
		public String name;// 客户昵称
		public String comment;// 评价内容

	}

}
