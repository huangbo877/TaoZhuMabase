package com.ruiyu.taozhuma.fragment;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.ruiyu.taozhuma.R;
import com.ruiyu.taozhuma.adapter.TzmCommentListAdapter;
import com.ruiyu.taozhuma.api.ApiClient;
import com.ruiyu.taozhuma.api.ApiListener;
import com.ruiyu.taozhuma.api.TzmProductDetailCommentApi;
import com.ruiyu.taozhuma.model.BaseModel;
import com.ruiyu.taozhuma.model.TzmProductDetailCommentModel;
import com.ruiyu.taozhuma.utils.LogUtil;
import com.ruiyu.taozhuma.utils.ProgressDialogUtil;
import com.ruiyu.taozhuma.widget.CustListView;
import com.ruiyu.taozhuma.widget.FlexibleRatingBar;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ProductDetailCommentFragment extends Fragment {

	@ViewInject(R.id.lv_comment)
	private CustListView lv_comment;

	private TzmProductDetailCommentApi api;
	private List<TzmProductDetailCommentModel> commentModels;
	private ApiClient apiClient;
	private TzmCommentListAdapter commentListAdapter;

	private boolean isLoadmore = false;// 是否继续加载
	private int pageNo = 1;
	private Integer pid;
	private boolean isbottom = false;
	View view;
	@ViewInject(R.id.tv_empty)
	private TextView tv_empty;
	@ViewInject(R.id.tv_fs)
	private TextView tv_fs;
	@ViewInject(R.id.rabarServer)
	private FlexibleRatingBar rabarServer;
	@ViewInject(R.id.tv_quan)
	private TextView tv_quan;
	@ViewInject(R.id.tv_hao)
	private TextView tv_hao;
	@ViewInject(R.id.tv_zhong)
	private TextView tv_zhong;
	@ViewInject(R.id.tv_cha)
	private TextView tv_cha;
	@ViewInject(R.id.linearLayout2)
	private LinearLayout linearLayout2;
	@ViewInject(R.id.linearLayout1)
	private LinearLayout linearLayout1;
	int score = 0;// 默认全部

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View v = inflater.inflate(R.layout.product_detail_comment_fragment,
				null);
		ViewUtils.inject(this, v);

		initView();
		handler.sendEmptyMessage(0);
		commentModels = new ArrayList<TzmProductDetailCommentModel>();
		lv_comment.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
					if (view.getLastVisiblePosition() == view.getCount() - 1) {
						if (!isbottom) {
							isLoadmore = true;
							pageNo++;
							loadData(pid);
						}

					}
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// lastItem = firstVisibleItem + visibleItemCount - 1 ;
			}
		});
		view = LayoutInflater.from(getActivity()).inflate(
				R.layout.footview_loading, null);
		return v;
	}

	private void initView() {
		tv_quan.setOnClickListener(clickListener);
		tv_hao.setOnClickListener(clickListener);
		tv_zhong.setOnClickListener(clickListener);
		tv_cha.setOnClickListener(clickListener);
	}

	OnClickListener clickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.tv_quan:
				handler.sendEmptyMessage(0);
				break;
			case R.id.tv_hao:
				handler.sendEmptyMessage(1);
				break;
			case R.id.tv_zhong:
				handler.sendEmptyMessage(2);
				break;
			case R.id.tv_cha:
				handler.sendEmptyMessage(3);
				break;

			}
		}
	};
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {

			switch (msg.what) {
			case 0:
				tv_quan.setTextColor(getResources().getColor(R.color.base));
				tv_quan.setBackgroundResource(R.drawable.bg_pink_shape_corner);
				tv_hao.setTextColor(getResources()
						.getColor(R.color.text_chesk4));
				tv_hao.setBackgroundResource(R.drawable.bg_gray_shape_corner2);
				tv_zhong.setTextColor(getResources().getColor(
						R.color.text_chesk4));
				tv_zhong.setBackgroundResource(R.drawable.bg_gray_shape_corner2);
				tv_cha.setTextColor(getResources()
						.getColor(R.color.text_chesk4));
				tv_cha.setBackgroundResource(R.drawable.bg_gray_shape_corner2);
				isLoadmore = false;
				isbottom = false;
				score = 0;
				pageNo = 1;
				lv_comment.removeFooterView(view);
				loadData(pid);
				break;
			case 1:
				tv_quan.setTextColor(getResources().getColor(
						R.color.text_chesk4));
				tv_quan.setBackgroundResource(R.drawable.bg_gray_shape_corner2);
				tv_hao.setTextColor(getResources().getColor(R.color.base));
				tv_hao.setBackgroundResource(R.drawable.bg_pink_shape_corner);
				tv_zhong.setTextColor(getResources().getColor(
						R.color.text_chesk4));
				tv_zhong.setBackgroundResource(R.drawable.bg_gray_shape_corner2);
				tv_cha.setTextColor(getResources()
						.getColor(R.color.text_chesk4));
				tv_cha.setBackgroundResource(R.drawable.bg_gray_shape_corner2);
				isLoadmore = false;
				isbottom = false;
				score = 3;
				pageNo = 1;
				lv_comment.removeFooterView(view);
				loadData(pid);
				break;
			case 2:
				tv_quan.setTextColor(getResources().getColor(
						R.color.text_chesk4));
				tv_quan.setBackgroundResource(R.drawable.bg_gray_shape_corner2);
				tv_hao.setTextColor(getResources()
						.getColor(R.color.text_chesk4));
				tv_hao.setBackgroundResource(R.drawable.bg_gray_shape_corner2);
				tv_zhong.setTextColor(getResources().getColor(R.color.base));
				tv_zhong.setBackgroundResource(R.drawable.bg_pink_shape_corner);
				tv_cha.setTextColor(getResources()
						.getColor(R.color.text_chesk4));
				tv_cha.setBackgroundResource(R.drawable.bg_gray_shape_corner2);
				isbottom = false;
				isLoadmore = false;
				score = 2;
				pageNo = 1;
				lv_comment.removeFooterView(view);
				loadData(pid);
				break;
			case 3:
				tv_quan.setTextColor(getResources().getColor(
						R.color.text_chesk4));
				tv_quan.setBackgroundResource(R.drawable.bg_gray_shape_corner2);
				tv_hao.setTextColor(getResources()
						.getColor(R.color.text_chesk4));
				tv_hao.setBackgroundResource(R.drawable.bg_gray_shape_corner2);
				tv_zhong.setTextColor(getResources().getColor(
						R.color.text_chesk4));
				tv_zhong.setBackgroundResource(R.drawable.bg_gray_shape_corner2);
				tv_cha.setTextColor(getResources().getColor(R.color.base));
				tv_cha.setBackgroundResource(R.drawable.bg_pink_shape_corner);
				isLoadmore = false;
				isbottom = false;
				score = 1;
				pageNo = 1;
				lv_comment.removeFooterView(view);
				loadData(pid);
				break;

			}
		};
	};

	public void loadData(Integer id) {
		pid = id;
		apiClient = new ApiClient(getActivity());
		api = new TzmProductDetailCommentApi();
		api.setId(pid);
		api.setPageNo(pageNo);
		api.setscore(score);
		apiClient.api(this.api, new ApiListener() {
			@Override
			public void onStart() {
				if (!isLoadmore) {
					ProgressDialogUtil
							.openProgressDialog(getActivity(), "", "");
				}

			}

			@Override
			public void onComplete(String jsonStr) {
				ProgressDialogUtil.closeProgressDialog();
				Gson gson = new Gson();
				Type type = new TypeToken<BaseModel<ArrayList<TzmProductDetailCommentModel>>>() {
				}.getType();
				BaseModel<ArrayList<TzmProductDetailCommentModel>> base = gson
						.fromJson(jsonStr, type);

				if (isLoadmore) {
					if (base.result != null && base.result.size() > 0) {

						commentModels.addAll(base.result);
						commentListAdapter.notifyDataSetChanged();
					} else {
						isbottom = true;
						lv_comment.removeFooterView(view);
					}
				} else {
					// 第一页
					if (base.result != null && base.result.size() > 0) {

						tv_empty.setVisibility(View.GONE);
						lv_comment.setVisibility(View.VISIBLE);
						commentModels = base.result;
						commentListAdapter = new TzmCommentListAdapter(
								getActivity(), commentModels);
						if (base.result.size() >= 10) {
							lv_comment.addFooterView(view);

						}

						lv_comment.setAdapter(commentListAdapter);
					} else {
						isbottom = true;
						lv_comment.removeFooterView(view);
						tv_empty.setVisibility(View.VISIBLE);
						lv_comment.setVisibility(View.GONE);

					}
				}

			}

			@Override
			public void onError(String error) {
				// ToastUtils.showShortToast(TzmProductDetailActivity.this,
				// R.string.msg_list_null);
				ProgressDialogUtil.closeProgressDialog();
				LogUtil.Log(error);
			}

			@Override
			public void onException(Exception e) {
				ProgressDialogUtil.closeProgressDialog();
				LogUtil.ErrorLog(e);
			}
		}, true);
	}

	
	@Override
	public void onDestroy() {
		
		handler.removeCallbacksAndMessages(null);
		super.onDestroy();
	}

	/**
	 * 给星控件赋值
	 */
	public void initfs(float pCmmt) {
		// TODO Auto-generated method stub
		rabarServer.setRating(pCmmt);
		tv_fs.setText(pCmmt + "");

	}
}
