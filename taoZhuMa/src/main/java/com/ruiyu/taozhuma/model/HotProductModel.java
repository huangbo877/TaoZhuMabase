package com.ruiyu.taozhuma.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 热卖商品Model
 * @author FU
 * 
 */
public class HotProductModel implements Serializable{
		public Integer product_id;//产品ID
		public Integer id;//产品ID
		public Integer number;//活动数量
		public String pro_price;//特价
		public String price;//价格
		public String image;//产品图片
		public String name;//产品名称
		public String pro_price_old;//原价
		public Integer subTypeId;//大分类（1为童车，2为奶嘴，3为童装，4为玩具）
		public Integer typeId;//小分类
		public String priceOld;//原价
		
}
