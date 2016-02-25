//package com.ruiyu.taozhuma.service;
//
//import java.util.List;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import android.content.Context;
//import android.content.Intent;
//import android.text.TextUtils;
//import android.util.Log;
//
//import com.baidu.android.pushservice.PushMessageReceiver;
//import com.ruiyu.taozhuma.activity.StartActivity;
//import com.ruiyu.taozhuma.activity.TzmPushOrderListActivity;
//import com.ruiyu.taozhuma.utils.UserInfoUtils;
//
//public class BaiduPushReceiver extends PushMessageReceiver {
//
//	/**
//	 * 调用PushManager.startWork后，sdk将对push
//	 * server发起绑定请求，这个过程是异步的。绑定请求的结果通过onBind返回。 如果您需要用单播推送，需要把这里获取的channel
//	 * id和user id上传到应用server中，再调用server接口用channel id和user id给单个手机或者用户推送。
//	 * 
//	 * @param context
//	 *            BroadcastReceiver的执行Context
//	 * @param errorCode
//	 *            绑定接口返回值，0 - 成功
//	 * @param appid
//	 *            应用id。errorCode非0时为null
//	 * @param userId
//	 *            应用user id。errorCode非0时为null
//	 * @param channelId
//	 *            应用channel id。errorCode非0时为null
//	 * @param requestId
//	 *            向服务端发起的请求id。在追查问题时有用；
//	 * @return none
//	 */
//	@Override
//	public void onBind(Context context, int errorCode, String appid,
//			String userId, String channelId, String requestId) {
//		String responseString = "onBind errorCode=" + errorCode + " appid="
//				+ appid + " userId=" + userId + " channelId=" + channelId
//				+ " requestId=" + requestId;
//		UserInfoUtils.setChannelId(channelId);
//		Log.d(TAG, responseString);
//	}
//
//	@Override
//	public void onDelTags(Context arg0, int arg1, List<String> arg2,
//			List<String> arg3, String arg4) {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public void onListTags(Context arg0, int arg1, List<String> arg2,
//			String arg3) {
//		// TODO Auto-generated method stub
//
//	}
//
//	/**
//	 * 接收透传消息的函数。
//	 * 
//	 * @param context
//	 *            上下文
//	 * @param message
//	 *            推送的消息
//	 * @param customContentString
//	 *            自定义内容,为空或者json字符串
//	 */
//	@Override
//	public void onMessage(Context context, String message,
//			String customContentString) {
//		// TODO Auto-generated method stub
//
//	}
//
//	/**
//	 * 接收通知到达的函数。
//	 * 
//	 * @param context
//	 *            上下文
//	 * @param title
//	 *            推送的通知的标题
//	 * @param description
//	 *            推送的通知的描述
//	 * @param customContentString
//	 *            自定义内容，为空或者json字符串
//	 */
//	@Override
//	public void onNotificationArrived(Context context, String title,
//			String description, String customContentString) {
//		// TODO Auto-generated method stub
//
//	}
//
//	/**
//	 * 接收通知点击的函数。
//	 * 
//	 * @param context
//	 *            上下文
//	 * @param title
//	 *            推送的通知的标题
//	 * @param description
//	 *            推送的通知的描述
//	 * @param customContentString
//	 *            自定义内容，为空或者json字符串
//	 */
//	@Override
//	public void onNotificationClicked(Context context, String title,
//			String description, String customContentString) {
//		// 自定义内容获取方式，mykey和myvalue对应通知推送时自定义内容中设置的键和值
//		// if (!TextUtils.isEmpty(customContentString)) {
//		// JSONObject customJson = null;
//		// try {
//		// customJson = new JSONObject(customContentString);
//		// String myvalue = null;
//		// if (!customJson.isNull("mykey")) {
//		// myvalue = customJson.getString("mykey");
//		// }
//		// } catch (JSONException e) {
//		// // TODO Auto-generated catch block
//		// e.printStackTrace();
//		// }
//		// }
//
//		Intent i = new Intent(context, TzmPushOrderListActivity.class);
//		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//		context.startActivity(i);
//	}
//
//	@Override
//	public void onSetTags(Context arg0, int arg1, List<String> arg2,
//			List<String> arg3, String arg4) {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public void onUnbind(Context arg0, int arg1, String arg2) {
//		// TODO Auto-generated method stub
//
//	}
//
//}
