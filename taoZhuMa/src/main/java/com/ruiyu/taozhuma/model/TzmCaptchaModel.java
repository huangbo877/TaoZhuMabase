package com.ruiyu.taozhuma.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 发送手机验证码Model
 * @author feng
 */

public class TzmCaptchaModel implements Serializable{
	public String phone;//手机号码
	public String captcha;//验证码
}
