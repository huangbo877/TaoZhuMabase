package com.ruiyu.taozhuma.activity;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.Locale;

import com.ruiyu.taozhuma.R;
import com.ruiyu.taozhuma.utils.ToastUtils;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

//选择文件操作类
public class SelectPicActivity extends Activity implements OnClickListener {

	// 使用照相机拍照获取图片
	public static final int SELECT_PIC_BY_TACK_PHOTO = 1;
	// 使用相册中的图片
	public static final int SELECT_PIC_BY_PICK_PHOTO = 2;
	// 剪切图片
	public static final int SELECT_PIC_BY_CUT_PHOTO = 3;
	// 从Intent获取图片路径的KEY
	public static final String KEY_PHOTO_PATH = "photo_path";
	public static final String KEY_PHOTO_TYPE = "type";
	private static final String TAG = "SelectPicActivity";
	private LinearLayout dialogLayout;
	private Button takePhotoBtn, pickPhotoBtn, cancelBtn;

	/** 获取到的图片路径 */
	private String picPath;
	private Intent lastIntent;
	private Uri photoUri;
	private File photoFile; // 剪切转存后的照片
	private File photoDir; // 拍照时，存储的照片目录
	private File cache; // 剪切完成时，存储的照片目录
	private Boolean isEdit = false; // 判断图片是否已经被编辑
	private static String photoPath = "/sdcard/personal/";
	private int crop = 300;
	private int imgId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_pic_layout);
		imgId = getIntent().getIntExtra("imgId", 0);
		dialogLayout = (LinearLayout) findViewById(R.id.dialog_layout);
		dialogLayout.setOnClickListener(this);
		takePhotoBtn = (Button) findViewById(R.id.btn_take_photo);
		takePhotoBtn.setOnClickListener(this);
		pickPhotoBtn = (Button) findViewById(R.id.btn_pick_photo);
		pickPhotoBtn.setOnClickListener(this);
		cancelBtn = (Button) findViewById(R.id.btn_cancel);
		cancelBtn.setOnClickListener(this);
		lastIntent = getIntent();
		// 在创建cache文件夹,位置mnt/sdcard/mnt
		cache = new File(Environment.getExternalStorageDirectory(), "personal");
		if (!cache.exists())
			cache.mkdirs();
		// 创建photoDir目录,用于存储拍照相片
		photoDir = new File(Environment.getExternalStorageDirectory(),
				"DCIM/Camera");
		if (!photoDir.exists())
			photoDir.mkdirs();
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.dialog_layout:
			finish();
			break;
		case R.id.btn_take_photo:
			takePhoto();
			break;
		case R.id.btn_pick_photo:
			pickPhoto();
			break;
		default:
			finish();
			break;
		}
	}

	/**
	 * 拍照获取图片
	 */
	private void takePhoto() {
		// 执行拍照前，应该先判断SD卡是否存在
		String SDState = Environment.getExternalStorageState();
		if (SDState.equals(Environment.MEDIA_MOUNTED)) {

			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);// "android.media.action.IMAGE_CAPTURE"
			/***
			 * 需要说明一下，以下操作使用照相机拍照，拍照后的图片会存放在相册中的 这里使用的这种方式有一个好处就是获取的图片是拍照后的原图
			 * 如果不实用ContentValues存放照片路径的话，拍照后获取的图片为缩略图不清晰
			 */
			ContentValues values = new ContentValues();
			photoUri = this.getContentResolver().insert(
					MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
			intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, photoUri);
			/** ----------------- */
			startActivityForResult(intent, SELECT_PIC_BY_TACK_PHOTO);
		} else {
			ToastUtils.showShortToast(this, "内存卡不存在");
		}
	}

	/***
	 * 从相册中取图片
	 */
	private void pickPhoto() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("image/*");
		intent.addCategory(Intent.CATEGORY_OPENABLE);
		try {
			startActivityForResult(Intent.createChooser(intent, "请选择一个要上传的文件"),
					SELECT_PIC_BY_PICK_PHOTO);
		} catch (android.content.ActivityNotFoundException ex) {
			ToastUtils.showShortToast(this, "请安装文件管理器");
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		finish();
		return super.onTouchEvent(event);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == SELECT_PIC_BY_TACK_PHOTO
					|| requestCode == SELECT_PIC_BY_PICK_PHOTO) {
				doPhoto(requestCode, data);
			} else if (requestCode == SELECT_PIC_BY_CUT_PHOTO) {
				if (data != null) {
					setPicToView(data);
				}
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * 保存裁剪之后的图片数据
	 * 
	 * @param picdata
	 */
	private void setPicToView(Intent picdata) {
		Bundle extras = picdata.getExtras();
		if (extras != null) {
			Bitmap bmp = extras.getParcelable("data");
			// --转存图片路径
			saveImage(bmp);
		}
	}

	// 保存转存图片
	private void saveImage(Bitmap bitmap) {
		FileOutputStream fos = null;
		BufferedOutputStream bos = null;
		ByteArrayOutputStream baos = null;// 字节数组输出流
		String headImgPath = null;
		try {
			baos = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
			byte[] byteArray = baos.toByteArray();// 字节数组输出流转换成字节数组
			// 将字节数组写入到刚创建的图片文件中
			String name = imgId + "tzm.jpg";
			headImgPath = photoPath + name;
			photoFile = new File(headImgPath);// 转存后的图片,本地图片
			if (!photoFile.exists())
				photoFile.createNewFile();
			/**
			 * 最后一步选择剪切后图片，获取图片的路径
			 * 
			 * @param requestCode
			 * @param data
			 */
			fos = new FileOutputStream(photoFile);
			bos = new BufferedOutputStream(fos);
			bos.write(byteArray);
			picPath = photoFile.getAbsolutePath();
			Log.i(TAG, "imagePath = " + picPath);
			/*
			 * if(picPath != null && ( picPath.endsWith(".png") ||
			 * picPath.endsWith(".PNG") ||picPath.endsWith(".jpg")
			 * ||picPath.endsWith(".JPG") ))
			 */
			if (picPath != null) {
				lastIntent.putExtra(KEY_PHOTO_TYPE, 0);
				lastIntent.putExtra(KEY_PHOTO_PATH, picPath);
				setResult(Activity.RESULT_OK, lastIntent);
				finish();
			} else {
			    ToastUtils.showShortToast(this, "选择文件不正确!");
			}
			isEdit = true; // --图片已编辑
		} catch (Exception e) {
			isEdit = false; // --图片未编辑
			e.printStackTrace();
		} finally {
			if (baos != null) {
				try {
					baos.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (bos != null) {
				try {
					bos.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (fos != null) {
				try {
					fos.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 选择图片后，获取图片的路径
	 * 
	 * @param requestCode
	 * @param data
	 */
	private void doPhoto(int requestCode, Intent data) {
		if (requestCode == SELECT_PIC_BY_PICK_PHOTO) // 从相册取图片，有些手机有异常情况，请注意
		{
			if (data == null) {
				ToastUtils.showShortToast(this, "选择图片文件出错");
				return;
			}
			photoUri = data.getData();
			if (photoUri == null) {
				ToastUtils.showShortToast(this, "选择图片文件出错");
				return;
			}
		}
		// 剪切图片
		startPhotoZoom(photoUri);
	}

	protected String getAbsoluteImagePath(Uri uri) {
		// can post image
		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor cursor = managedQuery(uri, proj, // Which columns to return
				null, // WHERE clause; which rows to return (all rows)
				null, // WHERE clause selection arguments (none)
				null); // Order-by clause (ascending by name)
		if (cursor != null) {
			int column_index = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();

			return cursor.getString(column_index);
		} else {

			// 如果游标为空说明获取的已经是绝对路径了
			return uri.getPath();
		}
	}

	/**
	 * 裁剪图片方法实现
	 * 
	 * @param uri
	 */
	public void startPhotoZoom(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", crop);
		intent.putExtra("outputY", crop);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, SELECT_PIC_BY_CUT_PHOTO);
	}

}
