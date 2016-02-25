package com.ruiyu.taozhuma.api;

import java.util.HashMap;
import java.util.Map;

import com.ruiyu.taozhuma.config.AppConfig;

/**
 * 
 * 产品描述图API
 * 
 * @author Fu
 * 
 */
public class TzmProductDetailCommentApi implements BaseApi {

	private Integer id;// 产品id
	private Integer score;// 好评
	private Integer uid;// 客户id
	private Integer pageNo;

	@Override
	public Map<String, String> getParamMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("id", id + "");
		map.put("uid", uid + "");
		map.put("pageNo", pageNo + "");
		map.put("score", score + "");
		return map;
	}

	@Override
	public String getUrl() {
		return AppConfig.TZM_PRODUCTDETAILCOMMENT_URL;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getscore() {
		return score;
	}

	public void setscore(Integer score) {
		this.score = score;
	}
	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}

	public Integer getPageNo() {
		return pageNo;
	}

	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}

}
