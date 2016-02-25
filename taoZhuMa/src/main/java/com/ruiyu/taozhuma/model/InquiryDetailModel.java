package com.ruiyu.taozhuma.model;

import java.util.ArrayList;

import org.w3c.dom.Text;

/**
 * 询价详情Model
 * @author Boo
 *
 */

public class InquiryDetailModel {
	public String id;//询价id
	public String contactor;//询价人
	public String telephone;//联系方式
	public String productName;//产品名称
	public String productItemNum;//产品型号
	public String productPrice;//产品价格
	public String remarks;//询价详情
	public String reply;//询价详情
	public String uid;//回复者id（供应商）
	public String buyerId;//访问者id（采购商）
	public String state;//询价的状态0=>未回复
	public String productImage;//产品图片
	public String enterpriseName;//厂家名称
	public String addTime;//询价时间
	public int isRead;//是否查看：0未查看1已查看
}
