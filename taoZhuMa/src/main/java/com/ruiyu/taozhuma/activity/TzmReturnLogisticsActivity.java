package com.ruiyu.taozhuma.activity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ruiyu.taozhuma.R;
import com.ruiyu.taozhuma.api.ApiClient;
import com.ruiyu.taozhuma.api.ApiListener;
import com.ruiyu.taozhuma.api.LogisticsApi;
import com.ruiyu.taozhuma.api.SubmitLogisticsApi;
import com.ruiyu.taozhuma.api.TzmRefunDoneDetailsApi;
import com.ruiyu.taozhuma.base.BaseApplication;
import com.ruiyu.taozhuma.base.xUtilsImageLoader;
import com.ruiyu.taozhuma.model.BaseModel;
import com.ruiyu.taozhuma.model.OrderDetailModel;
import com.ruiyu.taozhuma.model.OrderRefunModel;
import com.ruiyu.taozhuma.model.OrderRefunModel;
import com.ruiyu.taozhuma.model.RefsellerModel;
import com.ruiyu.taozhuma.api.RefSellerInfoByOrderIdApi;
import com.ruiyu.taozhuma.model.TzmOrderDetailModel;
import com.ruiyu.taozhuma.model.UserModel;
import com.ruiyu.taozhuma.model.logisticsModel;
import com.ruiyu.taozhuma.utils.LogUtil;
import com.ruiyu.taozhuma.utils.ProgressDialogUtil;
import com.ruiyu.taozhuma.utils.StringUtils;
import com.ruiyu.taozhuma.utils.ToastUtils;

/**
 * 
 * 填写退货物流信息
 * 
 * @author huangbo
 * 
 *         上午9:42:32
 */
public class TzmReturnLogisticsActivity extends Activity {
	private Button btn_head_left;
	private TextView txt_head_title;
	private LinearLayout ll_logistics_detail;
	private TextView tv_chose_express_company, tv_product_name, tv_return_name,
			tv_retrun_address, tv_phoneNumber, tv_product_limint_buy,
			tv_product_color, tv_product_size, tv_show_product_price,
			tv_show_product_spec, tv_show_product_preferential,
			tv_show_product_return_money;
	private Spinner sp_logisticsName;
	private EditText et_input_express_number;
	private Button bt_register;
	ImageView iv_show_product_image;

	private String[] logisticsNames;
	private ArrayAdapter<String> adapter;

	private TzmOrderDetailModel.Products model;
	// private Carts carts;
	// private List<product> products;
	private UserModel userInfo;
	private LayoutInflater layoutInflater;
	RefsellerModel RefsellerModel;
	private LogisticsApi logisticsApi;
	private SubmitLogisticsApi submitLogisticsApi;
	TzmRefunDoneDetailsApi tzmRefunDoneDetailsApi;
	private ApiClient apiClient1, apiClient2, apiClient3;
	private List<logisticsModel> logisticsModels;
	OrderRefunModel orderRefunModel;
	private RefSellerInfoByOrderIdApi RefSellerInfoByOrderIdApi;
	private int position;
	int oid;// 订单id
	String typez;
	xUtilsImageLoader imageLoader;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tzm_return_goods_input_logistics_imformation_activity);
		imageLoader = new xUtilsImageLoader(this);
		oid = getIntent().getIntExtra("oid", 0);
		userInfo = BaseApplication.getInstance().getLoginUser();
		apiClient3 = new ApiClient(this);
		// products= model.product;
		initView();
		loadData();
		loadData1();
		RefSeller();
	}

	// 查询物流公司
	private void loadData() {
		apiClient1 = new ApiClient(this);
		logisticsApi = new LogisticsApi();
		apiClient1.api(logisticsApi, new ApiListener() {

			@Override
			public void onStart() {
			}

			@Override
			public void onError(String error) {
				ToastUtils.showShortToast(TzmReturnLogisticsActivity.this, error);
			}

			@Override
			public void onException(Exception e) {
				// ProgressDialogUtil.closeProgressDialog();
				LogUtil.ErrorLog(e);
			}

			@Override
			public void onComplete(String jsonStr) {
				if (StringUtils.isNotBlank(jsonStr)) {
					Gson gson = new Gson();
					Type type = new TypeToken<BaseModel<ArrayList<logisticsModel>>>() {
					}.getType();
					BaseModel<ArrayList<logisticsModel>> base = gson.fromJson(
							jsonStr, type);
					if (base.result != null && base.result.size() > 0) {
						logisticsModels.addAll(base.result);
						logisticsNames = new String[logisticsModels.size()];
						for (int i = 0; i < logisticsModels.size(); i++) {
							logisticsNames[i] = logisticsModels.get(i).name;
						}
						sp_logisticsName = (Spinner) findViewById(R.id.sp_logisticsName);
						tv_chose_express_company
								.setOnClickListener(clickListener);
						adapter = new ArrayAdapter<String>(
								TzmReturnLogisticsActivity.this,
								android.R.layout.simple_spinner_item,
								logisticsNames);
						adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
						sp_logisticsName.setAdapter(adapter);
						sp_logisticsName
								.setOnItemSelectedListener(new SpinnerSelectedListener());
					} else {
						ToastUtils
								.showShortToast(
										TzmReturnLogisticsActivity.this,
										base.error_msg);
					}
				}
				// ProgressDialogUtil.closeProgressDialog();

			}
		}, true);

	}

	// 退货地址
	private void RefSeller() {
		apiClient1 = new ApiClient(this);
		RefSellerInfoByOrderIdApi = new com.ruiyu.taozhuma.api.RefSellerInfoByOrderIdApi();
		RefSellerInfoByOrderIdApi.setOid(oid);
		apiClient1.api(RefSellerInfoByOrderIdApi, new ApiListener() {

			@Override
			public void onStart() {
				// ProgressDialogUtil.openProgressDialog(
				// TzmReturnLogisticsActivity.this, "", "正在提交...");
			}

			@Override
			public void onError(String error) {
				LogUtil.Log(error);
				ToastUtils.showShortToast(TzmReturnLogisticsActivity.this, error);
			}

			@Override
			public void onException(Exception e) {
				// ProgressDialogUtil.closeProgressDialog();
				LogUtil.ErrorLog(e);
			}

			@Override
			public void onComplete(String jsonStr) {
				// ProgressDialogUtil.closeProgressDialog();
				if (StringUtils.isNotBlank(jsonStr)) {
					Gson gson = new Gson();
					Type type = new TypeToken<BaseModel<RefsellerModel>>() {
					}.getType();
					BaseModel<RefsellerModel> base = gson.fromJson(jsonStr,
							type);
					if (base.result != null) {
						RefsellerModel = base.result;
						tv_return_name.setText(RefsellerModel.name);
						tv_phoneNumber.setText(RefsellerModel.phone);
						tv_retrun_address.setText(RefsellerModel.address);
					} else {
						ToastUtils
								.showShortToast(
										TzmReturnLogisticsActivity.this,
										base.error_msg);
					}
				}
			}
		}, true);

	}

	// 查询退货订单详情
	private void loadData1() {
		tzmRefunDoneDetailsApi = new TzmRefunDoneDetailsApi();
		tzmRefunDoneDetailsApi.setOid(oid);
		apiClient3.api(tzmRefunDoneDetailsApi, new ApiListener() {

			@Override
			public void onStart() {
			}

			@Override
			public void onError(String error) {
				ToastUtils.showShortToast(TzmReturnLogisticsActivity.this, error);
			}

			@Override
			public void onException(Exception e) {
				// ProgressDialogUtil.closeProgressDialog();
				LogUtil.ErrorLog(e);
			}

			@Override
			public void onComplete(String jsonStr) {
				// ProgressDialogUtil.closeProgressDialog();
				if (StringUtils.isNotBlank(jsonStr)) {
					LogUtil.Log("===============");
					LogUtil.Log(jsonStr);
					LogUtil.Log("===============");
					try {
						Gson gson = new Gson();
						Type type = new TypeToken<BaseModel<OrderRefunModel>>() {
						}.getType();
						BaseModel<OrderRefunModel> base = gson.fromJson(
								jsonStr, type);

						
							orderRefunModel = base.result;
							ordersDetail();
							
						
					} catch (Exception exception) {
						exception.printStackTrace();
					}
					System.out.println(orderRefunModel);
				}
			}
		}, true);
	}

	private void initView() {
		btn_head_left = (Button) findViewById(R.id.btn_head_left);
		btn_head_left.setOnClickListener(clickListener);
		txt_head_title = (TextView) findViewById(R.id.txt_head_title);
		tv_return_name = (TextView) findViewById(R.id.tv_return_name);
		tv_phoneNumber = (TextView) findViewById(R.id.tv_phoneNumber);
		tv_retrun_address = (TextView) findViewById(R.id.tv_retrun_address);
		txt_head_title.setText("提交物流");
		ll_logistics_detail = (LinearLayout) findViewById(R.id.ll_logistics_detail);
		layoutInflater = LayoutInflater.from(TzmReturnLogisticsActivity.this);
		et_input_express_number = (EditText) findViewById(R.id.et_input_express_number);
		bt_register = (Button) findViewById(R.id.bt_register);
		bt_register.setText("提交申请");
		bt_register.setOnClickListener(clickListener);
		tv_chose_express_company = (TextView) findViewById(R.id.tv_chose_express_company);
		sp_logisticsName = (Spinner) findViewById(R.id.sp_logisticsName);
		logisticsModels = new ArrayList<logisticsModel>();
		RefsellerModel = new RefsellerModel();
	}

	private void ordersDetail() {
		iv_show_product_image = (ImageView) findViewById(R.id.iv_show_product_image);// 商品图片
		tv_product_name = (TextView) findViewById(R.id.tv_product_name);// 商品名字
		tv_product_limint_buy = (TextView) findViewById(R.id.tv_product_limint_buy);// 商品类型
		tv_product_color = (TextView) findViewById(R.id.tv_product_color);// 颜色
		tv_product_size = (TextView) findViewById(R.id.tv_product_size);// 大小
		tv_show_product_price = (TextView) findViewById(R.id.tv_show_product_price);// 价格
		tv_show_product_spec = (TextView) findViewById(R.id.tv_show_product_spec);// 件数
		tv_show_product_preferential = (TextView) findViewById(R.id.tv_show_product_preferential);
		tv_show_product_return_money = (TextView) findViewById(R.id.tv_show_product_return_money);

		imageLoader
				.display(iv_show_product_image, orderRefunModel.productImage);
		tv_product_name.setText(orderRefunModel.productName);
		switch (orderRefunModel.type) {
		case 0:
			typez = "普通商品";
			break;
		case 1:
			typez = "限量购";
			break;
		case 2:
			typez = "专题商品";
			break;
		case 3:
			typez = "活动商品";
			break;
		}
		if (StringUtils.isNotEmpty(orderRefunModel.skuValue)) {
			String[] temp = null;
			temp = orderRefunModel.skuValue.split(";");
			tv_product_color.setText(temp[0]);
			tv_product_size.setText(temp[1]);

		} else {
			tv_product_color.setText("");
			tv_product_size.setText("");
		}
		tv_product_limint_buy.setText(typez);
		tv_show_product_spec.setText(orderRefunModel.refundNumber + "件");
		tv_show_product_price.setText("¥  " + orderRefunModel.price);
		tv_show_product_preferential.setText("优惠 ¥  "
				+ orderRefunModel.discountPrice);
		tv_show_product_return_money.setText("实际退款 ¥  "
				+ orderRefunModel.refundPrice);

	}

	View.OnClickListener clickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_head_left:
				onBackPressed();
				break;
			case R.id.tv_chose_express_company:
				sp_logisticsName.performClick();
				sp_logisticsName.setVisibility(View.VISIBLE);
				tv_chose_express_company.setVisibility(View.GONE);
				break;
			case R.id.bt_register:
				submitData();
				break;
			}
		}
	};

	protected void submitData() {
		apiClient2 = new ApiClient(this);
		submitLogisticsApi = new SubmitLogisticsApi();
		if (logisticsModels.size() == 0 || logisticsModels == null) {
			loadData();
			ToastUtils.showShortToast(TzmReturnLogisticsActivity.this, "请选择物流公司");
			return;
		}
		if (tv_chose_express_company.getVisibility() == View.VISIBLE) {
			ToastUtils.showShortToast(TzmReturnLogisticsActivity.this, "请选择物流公司");
			return;
		}
		if (StringUtils.isEmpty(et_input_express_number.getText().toString())) {
			ToastUtils.showShortToast(TzmReturnLogisticsActivity.this, "请输入物流的单号");
			return;
		}
		submitLogisticsApi.setUid(userInfo.uid);
		submitLogisticsApi.setOid(oid);
		submitLogisticsApi.setLogisticsNum(et_input_express_number.getText()
				.toString());
		submitLogisticsApi
				.setLogisticsName(logisticsModels.get(position).nameEn);
		apiClient2.api(submitLogisticsApi, new ApiListener() {
			@Override
			public void onStart() {
				ProgressDialogUtil.openProgressDialog(
						TzmReturnLogisticsActivity.this, "", "正在提交...");
			}

			@Override
			public void onError(String error) {
				ProgressDialogUtil.closeProgressDialog();
				ToastUtils.showShortToast(TzmReturnLogisticsActivity.this, error);
			}

			@Override
			public void onException(Exception e) {
				ProgressDialogUtil.closeProgressDialog();
				LogUtil.ErrorLog(e);
			}

			@Override
			public void onComplete(String jsonStr) {
				ProgressDialogUtil.closeProgressDialog();
				if (StringUtils.isNotBlank(jsonStr)) {
					try {
						JSONObject json = new JSONObject(jsonStr);
						ToastUtils.showShortToast(TzmReturnLogisticsActivity.this,
								json.getString("error_msg"));
						if (json.getBoolean("success")) {
							onBackPressed();
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}, true);

	}

	class SpinnerSelectedListener implements OnItemSelectedListener {

		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			position = arg2;
		}

		public void onNothingSelected(AdapterView<?> arg0) {
		}
	}
}
