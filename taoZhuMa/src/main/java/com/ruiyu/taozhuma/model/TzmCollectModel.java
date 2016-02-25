package com.ruiyu.taozhuma.model;

import java.io.Serializable;

/**
 * 关注Model
 * 
 * @author LinJianhong
 * 
 */
public class TzmCollectModel implements Serializable {

	public int favId;// 收藏id
	public int favoriteId;// 收藏的产品id
	public String favName;// 产品名称
	public String favPrice;// 产品价格
	public String favImage;// 产品图片
	public String favItem;// 产品型号
	public Integer status;// 是否下架（0未开始 1进行中 2已经结束 
	/**
	 * 标识是否可以删除
	 */
	private boolean canRemove = true;

	public boolean isCanRemove() {
		return canRemove;
	}

	public void setCanRemove(boolean canRemove) {
		this.canRemove = canRemove;
	}

	/**
	 * 专场收藏 返回的数据多了 新的字段 activityId 
	 * activityId: 54, 
	 * favName: "淘竹马自营专场特卖",
	 * favImage: "http://b2c.taozhuma.com/upfiles/specialShow/ ",
	 * favoriteId:54,
	 * favId: 10,
	 * favItem: "", 
	 * status: 1, 
	 * favPrice: ""
	 */
    public Integer activityId;//专场活动id 
}
