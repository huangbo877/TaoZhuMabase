package com.ruiyu.taozhuma.model;

/**
 * 订单提交
 * 
 * @author
 * 
 */
public class OrderAddModel {
	public AliPay aplipay;
	public WeChat wxpay;
	public String balance;
	public Integer fullpay;//0-非钱包全额，1-钱包全额

	public class AliPay {
		public String body;// 支付描述
		public String subject;// 订单名称
		public String sign_type;// 签名算法
		public String notify_url;// 异步付款通知url
		public String out_trade_no;// 商户交易流水号，唯一
		public String return_url;//
		public String sign;// 签名
		public String _input_charset;// 字符集
		public String exter_invoke_ip;// ip
		public String total_fee;// 金额
		public String service;// 服务名
		public String partner;// 合作身份者ID
		public String anti_phishing_key;// 防钓鱼
		public String seller_email;// 销售者邮箱
		public String payment_type;// 付款方式
		public String show_url;//

	}

	public class WeChat {
		public String sign;// 签名
		public String timestamp;// 10位时间
		public String partnerid;// 商户id
		public String noncestr;// 随机数
		public String prepayid;// 预支付id
		public String appid;// appid

	}
}
