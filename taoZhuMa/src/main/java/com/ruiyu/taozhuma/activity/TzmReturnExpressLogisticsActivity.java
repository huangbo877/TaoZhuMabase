/**
 * 
 */
package com.ruiyu.taozhuma.activity;

import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.ruiyu.taozhuma.R;
import com.ruiyu.taozhuma.api.ApiClient;
import com.ruiyu.taozhuma.api.ApiListener;
import com.ruiyu.taozhuma.api.TzmRefuseProductExpressApi;
import com.ruiyu.taozhuma.base.BaseApplication;
import com.ruiyu.taozhuma.model.BaseModel;
import com.ruiyu.taozhuma.model.TzmExpressModel;
import com.ruiyu.taozhuma.model.UserModel;
import com.ruiyu.taozhuma.utils.LogUtil;
import com.ruiyu.taozhuma.utils.StringUtils;
import com.ruiyu.taozhuma.utils.ToastUtils;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author 林尧 2015-11-7 用户退货时 快递 的 物流信息
 */
public class TzmReturnExpressLogisticsActivity extends Activity {
	private String TAG = "TzmReturnExpressLogisticsActivity";
	// 返回按钮
	@ViewInject(R.id.btn_head_left)
	private Button btn_head_left;
	// 退单的状态 ,具体的快递, 快递单号
	@ViewInject(R.id.tv_express_status_type)
	private TextView tv_express_status_type;
	@ViewInject(R.id.tv_information_from)
	private TextView tv_information_from;
	@ViewInject(R.id.tv_express_number)
	private TextView tv_express_number;
	// 快递单详情 时间轴
	@ViewInject(R.id.ll_order_detail)
	private LinearLayout ll_order_detail;
	View convertView[] = null;
	private LayoutInflater layoutInflater;

	private TzmRefuseProductExpressApi tzmExpressApi;
	private ApiClient client;
	private TzmExpressModel tzmExpressModel;
	private UserModel userInfo;
	private int orderId;
	private int j = 0;
	private String[] str = { "在途，即货物处于运输过程中", "揽件，货物已由快递公司揽收并且产生了第一条跟踪信息",
			"疑难，货物寄送过程出了问题", "签收，收件人已签收", "退签，即货物由于用户拒签、超区等原因退回，而且发件人已经签收",
			"派件，即快递正在进行同城派件", "退回，货物正处于退回发件人的途中" };

	// tzm_return_express_detail.xml 的各个对象
	private TextView tv_content, tv_time;
	private ImageView image1;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Log.i(TAG, TAG + ">>>>>>>>>>>>>>onCreate");
		setContentView(R.layout.tzm_return_express_logistics_activity);
		ViewUtils.inject(this);
		layoutInflater = LayoutInflater
				.from(TzmReturnExpressLogisticsActivity.this);
		userInfo = BaseApplication.getInstance().getLoginUser();
		orderId = getIntent().getIntExtra("orderId", 0);
		System.out.println(orderId+"............");
		tzmExpressModel = new TzmExpressModel();
		client = new ApiClient(this);
		loadData();
	}

	// 生成时间轴
	private void initView() {
		
		btn_head_left.setOnClickListener(clickListener);
		if (tzmExpressModel != null) {
			if(StringUtils.isNotEmpty(tzmExpressModel.state)){
				tv_express_status_type.setText(str[Integer.parseInt(tzmExpressModel.state)]);
			}
			else{
				tv_express_status_type.setText("订单失效");
			}
			tv_express_number.setText("运单编号 : "+tzmExpressModel.expressNumber);
			tv_information_from.setText("信息来源 : "+tzmExpressModel.expressType);
			
			
			if (tzmExpressModel.express.size() != 0) {
				for (int index = 0; index < tzmExpressModel.express.size(); index++) {
					convertView = new View[tzmExpressModel.express.size()];
					convertView[index] = (View) layoutInflater.inflate(
							R.layout.tzm_return_express_detail, null);
					j = index;
					convertView[index].setId(j);
					tv_content = (TextView) convertView[index]
							.findViewById(R.id.tv_content);
					tv_content
							.setText(tzmExpressModel.express.get(index).content);
					tv_time = (TextView) convertView[index]
							.findViewById(R.id.tv_time);
					tv_time.setText(tzmExpressModel.express.get(index).time);
					ll_order_detail.addView(convertView[index]);
					image1 = (ImageView) convertView[index]
							.findViewById(R.id.image1);
					if (index == 0) {
						image1.setImageResource(R.drawable.tzm_260);
					} else {
						image1.setImageResource(R.drawable.tzm_261);
					}
				}
			}
		}
	}
	View.OnClickListener clickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_head_left:
				onBackPressed();
				break;
			
			}
		}
	};
	private void loadData() {
		tzmExpressApi = new TzmRefuseProductExpressApi();
		tzmExpressApi.setOrderId(orderId);
		
		client.api(tzmExpressApi, new ApiListener() {

			@Override
			public void onStart() {
			}

			@Override
			public void onException(Exception e) {
				LogUtil.ErrorLog(e);
			}

			@Override
			public void onError(String error) {
				LogUtil.Log(error);
				ToastUtils.showShortToast(TzmReturnExpressLogisticsActivity.this, error);
			}

			@Override
			public void onComplete(String jsonStr) {
				try {
					Gson gson = new Gson();
					Type type = new TypeToken<BaseModel<TzmExpressModel>>() {
					}.getType();
					BaseModel<TzmExpressModel> base = gson.fromJson(jsonStr,
							type);
					if (base.success) {
						tzmExpressModel = base.result;
						initView();//初始化
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}, true);
	}
	
}
