package com.ruiyu.taozhuma.activity;

import java.lang.reflect.Type;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ruiyu.taozhuma.R;
import com.ruiyu.taozhuma.api.ApiClient;
import com.ruiyu.taozhuma.api.ApiListener;
import com.ruiyu.taozhuma.api.TzmExpressApi;
import com.ruiyu.taozhuma.base.BaseApplication;
import com.ruiyu.taozhuma.model.BaseModel;
import com.ruiyu.taozhuma.model.TzmExpressModel;
import com.ruiyu.taozhuma.model.UserModel;
import com.ruiyu.taozhuma.utils.LogUtil;
import com.ruiyu.taozhuma.utils.StringUtils;
import com.ruiyu.taozhuma.utils.ToastUtils;

import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 查看物流
 * @author yangfeng
 */
public class TzmExpressActivity extends Activity {

	private Button btn_head_left;
	private TextView txt_head_title,tv_express_number,tv_express_type,tv_status_type;
	private TextView tv_content,tv_time;
	private ImageView image1;
	private Button btn_head_right;
	private ImageView iv_productImage;
	private LinearLayout ll_order_detail;
	View convertView[] = null;
	private LayoutInflater layoutInflater;

	private TzmExpressApi tzmExpressApi;
	private ApiClient client;
	private TzmExpressModel tzmExpressModel;
	
	private UserModel userInfo;
	private int orderId;
	private int j=0;

	private String[] str = { "在途，即货物处于运输过程中", "揽件，货物已由快递公司揽收并且产生了第一条跟踪信息",
			"疑难，货物寄送过程出了问题", "签收，收件人已签收", "退签，即货物由于用户拒签、超区等原因退回，而且发件人已经签收",
			"派件，即快递正在进行同城派件","退回，货物正处于退回发件人的途中" };
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.express_activity);
		
		btn_head_left = (Button) findViewById(R.id.btn_head_left);
		btn_head_left.setOnClickListener(onClick);
		txt_head_title = (TextView) findViewById(R.id.txt_head_title);
		txt_head_title.setText("物流详情");
		tv_express_number = (TextView) findViewById(R.id.tv_express_number);
		tv_express_type = (TextView) findViewById(R.id.tv_express_type);
		ll_order_detail = (LinearLayout) findViewById(R.id.ll_order_detail);
		tv_status_type=(TextView) findViewById(R.id.tv_status_type);
		layoutInflater = LayoutInflater.from(TzmExpressActivity.this);
		
		userInfo = BaseApplication.getInstance().getLoginUser();
		orderId = getIntent().getIntExtra("orderId", 0);
		
		tzmExpressModel = new TzmExpressModel();
		client = new ApiClient(this);
		loadData();
		
	}

	private void initView() {
		if(tzmExpressModel != null){
			if(StringUtils.isNotEmpty(tzmExpressModel.state)){
				tv_status_type.setText(str[Integer.parseInt(tzmExpressModel.state)]);
			}
			else{
				tv_status_type.setText("订单失效");
			}
			tv_express_number.setText(tzmExpressModel.expressNumber);
			tv_express_type.setText(tzmExpressModel.expressType);
			
			
			if(tzmExpressModel.express.size()!=0){
				
				for(int index=0; index < tzmExpressModel.express.size(); index++){
					convertView = new View[tzmExpressModel.express.size()];
					convertView[index] = (View)layoutInflater.inflate(R.layout.tzm_express_detail, null);
					j = index;
					convertView[index].setId(j);
					tv_content = (TextView) convertView[index].findViewById(R.id.tv_content);
					tv_content.setText(tzmExpressModel.express.get(index).content);
					tv_time = (TextView) convertView[index].findViewById(R.id.tv_time);
					tv_time.setText(tzmExpressModel.express.get(index).time);
					ll_order_detail.addView(convertView[index]);
					image1= (ImageView) convertView[index].findViewById(R.id.image1);
					if(index==0){
						image1.setImageResource(R.drawable.tzm_260);
					}else{
						image1.setImageResource(R.drawable.tzm_261);
					}
				}
			}
			
		}
	
	}

	private void loadData() {
		tzmExpressApi = new TzmExpressApi();
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
				ToastUtils.showShortToast(TzmExpressActivity.this, error);
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

	OnClickListener onClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_head_left:
				onBackPressed();
				break;
			}
		}
	};

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

}
