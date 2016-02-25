/**
 * 
 */
package com.ruiyu.taozhuma.fragment;

import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.ruiyu.taozhuma.R;
import com.ruiyu.taozhuma.activity.TzmCollectActivity;
import com.ruiyu.taozhuma.adapter.ScenesFragmentAdapter;
import com.ruiyu.taozhuma.adapter.ScenesListAdapter;
import com.ruiyu.taozhuma.api.ApiClient;
import com.ruiyu.taozhuma.api.ApiListener;
import com.ruiyu.taozhuma.api.ScenesFragmentApi;
import com.ruiyu.taozhuma.api.ScenesListApi;
import com.ruiyu.taozhuma.model.BaseModel;
import com.ruiyu.taozhuma.model.ScenesFragmentModel;
import com.ruiyu.taozhuma.model.ScenesListModel;
import com.ruiyu.taozhuma.utils.LogUtil;
import com.ruiyu.taozhuma.utils.ProgressDialogUtil;
import com.ruiyu.taozhuma.utils.ToastUtils;
import com.ruiyu.taozhuma.widget.ListViewForScrollView;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * @author 林尧 2015-12-21 场景套餐片段
 */
public class ScenesFragment extends Fragment {
	private String TAG = "ScenesFragment";

	@ViewInject(R.id.txt_head_title)
	private TextView txt_head_title;
	@ViewInject(R.id.tv_content_fragment)
	private TextView tv_content_fragment;
	
	@ViewInject(R.id.elistView)
	private ListViewForScrollView expandableListView;

	private ScenesFragmentApi scenesFragmentApi;
	private List<ScenesFragmentModel> scenesFragmentModels;
	private ScenesFragmentAdapter scenesFragmentAdapter;

	private ApiClient apiClient2;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater,
	 * android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		LogUtil.Log(TAG, "onCreateView");
		View view = inflater.inflate(R.layout.scenes_fragment, null);
		ViewUtils.inject(this, view);
		txt_head_title.setText("场景推荐");
		scenesFragmentApi = new ScenesFragmentApi();
		apiClient2 = new ApiClient(getActivity());
		loadData();
		return view;
	}

	/**
	 * 
	 */
	private void loadData() {
		/**
		 * 首页场景套餐列表
		 */
		// scenesListApi.setPageNo(1);
		apiClient2.api(scenesFragmentApi, new ApiListener() {

			@Override
			public void onStart() {
				ProgressDialogUtil.openProgressDialog(getActivity(),
						"", "");
			}

			@Override
			public void onException(Exception e) {
				ProgressDialogUtil.closeProgressDialog();
				LogUtil.ErrorLog(e);
				ToastUtils.showToast(getActivity(), "网络异常");
				
			}

			@Override
			public void onError(String error) {

				ToastUtils.showToast(getActivity(), error);
				LogUtil.Log(error);
				ProgressDialogUtil.closeProgressDialog();
			}

			@Override
			public void onComplete(String jsonStr) {
				ProgressDialogUtil.closeProgressDialog();
				Gson gson = new Gson();

				Type type = new TypeToken<BaseModel<List<ScenesFragmentModel>>>() {
				}.getType();
				BaseModel<List<ScenesFragmentModel>> base = gson.fromJson(
						jsonStr, type);
				if (base.result != null && base.result.size() > 0) {
					LogUtil.Log(TAG, base.result.size() + "");
					scenesFragmentModels = base.result;
					scenesFragmentAdapter = new ScenesFragmentAdapter(
							getActivity(), scenesFragmentModels);

					expandableListView.setAdapter(scenesFragmentAdapter);

				} else {
					tv_content_fragment.setVisibility(View.VISIBLE);
					expandableListView.setVisibility(View.GONE);
					ToastUtils.showShortToast(getActivity(), base.error_msg);
				}

			}
		}, true);

	}

}
