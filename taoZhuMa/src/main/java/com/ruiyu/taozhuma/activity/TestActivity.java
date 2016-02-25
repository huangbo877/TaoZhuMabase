package com.ruiyu.taozhuma.activity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ruiyu.taozhuma.R;
import com.ruiyu.taozhuma.api.ApiClient;
import com.ruiyu.taozhuma.api.ApiListener;
import com.ruiyu.taozhuma.model.BaseModel;

import com.ruiyu.taozhuma.test.ProductImageAdapter;
import com.ruiyu.taozhuma.test.ProductImageApi;
import com.ruiyu.taozhuma.test.ProductImageModel;
import com.ruiyu.taozhuma.utils.ProgressDialogUtil;
import com.ruiyu.taozhuma.widget.ListViewForScrollView;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * 测试用
 * 
 * @author Fu
 * 
 */
public class TestActivity extends Activity {



	
	// 接口调用
	private ApiClient client;
	private ProductImageAdapter productImageAdapter;
	private ProductImageApi productImageApi;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.image_list_layout2);
		client = new ApiClient(this);
		productImageApi=new ProductImageApi();
		productImageApi.setUid(141);
		final ListViewForScrollView listView=(ListViewForScrollView) findViewById(R.id.lv_imglist);
		
		
		client.api(this.productImageApi, new ApiListener() {
			@Override
			public void onStart() {
				
			}

			@Override
			public void onComplete(String jsonStr) {
				ProgressDialogUtil.closeProgressDialog();
				Gson gson = new Gson();
				Type type = new TypeToken<BaseModel<ArrayList<ProductImageModel>>>() {
				}.getType();
				BaseModel<ArrayList<ProductImageModel>> base = gson
						.fromJson(jsonStr, type);

				if (base.result != null && base.result.size() > 0) {
				        productImageAdapter=new ProductImageAdapter(TestActivity.this, base.result);
				        listView.setAdapter(productImageAdapter);
				        ProductImageAdapter  listAdapter = (ProductImageAdapter) listView.getAdapter();
				        if (listAdapter == null) {
				            return;
				        }
				 
				        int totalHeight = 0;
				        for (int i = 0; i < listAdapter.getCount(); i++) {
				            View listItem = listAdapter.getView(i, null, listView);
				            listItem.measure(0, 0);
				            totalHeight += listItem.getMeasuredHeight();
				        }
				 
				        ViewGroup.LayoutParams params = listView.getLayoutParams();
				        params.height = totalHeight
				                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
				        System.out.println(params.height+">>>>>>>>>>>>>>>>>>>>>");
				        listView.setLayoutParams(params);
				} else {
					
				}

			}

			@Override
			public void onError(String error) {
		
			}

			@Override
			public void onException(Exception e) {
			
			}
		}, true);

		
		
		
		
		
		// ViewUtils.inject(this);
		// webView.loadUrl("http://testb2c.taozhuma.com/goSceneProductWeb.do?scenePojo.id=1");
		// WebSettings setting = webView.getSettings();
		// setting.setJavaScriptEnabled(true);// 支持js
		// setting.setDefaultTextEncodingName("utf-8");// 设置字符编码 增加对中文的支持
		// webView.setScrollBarStyle(0);// 滚动条风格，为0指滚动条不占用空间，直接覆盖在网页上
		// webView.setWebViewClient(new WebViewClient() {
		// public boolean shouldOverrideUrlLoading(WebView view, String url) {
		// //拦截超链接
		// ToastUtils.showShortToast(TestActivity.this, url);
		// return true;
		//
		// };
		// });

//
//		mcoySnapPageLayout = (McoySnapPageLayout) findViewById(R.id.flipLayout);
//
//		topPage = new McoyProductDetailInfoPage(TestActivity.this,
//				getLayoutInflater().inflate(R.layout.mcoy_produt_detail_layout,
//						null));
//		bottomPage = new McoyProductContentPage(TestActivity.this,
//				getLayoutInflater().inflate(R.layout.mcoy_product_content_page,
//						null));
//
//		mcoySnapPageLayout.setSnapPages(topPage, bottomPage);

	}

	// private void loadData() {
	// apiClient = new ApiClient(TestActivity.this);
	// productSkuLinkApi = new ProductSkuLinkApi();
	// productSkuLinkApi.setPid(5);
	// apiClient.api(productSkuLinkApi, new ApiListener() {
	//
	// @Override
	// public void onStart() {
	// }
	//
	// @Override
	// public void onException(Exception e) {
	// LogUtil.ErrorLog(e);
	// }
	//
	// @Override
	// public void onError(String error) {
	// LogUtil.Log(error);
	// }
	//
	// @Override
	// public void onComplete(String jsonStr) {
	// try {
	// Gson gson = new Gson();
	// Type type = new TypeToken<BaseModel<List<ProductSkuLinkModel>>>() {
	// }.getType();
	// BaseModel<List<ProductSkuLinkModel>> base = gson.fromJson(
	// jsonStr, type);
	// if (base.success) {
	// productSkuLinkModels = base.result;
	// dbUtils.saveOrUpdateAll(productSkuLinkModels);
	// loadData2();
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	//
	// }
	// }, true);
	// }

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

}
