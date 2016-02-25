package com.ruiyu.taozhuma.api;

import java.util.HashMap;
import java.util.Map;

import android.os.IInterface;

import com.ruiyu.taozhuma.config.AppConfig;

/**
 * 
 * 产品分类API
 * @author Fu
 *
 */
public class TzmProductTypeApi implements BaseApi {

	
	@Override
	public Map<String, String> getParamMap() {
		Map<String, String> map = new HashMap<String, String>();
		return map;		
	}

	@Override
	public String getUrl() {
		return AppConfig.TZM_PRODUCTTYPE_URL;
	}


}
