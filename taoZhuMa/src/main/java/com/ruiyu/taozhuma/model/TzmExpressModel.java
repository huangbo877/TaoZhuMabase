package com.ruiyu.taozhuma.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.ruiyu.taozhuma.model.TzmMyorderModel.Carts;

/**
 * 查看物流Model
 * @author feng
 */

public class TzmExpressModel implements Serializable{
	public String expressNumber;//快递名称
	public String expressType;//快递单号
	public String state;//订单状态
	public List<Express> express;
	public class Express{
		public String content;//内容
		public String time;//时间
	}
}
