package com.ruiyu.taozhuma.fragment;

import java.lang.reflect.Type;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.ruiyu.taozhuma.R;
import com.ruiyu.taozhuma.adapter.TzmActivityListAdapter;
import com.ruiyu.taozhuma.api.ApiClient;
import com.ruiyu.taozhuma.api.ApiListener;
import com.ruiyu.taozhuma.api.TzmAppActivityListApi;
import com.ruiyu.taozhuma.even.ScrollEven;
import com.ruiyu.taozhuma.model.BaseModel;
import com.ruiyu.taozhuma.model.TzmAppActivityListModel;
import com.ruiyu.taozhuma.utils.LogUtil;
import com.ruiyu.taozhuma.widget.ListViewForScrollView;

import de.greenrobot.event.EventBus;

import android.support.v4.app.Fragment;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.AbsListView.OnScrollListener;

/**
 * app活动列表
 */
public class AppActivityListFragment extends Fragment {

	private ArrayList<TzmAppActivityListModel> listModels;
	private TzmAppActivityListApi api;
	private int currentPage = 1;// 当前页数
	private ApiClient apiClient;
	private TzmActivityListAdapter activityListAdapter;
	private float y = 0;
	@ViewInject(R.id.lv_activity)
	private ListViewForScrollView lv_activity;
	@ViewInject(R.id.rl_bg)
	private RelativeLayout rl_bg;
	@ViewInject(R.id.rl_lv)
	private RelativeLayout rl_lv;
	@ViewInject(R.id.iv_gotop)
	private ImageView iv_gotop;
	BitmapUtils bitmapUtils;
	private boolean isTop = true;
	private int type;
	private int id;

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.control_list_fragment, null);
		ViewUtils.inject(this, view);
		EventBus.getDefault().register(this);
		Bundle bundle = getArguments();
		id = bundle.getInt("id", 0);
		type = bundle.getInt("type", 0);
		// initView(view);
		lv_activity.setOnTouchListener(new OnTouchListener() {

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
		lv_activity.setOnScrollListener(new OnScrollListener() {

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
		iv_gotop.setOnClickListener(clickListener);
		loadData();
		return view;

	}

	public void onEventMainThread(ScrollEven event) {
		lv_activity.smoothScrollToPosition(0);
		// mImageSwitcher.requestFocus();
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

	// 访问接口,获取列表的数据源
	protected void loadData() {
		api = new TzmAppActivityListApi();
		listModels = new ArrayList<TzmAppActivityListModel>();
		apiClient = new ApiClient(getActivity());
		api.setPageNo(currentPage);
		api.setType(type);
		api.setid(id);
		apiClient.api(this.api, new ApiListener() {
			@Override
			public void onStart() {
			}

			@Override
			public void onComplete(String jsonStr) {
				Gson gson = new Gson();
				Type type = new TypeToken<BaseModel<ArrayList<TzmAppActivityListModel>>>() {
				}.getType();
				BaseModel<ArrayList<TzmAppActivityListModel>> base = gson
						.fromJson(jsonStr, type);
				if (base.result != null) {
					listModels = base.result;
					rl_bg.setVisibility(View.GONE);
					rl_lv.setVisibility(View.VISIBLE);
				} else {
					rl_bg.setVisibility(View.VISIBLE);
					rl_lv.setVisibility(View.GONE);
				}
				// if (isLoadMore) {
				// // 加载更多
				// listPosition = tzmClientInfoModels.size();
				// if (base.result != null) {
				// tzmClientInfoModels.addAll(base.result);
				// } else {
				// ToastUtils.showShortToast(TzmClientInfoActivity.this,
				// R.string.msg_list_null);
				// }
				//
				// } else {
				// // 刷新
				// listPosition = 0;
				// tzmClientInfoModels.clear();
				// if (base.result != null) {
				// tzmClientInfoModels = base.result;
				// } else {
				// ToastUtils.showShortToast(TzmClientInfoActivity.this,
				// R.string.msg_list_null);
				// }
				// }

				initClientInfoLlist();
			}

			@Override
			public void onError(String error) {
				// ToastUtils
				// .showShortToast(getActivity(), R.string.msg_list_null);
				rl_bg.setVisibility(View.VISIBLE);
				rl_lv.setVisibility(View.GONE);

			}

			@Override
			public void onException(Exception e) {
				LogUtil.ErrorLog(e);
			}
		}, true);

	}

	// 添加适配器,适配listview的item
	protected void initClientInfoLlist() {
		if (this.listModels != null) {
			activityListAdapter = new TzmActivityListAdapter(getActivity(),
					this.listModels);
			lv_activity.setAdapter(activityListAdapter);// 调用Adapter的getView()绘制item
			// lv_client_info_list.setSelection(listPosition);
			// mPullRefreshListView.onRefreshComplete();
		}
	}

	OnClickListener clickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.iv_gotop:
				lv_activity.smoothScrollToPosition(0);
				break;
			}

		}

	};

	// private void initView(View view) {
	// lv_control_list=(PullToRefreshListView)
	// view.findViewById(R.id.lv_control_list);
	// lv_control_list
	// .setOnRefreshListener(new OnRefreshListener2<ListView>() {
	//
	// @Override
	// public void onPullDownToRefresh(
	// PullToRefreshBase<ListView> refreshView) {
	// // TODO 下拉刷新
	// String label = DateUtils.formatDateTime(
	// getActivity().getApplicationContext(),
	// System.currentTimeMillis(),
	// DateUtils.FORMAT_SHOW_TIME
	// | DateUtils.FORMAT_SHOW_DATE
	// | DateUtils.FORMAT_ABBREV_ALL);
	//
	// // Update the LastUpdatedLabel
	// refreshView.getLoadingLayoutProxy()
	// .setLastUpdatedLabel(label);
	//
	// isLoadMore = false;
	// currentPage = 1;
	// // loadData();
	// }
	//
	// @Override
	// public void onPullUpToRefresh(
	// PullToRefreshBase<ListView> refreshView) {
	// isLoadMore = true;
	// currentPage++;
	// // loadData();
	//
	// }
	// });

	// listView = lv_control_list.getRefreshableView();
	// adapter = new ListViewAdapter(
	// getActivity(), this.listViewModels);
	// listView.setAdapter(adapter);
	// listView.setSelection(listPosition);
	// lv_control_list.onRefreshComplete();
	// }
	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		EventBus.getDefault().unregister(this);
	}
}
