package com.ruiyu.taozhuma.activity;

import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ruiyu.taozhuma.R;
import com.ruiyu.taozhuma.model.BaseModel;
import com.ruiyu.taozhuma.model.TzmAreaModel;
import com.ruiyu.taozhuma.model.TzmCityModel;
import com.ruiyu.taozhuma.model.TzmProvinceModel;
import com.ruiyu.taozhuma.utils.LogUtil;
import com.ruiyu.taozhuma.utils.ProgressDialogUtil;
import com.ruiyu.taozhuma.utils.ResourceUtils;

public class CitiesActivity extends Activity implements OnClickListener,
		OnWheelChangedListener {
	/**
	 * 所有省
	 */
	protected String[] mProvinceDatas;
	/**
	 * key - 省 value - 市
	 */
	protected Map<String, String[]> mCitisDatasMap = new HashMap<String, String[]>();
	/**
	 * key - 市 values - 区
	 */
	protected Map<String, String[]> mAreaDatasMap = new HashMap<String, String[]>();

	/**
	 * key - 区 values - 邮编
	 */
	// protected Map<String, String> mZipcodeDatasMap = new HashMap<String,
	// String>();

	protected Map<String, Integer> map = new HashMap<String, Integer>();

	/**
	 * 当前省的名称
	 */
	protected String mCurrentProviceName;
	/**
	 * 当前市的名称
	 */
	protected String mCurrentCityName;
	/**
	 * 当前区的名称
	 */
	protected String mCurrentAreaName = "";

	/**
	 * 当前区的邮政编码
	 */
	// protected String mCurrentZipCode = "";

	protected Integer provinceId;
	protected Integer cityId;
	protected Integer areaId;

	/**
	 * 解析省市区的XML数据
	 */

	private WheelView mViewProvince;
	private WheelView mViewCity;
	private WheelView mViewDistrict;
	private TextView tv_cancel, tv_confirm;

	private Intent resultIntent;

	private final static String fileName = "province.json";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.citys);
		getWindow().setLayout(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		setUpViews();
		setUpListener();
		setUpData();
	}

	private void setUpData() {
		// TODO Auto-generated method stub
		initList();
		// initProvinceDatas();
		mViewProvince.setViewAdapter(new ArrayWheelAdapter<String>(
				CitiesActivity.this, mProvinceDatas));
		// 设置可见条目数量
		mViewProvince.setVisibleItems(7);
		mViewCity.setVisibleItems(7);
		mViewDistrict.setVisibleItems(7);
		mViewProvince.setWheelBackground(R.drawable.wheel_bg_shape);

		updateCities();
		updateAreas();
	}

	private void setUpListener() {
		// 添加change事件
		mViewProvince.addChangingListener(this);
		// 添加change事件
		mViewCity.addChangingListener(this);
		// 添加change事件
		mViewDistrict.addChangingListener(this);
		// 添加onclick事件
		tv_cancel.setOnClickListener(this);
		tv_confirm.setOnClickListener(this);

	}

	private void setUpViews() {
		resultIntent = getIntent();
		mViewProvince = (WheelView) findViewById(R.id.id_province);
		mViewCity = (WheelView) findViewById(R.id.id_city);
		mViewDistrict = (WheelView) findViewById(R.id.id_district);
		tv_cancel = (TextView) findViewById(R.id.tv_cancel);
		tv_confirm = (TextView) findViewById(R.id.tv_confirm);

	}

	protected void initList() {

		try {
			LogUtil.Log("before start work at "
					+ Calendar.getInstance().getTimeInMillis());
			String jsonStr = ResourceUtils.geFileFromAssets(this, fileName);
			Gson gson = new Gson();
			Type type = new TypeToken<BaseModel<List<TzmProvinceModel>>>() {
			}.getType();
			BaseModel<List<TzmProvinceModel>> base = gson.fromJson(jsonStr,
					type);
			if (base.success && base.result != null) {
				List<TzmProvinceModel> provinceModels = base.result;
				// */ 初始化默认选中的省、市、区
				if (provinceModels != null && !provinceModels.isEmpty()) {
					mCurrentProviceName = provinceModels.get(0).provincState;
					provinceId = provinceModels.get(0).provinceId;
					List<TzmCityModel> cityList = provinceModels.get(0).cities;
					if (cityList != null && !cityList.isEmpty()) {
						mCurrentCityName = cityList.get(0).cityState;
						cityId = cityList.get(0).cityId;
						List<TzmAreaModel> districtList = cityList.get(0).areas;
						mCurrentAreaName = districtList.get(0).areaState;
						areaId = districtList.get(0).areaId;
						// mCurrentZipCode = districtList.get(0).getZipcode();
					}
				}
				// */
				mProvinceDatas = new String[provinceModels.size()];
				for (int i = 0; i < provinceModels.size(); i++) {
					// 遍历所有省的数据
					mProvinceDatas[i] = provinceModels.get(i).provincState;
					map.put(provinceModels.get(i).provincState,
							provinceModels.get(i).provinceId);
					List<TzmCityModel> cityList = provinceModels.get(i).cities;
					String[] cityNames = new String[cityList.size()];
					for (int j = 0; j < cityList.size(); j++) {
						// 遍历省下面的所有市的数据
						cityNames[j] = cityList.get(j).cityState;
						map.put(cityNames[j], cityList.get(j).cityId);
						List<TzmAreaModel> areaModels = cityList.get(j).areas;
						String[] areaNames = new String[areaModels.size()];
						for (int k = 0; k < areaModels.size(); k++) {
							// 遍历市下面所有区/县的数据
							areaNames[k] = areaModels.get(k).areaState;
							map.put(areaModels.get(k).areaState,
									areaModels.get(k).areaId);
							// TzmAreaModel districtModel = new TzmAreaModel();
							// 区/县对于的邮编，保存到mZipcodeDatasMap
							// mZipcodeDatasMap.put(districtList.get(k).getName(),
							// districtList.get(k).getZipcode());
							// distrinctArray[k] = districtModel;
							// distrinctNameArray[k] = districtModel.areaState;
						}
						// 市-区/县的数据，保存到mDistrictDatasMap
						mAreaDatasMap.put(cityNames[j], areaNames);
					}
					// 省-市的数据，保存到mCitisDatasMap
					mCitisDatasMap.put(provinceModels.get(i).provincState,
							cityNames);

				}
				LogUtil.Log("after start work at "
						+ Calendar.getInstance().getTimeInMillis());
				ProgressDialogUtil.closeProgressDialog();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// protected void initProvinceDatas() {
	// List<ProvinceModel2> provinceList = null;
	// AssetManager asset = getAssets();
	// try {
	// InputStream input = asset.open("province_data.xml");
	// // 创建一个解析xml的工厂对象
	// SAXParserFactory spf = SAXParserFactory.newInstance();
	// // 解析xml
	// SAXParser parser = spf.newSAXParser();
	// XmlParserHandler handler = new XmlParserHandler();
	// parser.parse(input, handler);
	// input.close();
	// // 获取解析出来的数据
	// provinceList = handler.getDataList();
	// // */ 初始化默认选中的省、市、区
	// if (provinceList != null && !provinceList.isEmpty()) {
	// mCurrentProviceName = provinceList.get(0).getName();
	// List<CityModel> cityList = provinceList.get(0).getCityList();
	// if (cityList != null && !cityList.isEmpty()) {
	// mCurrentCityName = cityList.get(0).getName();
	// List<DistrictModel> districtList = cityList.get(0)
	// .getDistrictList();
	// mCurrentDistrictName = districtList.get(0).getName();
	// mCurrentZipCode = districtList.get(0).getZipcode();
	// }
	// }
	// // */
	// mProvinceDatas = new String[provinceList.size()];
	// for (int i = 0; i < provinceList.size(); i++) {
	// // 遍历所有省的数据
	// mProvinceDatas[i] = provinceList.get(i).getName();
	// List<CityModel> cityList = provinceList.get(i).getCityList();
	// String[] cityNames = new String[cityList.size()];
	// for (int j = 0; j < cityList.size(); j++) {
	// // 遍历省下面的所有市的数据
	// cityNames[j] = cityList.get(j).getName();
	// List<DistrictModel> districtList = cityList.get(j)
	// .getDistrictList();
	// String[] distrinctNameArray = new String[districtList
	// .size()];
	// DistrictModel[] distrinctArray = new DistrictModel[districtList
	// .size()];
	// for (int k = 0; k < districtList.size(); k++) {
	// // 遍历市下面所有区/县的数据
	// DistrictModel districtModel = new DistrictModel(
	// districtList.get(k).getName(), districtList
	// .get(k).getZipcode());
	// // 区/县对于的邮编，保存到mZipcodeDatasMap
	// mZipcodeDatasMap.put(districtList.get(k).getName(),
	// districtList.get(k).getZipcode());
	// distrinctArray[k] = districtModel;
	// distrinctNameArray[k] = districtModel.getName();
	// }
	// // 市-区/县的数据，保存到mDistrictDatasMap
	// mDistrictDatasMap.put(cityNames[j], distrinctNameArray);
	// }
	// // 省-市的数据，保存到mCitisDatasMap
	// mCitisDatasMap.put(provinceList.get(i).getName(), cityNames);
	// }
	// } catch (Throwable e) {
	// e.printStackTrace();
	// } finally {
	//
	// }
	// }

	/**
	 * 根据当前的省，更新市WheelView的信息
	 */
	private void updateCities() {
		int pCurrent = mViewProvince.getCurrentItem();
		mCurrentProviceName = mProvinceDatas[pCurrent];
		provinceId = map.get(mCurrentProviceName);
		String[] cities = mCitisDatasMap.get(mCurrentProviceName);
		if (cities == null) {
			cities = new String[] { "" };
		}
		mViewCity.setViewAdapter(new ArrayWheelAdapter<String>(this, cities));
		mViewCity.setCurrentItem(0);
		updateAreas();
	}

	/**
	 * 根据当前的市，更新区WheelView的信息
	 */
	private void updateAreas() {
		int pCurrent = mViewCity.getCurrentItem();
		mCurrentCityName = mCitisDatasMap.get(mCurrentProviceName)[pCurrent];
		cityId = map.get(mCurrentCityName);
		String[] areas = mAreaDatasMap.get(mCurrentCityName);
		if (areas == null) {
			areas = new String[] { "" };
		}
		mCurrentAreaName = areas[0];
		areaId = map.get(mCurrentAreaName);
		mViewDistrict
				.setViewAdapter(new ArrayWheelAdapter<String>(this, areas));
		mViewDistrict.setCurrentItem(0);
	}

	@Override
	public void onChanged(WheelView wheel, int oldValue, int newValue) {
		if (wheel == mViewProvince) {
			updateCities();
		} else if (wheel == mViewCity) {
			updateAreas();
		} else if (wheel == mViewDistrict) {
			mCurrentAreaName = mAreaDatasMap.get(mCurrentCityName)[newValue];
			areaId = map.get(mCurrentAreaName);
			// mCurrentZipCode = mZipcodeDatasMap.get(mCurrentDistrictName);
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_confirm:
			resultIntent.putExtra("mCurrentProviceName", mCurrentProviceName);
			resultIntent.putExtra("mCurrentCityName", mCurrentCityName);
			resultIntent.putExtra("mCurrentAreaName", mCurrentAreaName);
			resultIntent.putExtra("provinceId", provinceId);
			resultIntent.putExtra("cityId", cityId);
			resultIntent.putExtra("areaId", areaId);
			setResult(Activity.RESULT_OK, resultIntent);
			finish();
			break;
		case R.id.tv_cancel:
			setResult(Activity.RESULT_CANCELED, resultIntent);
			finish();
			break;
		}

	}
}