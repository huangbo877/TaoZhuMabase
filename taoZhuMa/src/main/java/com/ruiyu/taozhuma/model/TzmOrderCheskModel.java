package com.ruiyu.taozhuma.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 审核Model
 * 
 * @author fu
 * 
 */

public class TzmOrderCheskModel implements Serializable {
	public String refundPrice;// 退款金额
	public String createDate;// 申请时间
	public String type;// 退款类型
	public String status;// 退货状态
	public String refundType;// 退货原因
	public String refundReason;// 退款原因说明
	public String remarks;// 退款审核不通过说明

}
