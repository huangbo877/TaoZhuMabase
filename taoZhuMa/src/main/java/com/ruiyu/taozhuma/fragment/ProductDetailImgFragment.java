package com.ruiyu.taozhuma.fragment;

import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.ruiyu.taozhuma.R;
import com.ruiyu.taozhuma.activity.ProductDetailActivity;
import com.ruiyu.taozhuma.activity.ProductDetailActivity;
import com.ruiyu.taozhuma.adapter.ProductDetailImgAdapter;
import com.ruiyu.taozhuma.api.ApiClient;
import com.ruiyu.taozhuma.api.ApiListener;
import com.ruiyu.taozhuma.api.ProductDetailAppApi;
import com.ruiyu.taozhuma.base.xUtilsImageLoader;
import com.ruiyu.taozhuma.model.BaseModel;
import com.ruiyu.taozhuma.model.ProductDetailImageModel;
import com.ruiyu.taozhuma.model.TzmProductDetailModel;
import com.ruiyu.taozhuma.utils.LogUtil;
import com.ruiyu.taozhuma.widget.CustListView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;

public class ProductDetailImgFragment extends Fragment implements
		OnClickListener {

	@ViewInject(R.id.lv_img)
	private CustListView lv_img;

	private ProductDetailAppApi api;
	private List<ProductDetailImageModel> imageModels;
	private ApiClient apiClient;
	private ProductDetailImgAdapter adapter;

	int width;// 屏幕宽度

	private ProductDetailActivity activity;

	private TzmProductDetailModel tzmProductDetailModel;
	// 猜你喜欢
	private ImageView im_like01, im_like02, im_like03, im_like04;
	private TextView tv_like_name01, tv_like_name02, tv_like_name03,
			tv_like_name04;
	private TextView tv_like_price01, tv_like_price02, tv_like_price03,
			tv_like_price04;
	private RelativeLayout rl_like01, rl_like02, rl_like03, rl_like04;
	xUtilsImageLoader imageLoader;

	@ViewInject(R.id.pb_pimg)
	private ProgressBar pb_pimg;

	@ViewInject(R.id.iv_gotop)
	private ImageView iv_gotop;
	private float y = 0;
	private boolean isTop = true;

	@SuppressLint({ "InflateParams", "ClickableViewAccessibility" })
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View v = inflater.inflate(R.layout.product_detail_img_fragment, null);
		ViewUtils.inject(this, v);
		DisplayMetrics metrics = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay()
				.getMetrics(metrics);
		width = metrics.widthPixels;
		activity = (ProductDetailActivity) getActivity();
		lv_img.setOnTouchListener(new OnTouchListener() {

			@SuppressLint("ClickableViewAccessibility")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					y = event.getY();
					break;
				case MotionEvent.ACTION_MOVE:

					break;
				case MotionEvent.ACTION_UP:
					if (event.getY() - y > 100 && !isTop) {
						// ToastUtils.showToast(TzmProductListActivity.this,
						// "上滑");
						showView();
					} else if (y - event.getY() > 100) {
						// ToastUtils.showToast(TzmProductListActivity.this,
						// "下滑");
						hideView();
					}
					break;

				}
				return false;
			}
		});
		lv_img.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				switch (scrollState) {
				case OnScrollListener.SCROLL_STATE_IDLE:
					if (view.getFirstVisiblePosition() == 0) {
						isTop = true;
						hideView();
					} else {
						isTop = false;
					}
					break;

				}

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {

			}
		});
		iv_gotop.setOnClickListener(this);
		return v;
	}

	private void showView() {
		if (iv_gotop.getVisibility() == View.GONE) {
			iv_gotop.setVisibility(View.VISIBLE);
		}
	}

	private void hideView() {
		if (iv_gotop.getVisibility() == View.VISIBLE) {
			iv_gotop.setVisibility(View.GONE);
		}
	}

	public void loadData(Integer id) {
		apiClient = new ApiClient(getActivity());
		api = new ProductDetailAppApi();
		api.setId(id);
		apiClient.api(this.api, new ApiListener() {
			@Override
			public void onStart() {
				// ProgressDialogUtil.openProgressDialog(
				// NewFragment.this.getActivity(), "", "正在加载...");
			}

			@Override
			public void onComplete(String jsonStr) {
				// ProgressDialogUtil.closeProgressDialog();
				try {

					Gson gson = new Gson();
					Type type = new TypeToken<BaseModel<List<ProductDetailImageModel>>>() {
					}.getType();
					BaseModel<List<ProductDetailImageModel>> base = gson
							.fromJson(jsonStr, type);

					if (base.result != null && base.result.size() > 0) {
						imageModels = base.result;
						adapter = new ProductDetailImgAdapter(getActivity(),
								imageModels, width);
						lv_img.setAdapter(adapter);
						setRecommendView();
						pb_pimg.setVisibility(View.GONE);
					} else {
						LogUtil.Log(base.error_msg);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

			@Override
			public void onError(String error) {
				// ToastUtils.showShortToast(TzmProductDetailActivity.this,
				// R.string.msg_list_null);
				// ProgressDialogUtil.closeProgressDialog();
			}

			@Override
			public void onException(Exception e) {
				// ProgressDialogUtil.closeProgressDialog();
				LogUtil.ErrorLog(e);
			}
		}, true);
	}

	@SuppressLint("InflateParams")
	private void setRecommendView() {
		tzmProductDetailModel = activity.getDetailModel();
		// 猜你喜欢,固定四个
		if (tzmProductDetailModel != null
				&& tzmProductDetailModel.attribute != null) {
			View view = LayoutInflater.from(getActivity()).inflate(
					R.layout.guess_favorite_list_layout, null);
			imageLoader = new xUtilsImageLoader(getActivity());
			im_like01 = (ImageView) view.findViewById(R.id.im_like01);
			im_like02 = (ImageView) view.findViewById(R.id.im_like02);
			im_like03 = (ImageView) view.findViewById(R.id.im_like03);
			im_like04 = (ImageView) view.findViewById(R.id.im_like04);
			tv_like_name01 = (TextView) view.findViewById(R.id.tv_like_name01);
			tv_like_name02 = (TextView) view.findViewById(R.id.tv_like_name02);
			tv_like_name03 = (TextView) view.findViewById(R.id.tv_like_name03);
			tv_like_name04 = (TextView) view.findViewById(R.id.tv_like_name04);
			tv_like_price01 = (TextView) view
					.findViewById(R.id.tv_like_price01);
			tv_like_price02 = (TextView) view
					.findViewById(R.id.tv_like_price02);
			tv_like_price03 = (TextView) view
					.findViewById(R.id.tv_like_price03);
			tv_like_price04 = (TextView) view
					.findViewById(R.id.tv_like_price04);
			rl_like01 = (RelativeLayout) view.findViewById(R.id.rl_like01);
			rl_like02 = (RelativeLayout) view.findViewById(R.id.rl_like02);
			rl_like03 = (RelativeLayout) view.findViewById(R.id.rl_like03);
			rl_like04 = (RelativeLayout) view.findViewById(R.id.rl_like04);
			int size = tzmProductDetailModel.attribute.size();
			if (size > 0) {
				rl_like01.setVisibility(View.VISIBLE);
				imageLoader.display(im_like01,
						tzmProductDetailModel.attribute.get(0).image);
				tv_like_name01
						.setText(tzmProductDetailModel.attribute.get(0).name);
				tv_like_price01.setText("¥ "
						+ tzmProductDetailModel.attribute.get(0).price);
				rl_like01.setOnClickListener(this);
			}
			if (size > 1) {
				rl_like02.setVisibility(View.VISIBLE);
				imageLoader.display(im_like02,
						tzmProductDetailModel.attribute.get(1).image);
				tv_like_name02
						.setText(tzmProductDetailModel.attribute.get(1).name);
				tv_like_price02.setText("¥ "
						+ tzmProductDetailModel.attribute.get(1).price);
				rl_like02.setOnClickListener(this);
			}
			if (size > 2) {
				rl_like03.setVisibility(View.VISIBLE);
				imageLoader.display(im_like03,
						tzmProductDetailModel.attribute.get(2).image);
				tv_like_name03
						.setText(tzmProductDetailModel.attribute.get(2).name);
				tv_like_price03.setText("¥ "
						+ tzmProductDetailModel.attribute.get(2).price);
				rl_like03.setOnClickListener(this);
			}
			if (size > 3) {
				rl_like04.setVisibility(View.VISIBLE);
				imageLoader.display(im_like04,
						tzmProductDetailModel.attribute.get(3).image);
				tv_like_name04
						.setText(tzmProductDetailModel.attribute.get(3).name);
				tv_like_price04.setText("¥ "
						+ tzmProductDetailModel.attribute.get(3).price);
				rl_like04.setOnClickListener(this);
			}
			lv_img.addFooterView(view);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_like01:
			Intent intent = new Intent(getActivity(),
					ProductDetailActivity.class);
			intent.putExtra("id",
					tzmProductDetailModel.attribute.get(0).productId);
			intent.putExtra("activityId",
					tzmProductDetailModel.attribute.get(0).activityId);
			startActivity(intent);
			break;
		case R.id.rl_like02:
			Intent intent2 = new Intent(getActivity(),
					ProductDetailActivity.class);
			intent2.putExtra("id",
					tzmProductDetailModel.attribute.get(1).productId);
			intent2.putExtra("activityId",
					tzmProductDetailModel.attribute.get(1).activityId);
			startActivity(intent2);
			break;
		case R.id.rl_like03:
			Intent intent3 = new Intent(getActivity(),
					ProductDetailActivity.class);
			intent3.putExtra("id",
					tzmProductDetailModel.attribute.get(2).productId);
			intent3.putExtra("activityId",
					tzmProductDetailModel.attribute.get(2).activityId);
			startActivity(intent3);
			break;
		case R.id.rl_like04:
			Intent intent4 = new Intent(getActivity(),
					ProductDetailActivity.class);
			intent4.putExtra("id",
					tzmProductDetailModel.attribute.get(3).productId);
			intent4.putExtra("activityId",
					tzmProductDetailModel.attribute.get(3).activityId);
			startActivity(intent4);
			break;
		case R.id.iv_gotop:
			// gridView.setSelection(0);
			lv_img.smoothScrollToPosition(0);
			break;
		}

	}

}
