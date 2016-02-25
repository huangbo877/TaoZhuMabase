package com.ruiyu.taozhuma.utils;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.telephony.PhoneNumberUtils;
import android.telephony.SmsManager;

public class PhoneUtils {

	/**
	 * 直接发送短信
	 * 
	 * @param phone
	 *            手机号码
	 * @param messgae
	 *            短信内容
	 */
	public static void sendSMS(String phone, String messgae) {
		SmsManager manager = SmsManager.getDefault();
		ArrayList<String> list = manager.divideMessage(messgae); // 因为一条短信有字数限制，因此要将长短信拆分
		for (String text : list) {
			manager.sendTextMessage(phone, null, text, null, null);
		}
	}
	
	/**
	 * 调用系统短信界面发送短信
	 * @param context
	 * @param phone
	 * @param messgae
	 */
	public static void sendSMS(Context context, String phone,String messgae){
		if(PhoneNumberUtils.isGlobalPhoneNumber(phone)){ 
            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:"+phone));           
            intent.putExtra("sms_body", messgae);           
            context.startActivity(intent); 
        } 
	}

	/**
	 * 拨打电话
	 * @param context	
	 * @param phone	电话号码
	 */
	public static void callPhone(Context context, String phone) {
		if (!StringUtils.isEmailStr(phone)) {
			Intent phoneIntent = new Intent("android.intent.action.CALL",
					Uri.parse("tel:" + phone));
			// 启动
			context.startActivity(phoneIntent);
		}
	}
}
