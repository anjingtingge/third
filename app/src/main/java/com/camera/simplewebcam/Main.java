package com.camera.simplewebcam;

import android.app.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;

import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.camera.simplewebcam.utils.SerialPortUtil;
import com.youshi.mylibrary.FoodIdentify;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Array;
import java.sql.Time;
import java.util.ArrayList;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import util.MultiPartStack;
import util.MultiPartStringRequest;

import static android.R.attr.text;
import static android.R.attr.y;
import static android.os.Build.VERSION_CODES.M;
import static com.camera.simplewebcam.R.drawable.d;
import static com.camera.simplewebcam.R.id.shidu;


public class Main extends Activity {


//	CameraPreview cp;
static CameraPreview cameraPreview;
	private static byte[] metacamData;
	static final int IMG_WIDTH=1280;
	static final int IMG_HEIGHT=720;
	static final String dwins = "dwins";
    private static ImageView imageViewOne,gaoshi,dishi,zhongshi;

//	private static ImageView imageViewTwo;
//	private static ImageView imageViewThree;
	public static final String JD_PIC_PATH = Environment
			.getExternalStorageDirectory().getPath() + "/DCIM/hisense";
	private static boolean isClick = false;
	private static Handler mhandler = new Handler();
//	private static Button cpatureBtn;

	private static long prepareCameraTime;
	private static long takePicTime;
	private long savePicTime;
	private static String strTime = "";
	private  TextView bangzhu;


	private EditText editInput;
//	private Button btnSend,close_btn,delete_btn,delete_current_btn;
	private ImageView close_btn;
	private static TextView textShow,shibie;
	private  static boolean open=false,flage=true;
	public  Thread receiveThread = null;

	private static int current=0;
	private Button refresh,new_button;
	private static int i=0,j=-1,q=0,dishib=0,zhongshib=1,gaoshib=0;






	//图片识别
private static RelativeLayout llLayout;
	private static Button tempbuttonButton[]=new Button[50];
//	private static  Button tempbuttonButton[];
	private List<Food> top,middle,bottom;
	private List<Map<String, Object>> data_list;
	private List<Map<String, Object>> data_list1;
	private List<Map<String, Object>> data_list2;
	    private TextView textdisplay;

	private int[] icon;

	private String[] iconName_CN,iconName_EN;
	//    public Button fanhui;
	public String url = "";//在此写入服务器地址
	private static RequestQueue mSingleQueue;
	private File[] file;
	private static String filepath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath();
	private String pathtri = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath();
	public static final String PIC_INFO_NAME = "recognize_result.txt";
	private String focus0="0.jpg",focus1="1.jpg",focus2="2.jpg";
	int width;
	int height;

	private static NumberAnimTextView mNumberAnimTextView2;

	private PopupWindow popWin = null; // 弹出窗口
	private View popView = null; // 保存弹出窗口布局
	private boolean show=true;

//private Bitmap bb,	bmp = null;


	private Main.MyBtnClicker myBtnClicker = new Main.MyBtnClicker();


	//创建一个Handler
	private Handler handler = new Handler() {

		public void handleMessage(Message msg) {
			switch (msg.what) {
				case 1:
					//在这里可以进行UI操作
					//对msg.obj进行String强制转换
					handler.postDelayed(new Runnable() {

						@Override
						public void run() {
							//do something

							GetResponse();
						}
					}, 1000);    //延时1s执行




//					textShow.setTextColor(Color.RED);
					break;
//				case 2:
//					addButton();
//
//					break;
				default:

					break;
			}
		}

	};

	private class MyBtnClicker implements View.OnClickListener {
		@Override
		public void onClick(View view) {

			switch (view.getId()) {

//				case R.id.back:
//					finish();
//					break;

				case R.id.bangzhu:
//					PictureManger.deleteAllPictures();
//					showExitDialog01();
//					Log.e("帮助", "onClick");
//					popWin.setFocusable(false);
//					popWin.showAtLocation(view,Gravity.NO_GRAVITY, 0, 0); // 显示弹出窗口
//
//					new Handler().postDelayed(new Runnable() {
//
//						@Override
//						public void run() {
//							//do somethingpo
//							popWin.dismiss();
//						}
//					}, 3000);    //延时1s执行

					Intent intent = new Intent(Settings.ACTION_SETTINGS);
					startActivity(intent);

//					GetResponse();
//					finish();
					break;
//				case R.id.delete_current_btn:
//					PictureManger.deleteCurrentPictures();
//					break;
//				case R.id.refresh:
//					Toast toast = Toast.makeText(Main.this,"正在识别，请稍后 ...",Toast.LENGTH_SHORT);
//					toast.setGravity(Gravity.CENTER_HORIZONTAL,0,0);
//					toast.show();
//					//测试
//
//
//
//					Bitmap bmp0 = BitmapFactory.decodeFile(pathtri+"/triple/cut/0.jpg");
//					Bitmap bmp1 = BitmapFactory.decodeFile(pathtri+"/triple/cut/1.jpg");
//					Bitmap bmp2 = BitmapFactory.decodeFile(pathtri+"/triple/cut/2.jpg");
//					imageViewOne.setImageBitmap(bmp0);
////					imageViewTwo.setImageBitmap(bmp1);
////					imageViewThree.setImageBitmap(bmp2);
//
//
//					break;
//				case R.id.newbutton:
//					Intent intent = new Intent();
//					intent.setClass(Main.this,Ak.class);
//					startActivity(intent);
//					break;
				case R.id.dishimoshi:
				if (dishib==0){

					shibie.setText("非绿叶蔬菜类");
					shibie.setTextColor(Color.YELLOW);
					shibie.setTextSize(29);

					gaoshi.setImageResource(R.drawable.highhumidity);
					dishi.setImageResource(R.drawable.lowhumidity_p);
					zhongshi.setImageResource(R.drawable.mediumhumidity);
					mNumberAnimTextView2.setTextColor(Color.YELLOW);
					mNumberAnimTextView2.setDuration(800);
					mNumberAnimTextView2.setPostfixString("%");
					if (zhongshib==1){

					mNumberAnimTextView2.setNumberString("70", "50");}
					if (gaoshib==1){

						mNumberAnimTextView2.setNumberString("90", "50");}
					dishib=1;
					zhongshib=0;
					gaoshib=0;
				}


					break;
				case R.id.zhongshimoshi:
					if (zhongshib==0){

						shibie.setText("特定蔬菜类");
						shibie.setTextColor(Color.LTGRAY);
						shibie.setTextSize(29);
						gaoshi.setImageResource(R.drawable.highhumidity);
						dishi.setImageResource(R.drawable.lowhumidity);
						zhongshi.setImageResource(R.drawable.mediumhumidity_p);
						mNumberAnimTextView2.setTextColor(Color.LTGRAY);
						mNumberAnimTextView2.setDuration(800);
						mNumberAnimTextView2.setPostfixString("%");
						if (dishib==1){

							mNumberAnimTextView2.setNumberString("50", "70");}
						if (gaoshib==1){

							mNumberAnimTextView2.setNumberString("90", "70");}
						dishib=0;
						zhongshib=1;
						gaoshib=0;
					}

					break;
				case R.id.gaoshimoshi:
					if (gaoshib==0){

						shibie.setText("绿叶蔬菜类");
						shibie.setTextColor(Color.GREEN);
						shibie.setTextSize(29);
						gaoshi.setImageResource(R.drawable.highhumidity_p);
						dishi.setImageResource(R.drawable.lowhumidity);
						zhongshi.setImageResource(R.drawable.mediumhumidity);
						mNumberAnimTextView2.setTextColor(Color.GREEN);
						mNumberAnimTextView2.setDuration(800);
						mNumberAnimTextView2.setPostfixString("%");
						if (dishib==1){

							mNumberAnimTextView2.setNumberString("50", "90");}
						if (zhongshib==1){

							mNumberAnimTextView2.setNumberString("70", "90");}
						dishib=0;
						zhongshib=0;
						gaoshib=1;
					}

					break;

			}
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		cp = new CameraPreview(this);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.activity_main);

		tempbuttonButton[0] = new Button(Main.this);
		tempbuttonButton[1] = new Button(Main.this);
		tempbuttonButton[2] = new Button(Main.this);
		tempbuttonButton[3] = new Button(Main.this);
		tempbuttonButton[4] = new Button(Main.this);
		tempbuttonButton[5] = new Button(Main.this);
		tempbuttonButton[6] = new Button(Main.this);




//		textShow = (TextView) findViewById(R.id.tv_main_show);
		shibie=findViewById(R.id.shibie);
		SerialPortUtil.openSrialPort();

		Log.i("webcam", "start.");
//		cameraPreview = (CameraPreview) findViewById(R.id.preview_suf1);
//		imageView0 = findViewById(R.id.cut);

//		close_btn=findViewById(R.id.bangzhu);
		bangzhu=findViewById(R.id.bangzhu);

		bangzhu.setOnClickListener(myBtnClicker);
//		delete_current_btn=(Button)findViewById(R.id.delete_current_btn);
//		cpatureBtn = (Button) findViewById(R.id.capture_btn);
//		log_text = (TextView) findViewById(R.id.log_text);





        imageViewOne = (ImageView) findViewById(R.id.id_image_one);
		gaoshi=findViewById(R.id.gaoshimoshi);
		dishi=findViewById(R.id.dishimoshi);
		zhongshi=findViewById(R.id.zhongshimoshi);
		gaoshi.setOnClickListener(myBtnClicker);
		zhongshi.setOnClickListener(myBtnClicker);
		dishi.setOnClickListener(myBtnClicker);
//        imageViewTwo = (ImageView) findViewById(R.id.id_image_two);
//        imageViewThree = (ImageView) findViewById(R.id.id_image_three);
		cameraPreview = CameraPreview.getInstance();
		cameraPreview.otgPowerstateSet(true);
		metacamData = new byte[IMG_WIDTH * IMG_HEIGHT * 2];




		//食品识别
		llLayout = (RelativeLayout)findViewById(R.id.RelativeLayout1);

		iconName_CN = this.getResources().getStringArray(R.array.fruitname_new);
		iconName_EN = this.getResources().getStringArray(R.array.fruitname_new_en);
		TypedArray ar = this.getResources().obtainTypedArray(R.array.fruit_icon);
		int len = ar.length();
		top = new ArrayList<>();
		middle = new ArrayList<>();
		bottom = new ArrayList<>();
		icon = new int[len];
		for (int i = 0; i < len; i++) {
			icon[i] = ar.getResourceId(i, 0);
		}
		ar.recycle();
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		width = dm.widthPixels;
		height = dm.heightPixels;




//        textdisplay=findViewById(R.id.textview);

		url = this.getResources().getString(R.string.url);
		mSingleQueue = Volley.newRequestQueue(this, new MultiPartStack());

//		Toast.makeText(Main.this,"识别开始-onCreat",Toast.LENGTH_SHORT).show();


//GetResponse();

		mNumberAnimTextView2 = (NumberAnimTextView) findViewById(shidu);


		popView= LayoutInflater.from(Main.this).inflate(R.layout.cztest_popwin, null);
//        MainActivity.this.popView = inflater.inflate(
//                R.layout.cztest_popwin, null); // 读取布局管理器
		popWin = new PopupWindow(popView,
				ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true); // 实例化PopupWindow
		// 设置PopupWindow的弹出和消失效果
//        MainActivity.this.popWin
//                .setAnimationStyle(R.style.popupAnimation);

		popWin.setAnimationStyle(R.style.popupAnimation);

	}

	public  void threadGetPic(){

		new Thread(new Runnable() {
			@Override
			public void run() {
				isClick = true;
				mhandler.post(new Runnable() {
					@Override
					public void run() {

//						cpatureBtn.setText("抓拍中..");
//						log_text.setText("");
					}
				});
				getPic();
				mhandler.post(new Runnable() {
					@Override
					public void run() {
//						cpatureBtn.setText("拍照");
//						log_text.setText(strTime);

					}
				});
				isClick = false;


			}
		}).start();

//		Message message = new Message();
//		message.what = 1;
//		handler.sendMessage(message);
	}

	private  void getPic(){
		strTime = "";
//		for (int i =0; i<3 ;i++){
			Log.v(dwins," prepareCamera");
			long p1 = System.currentTimeMillis();
			int ret = cameraPreview.prepareCameraWithBase(i, 0);
			prepareCameraTime = System.currentTimeMillis() - p1;
			strTime =  strTime +"摄像头"+(i+1)+"准备时长："+prepareCameraTime +"ms \n";
			Log.v(dwins," prepareCamera done");
			if(ret != -1){
				Log.v(dwins," takepicture");
				p1 = System.currentTimeMillis();
			//	for(int framecount =16;framecount>0;framecount--)
				 cameraPreview.takepicture(metacamData);

				takePicTime = System.currentTimeMillis() - p1;
				strTime = strTime +" 抓拍用时："+ takePicTime + "ms \n";
				Log.v(dwins," takepicture done");
				if(metacamData!=null){
					PictureManger pictureManger = new PictureManger(mhandler,metacamData,i);
					ImageView imageView = null;
					switch (i){
						case 0:
							imageView = imageViewOne;
							break;
//						case 1:
//							imageView = imageViewTwo;
//							break;
//						case 2:
//							imageView = imageViewThree;
//							break;
					}
					if(imageView!=null)
						pictureManger.done(imageView);
				}
			}
            Log.v(dwins," ret:"+ret);
			cameraPreview.stopCamera();

		Message message = new Message();
		message.what = 1;
		handler.sendMessage(message);
	}

	private void init() {
//		editInput = (EditText) findViewById(R.id.et_main_input);
//		btnSend = (Button) findViewById(R.id.btn_main_send);
//		textShow = (TextView) findViewById(R.id.tv_main_show);
//
//		SerialPortUtil.openSrialPort();


	}

	private void onSend() {
		String input = editInput.getText().toString();
		if(TextUtils.isEmpty(input)){
			editInput.setError("要发送的数据不能为空");
			return;
		}
		SerialPortUtil.sendSerialPort(input);
	}

	/**
	 * 刷新UI界面
	 * @param data 接收到的串口数据
	 */
	public static void refreshTextView(String data){
		//  textShow.setText(textShow.getText().toString()+";"+data);
//		textShow.setText(data);
		int kai = Integer.parseInt(data);
		Log.i("snake", "接收到:" + kai);
		if(current == 1 && kai == 0){
//Main main=new Main();
//			main.threadGetPic();
//			textShow.setTextColor(android.graphics.Color.BLUE);
			if (!isClick){
//				showExitDialog01();
				Main pic = new Main();
				pic.threadGetPic();

			open=true;

//				pic.showExitDialog01();
			}


		}
		current=kai;

	}

public void  showExitDialog01() {

	AlertDialog.Builder builder = new AlertDialog.Builder(Main.this);
	builder.setTitle("提示");
	builder.setMessage("你是否要退出应用？");
	builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
		@Override
		public void onClick(DialogInterface dialogInterface, int i) {
//			BU.showToast(Main.this, "你点击了取消按钮");
		}
	});
	builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
		@Override
		public void onClick(DialogInterface dialogInterface, int i) {
//			ToastUtils.showToast(Main.this, "你点击了确定按钮");
		}
	});
	builder.create().show();

}





	/*******************************************************************/
	/**
	 *<p>获取指定文件的服务器地址</p>
	 * @return 服务器地址
	 */
	public String GetURL(){
		//默认IP地址如下
		String res = this.getResources().getString(R.string.url);
		return res;
	}
	private void GetResponse(){
		Log.e("Response","刷新");
		url = "http://27.223.99.146:8000/recognition/reg";
//        if(url.equals("")||url==null){
//		url = GetURL();
//            showInputDialog();
//        }
//		showFile();

		String path1 = "/triple/" + "video0.jpg";
		File file1 = new File(filepath + path1);

//		file = new File[]{file1};

		Map<String, File> files = new HashMap<String, File>();
		files.put("image", file1);
		Map<String, String> params = new HashMap<String, String>();
		addPutUploadFileRequest(files, params, mResonseListenerString,
				mErrorListener);
	}
	/****
	 * Volley上传
	 *
	 * @param files 要上传的文件
	 * @param params 附加参数
	 * @param responseListener 响应监听器
	 * @param errorListener 错误监听器
	 */
	//将图片上传到服务器
	public void addPutUploadFileRequest(final Map<String, File> files,
										final Map<String, String> params,
										final Response.Listener<String> responseListener,
										final Response.ErrorListener errorListener) {
		if (null == url || null == responseListener) {
			return;
		}

		MultiPartStringRequest multiPartRequest = new MultiPartStringRequest(
				Request.Method.POST, url, responseListener, errorListener) {

			@Override
			public Map<String, File> getFileUploads() {
				return files;
			}

			@Override
			public Map<String, String> getStringUploads() {
				return params;
			}

		};
		mSingleQueue.add(multiPartRequest);

	}

	private List<Food> parseStrJson(String jsonStr){
		List<Food> foods=null;
		try {
			JSONObject jObj = new JSONObject(jsonStr);
			Iterator<String> imgNames = jObj.keys();
			while (imgNames.hasNext()){
				String imgName = imgNames.next();
				JSONArray imgArr = jObj.getJSONArray(imgName);
				foods = new ArrayList<>(imgArr.length());
				for(int i=0;i<imgArr.length();i++){
					JSONObject object = imgArr.getJSONObject(i);
					Food food = new Food();
					food.setFoodName(object.getString("food_name"));
					food.setX1(object.getString("x1"));
					food.setX2(object.getString("x2"));
					food.setY1(object.getString("y1"));
					food.setY2(object.getString("y2"));
					foods.add(food);
				}
            /*注意，这里使用break的原因是返回的json中只
            获取第一章解析的图片，而实际上也就只有一张图
            片的解析，这里这样做保险，但没什么意义*/
				break;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return foods;
	}
	//接收服务器返回信息，
	Response.Listener<String> mResonseListenerString = new Response.Listener<String>() {

		@Override
		public void onResponse(String response) {
			Log.e("fanhui", response);
//			Toast.makeText(Main.this,"识别开始-返回第一张图片识别结果",Toast.LENGTH_SHORT).show();




			try {
				if(top==null){
					top = new ArrayList<>();
				}
				if(top.size()>0){
					top.clear();
				}
				top = parseStrJson(response);

//				if(!tempbuttonButton[0].toString().equals(""))
//				{
					llLayout.removeView(tempbuttonButton[0]);
					llLayout.removeView(tempbuttonButton[1]);
					llLayout.removeView(tempbuttonButton[2]);
					llLayout.removeView(tempbuttonButton[3]);
					llLayout.removeView(tempbuttonButton[4]);
					llLayout.removeView(tempbuttonButton[5]);
					llLayout.removeView(tempbuttonButton[6]);
//				}



				for(Food f:top){
//					Map<String,Object> map = new HashMap<>();
//					int index = getIndex(f.getFoodName());
					// map.put("image",icon[index]);
					// map.put("text",iconName_EN[index]);
					/// maps.add(map);
//					tempbuttonButton = new Button(Main.this);

//					tempbuttonButton[q] = new Button(Main.this);

//					tempbuttonButton.setText(f.getFoodName());
					float x1=((Float.parseFloat(f.getX1())+(float)273))/(float)1.7;
					float y1=Float.parseFloat(f.getY1())/(float)3.3+(float)1404;

//					float x1=(Float.parseFloat(f.getX1()));
//					float y1=Float.parseFloat(f.getY1());
					Log.e("T", "111");

//					tempbuttonButton = new Button(Main.this);


					tempbuttonButton[q].setX(x1 );

					tempbuttonButton[q].setY(y1);
					tempbuttonButton[q].setWidth(190);
					tempbuttonButton[q].setHeight(30);
					if (f.getFoodName().equals("西红柿")){
						tempbuttonButton[q].setText(f.getFoodName()+"(3天"+"-50% RH)");
					}
					if (f.getFoodName().equals("西兰花")){
						tempbuttonButton[q].setText(f.getFoodName()+"(4天"+"-90% RH)");
					}


					tempbuttonButton[q].setTextColor(Color.parseColor("#19F0C8"));
					tempbuttonButton[q].setTextSize(17);
					tempbuttonButton[q].setBackgroundColor(Color.parseColor("#304349"));
					tempbuttonButton[q].getBackground().setAlpha(70);
					Log.e("T", String.valueOf(x1));
					Log.e("T", String.valueOf(y1));
//                    tempbuttonButton.setBackgroundColor(Color.TRANSPARENT);




//

					llLayout.addView(tempbuttonButton[q]);
					q++;


				}

				Bitmap bmp0 = BitmapFactory.decodeFile(filepath+"/triple/video0.jpg");
				imageViewOne.setImageBitmap(bmp0);

//				imageViewOne.setImageResource(R.drawable.zhenxian_on);
				if (response.contains("西红柿")&&response.contains("西兰花"))
				{
					shibie.setText("保存条件冲突，请手动设置模式");
					shibie.setTextSize(21);
					shibie.setTextColor(Color.RED);


//					if (zhongshib==0){
//
//						shibie.setText("保存特定蔬菜");
//						shibie.setTextColor(Color.LTGRAY);
//						gaoshi.setImageResource(R.drawable.highhumidity);
//						dishi.setImageResource(R.drawable.lowhumidity);
//						zhongshi.setImageResource(R.drawable.mediumhumidity_p);
//						mNumberAnimTextView2.setTextColor(Color.LTGRAY);
//						mNumberAnimTextView2.setDuration(800);
//						mNumberAnimTextView2.setPostfixString("%");
//						if (dishib==1){
//
//							mNumberAnimTextView2.setNumberString("40", "50");}
//						if (gaoshib==1){
//
//							mNumberAnimTextView2.setNumberString("60", "50");}
//
//					}
//					dishib=0;
//					zhongshib=1;
//					gaoshib=0;



				}else if (response.contains("西兰花")||response.contains("生菜")||response.contains("青椒")){


//					if (flage==false){
//
//						shibie.setText("识别结果为绿叶蔬菜类");
//						shibie.setTextColor(Color.GREEN);
//
//						dishi.setImageResource(R.drawable.lowhumidity);
//						gaoshi.setImageResource(R.drawable.highhumidity_p);
//
//						mNumberAnimTextView2.setTextColor(Color.GREEN);
//						mNumberAnimTextView2.setDuration(800);
//						mNumberAnimTextView2.setPostfixString("%");
//						mNumberAnimTextView2.setNumberString("40", "60");
//					}
//					flage=true;

					if (gaoshib==0){

						shibie.setText("绿叶蔬菜类");
						shibie.setTextColor(Color.GREEN);
						shibie.setTextSize(29);
						gaoshi.setImageResource(R.drawable.highhumidity_p);
						dishi.setImageResource(R.drawable.lowhumidity);
						zhongshi.setImageResource(R.drawable.mediumhumidity);
						mNumberAnimTextView2.setTextColor(Color.GREEN);
						mNumberAnimTextView2.setDuration(800);
						mNumberAnimTextView2.setPostfixString("%");
						if (dishib==1){

							mNumberAnimTextView2.setNumberString("50", "90");}
						if (zhongshib==1){

							mNumberAnimTextView2.setNumberString("70", "90");}
						dishib=0;
						zhongshib=0;
						gaoshib=1;
					}


				}else if (response.contains("西红柿")){



//					if (flage==true)
//					{
//						shibie.setText("识别结果为非绿叶蔬菜类");
//						shibie.setTextColor(Color.GRAY);
//						gaoshi.setImageResource(R.drawable.highhumidity);
//						dishi.setImageResource(R.drawable.lowhumidity_p);
//						zhongshi.setImageResource(R.drawable.mediumhumidity);
////
//
//						mNumberAnimTextView2.setTextColor(Color.GRAY);
//						mNumberAnimTextView2.setDuration(800);
//						mNumberAnimTextView2.setPostfixString("%");
//						mNumberAnimTextView2.setNumberString("60", "40");
//					}
//					flage=false;


					if (dishib==0){

						shibie.setText("非绿叶蔬菜类");
						shibie.setTextColor(Color.YELLOW);
						shibie.setTextSize(29);
						gaoshi.setImageResource(R.drawable.highhumidity);
						dishi.setImageResource(R.drawable.lowhumidity_p);
						zhongshi.setImageResource(R.drawable.mediumhumidity);
						mNumberAnimTextView2.setTextColor(Color.YELLOW);
						mNumberAnimTextView2.setDuration(800);
						mNumberAnimTextView2.setPostfixString("%");
						if (zhongshib==1){

							mNumberAnimTextView2.setNumberString("70", "50");}
						if (gaoshib==1){

							mNumberAnimTextView2.setNumberString("90", "50");}
						dishib=1;
						zhongshib=0;
						gaoshib=0;
					}


				}




			} catch (Exception e) {
				System.out.println("Something wrong...");
				e.printStackTrace();
			}
		}
	};


	Response.ErrorListener mErrorListener = new Response.ErrorListener() {

		@Override
		public void onErrorResponse(VolleyError error) {
			Log.e("识别错误", "sdsdsd"+error);
//			Toast.makeText(Main.this,"识别故障",Toast.LENGTH_SHORT).show();
//			Toast.makeText(Main.this,error.toString(),Toast.LENGTH_SHORT).show();

			if (error != null) {
				if (error.networkResponse != null) {
//					Toast.makeText(Main.this,"Recognize error response",Toast.LENGTH_SHORT).show();
				}
			}
		}
	};
	private void DelFiles(File file){
		if (file.isFile()) {
			file.delete();
			return;
		}
		if(file.isDirectory()) {
			File[] childFiles = file.listFiles();
			if (childFiles == null || childFiles.length == 0) {
				//file.delete();
				return;
			}
			for (int i = 0; i < childFiles.length; i++) {
				DelFiles(childFiles[i]);
			}
			//file.delete();
		}
	}
	//删除cut下面的文件
	private boolean DelFile(File filepath){
		if(!filepath.exists()){
			try {
				filepath.mkdirs();
			}catch (Exception e){
				e.printStackTrace();
			}
			return false;
		}
		DelFiles(filepath);
		return true;
	}
	//剪切固定大小的图片
	private Bitmap CutImage(Bitmap bitmap) {
		if (bitmap == null) {
			Log.e("T", "kongtu");
			return null;
		}
		if (Math.min(bitmap.getHeight(), bitmap.getWidth()) > 380) {
			Bitmap result = null;
			int height = bitmap.getHeight();
			int width = bitmap.getWidth();
			if (height > width) {
				result = Bitmap.createBitmap(bitmap, 180, 0, 380, 1280);
			} else {
				result = Bitmap.createBitmap(bitmap, 0, 180, 1280, 380);
			}
			return result;
		} else {
			return bitmap;
		}
	}
	private static void makeRoot(String filePath) {
		File file = null;
		try {
			file = new File(filePath);
			if (!file.exists()){
				file.mkdirs();
			}/*else if(!file.isDirectory()){
                file.delete();
                file = new File(filePath);
                file.mkdirs();
            }*/
		} catch (Exception e) {

		}
	}
	private static File creatfile(String path, String path1) {
		File file = null;
		makeRoot(path1);
		try {
			file = new File(path1+path);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return file;
	}

	//将裁剪后的图片重新保存在SD卡中
	private void saveBitmap(Bitmap bmp, String filename, String filepath) {
		Log.e("Tag", "save image");
		String path =filename;//照片文件
		Log.e("裁剪文件",path);
		//String path1 = filepath + filepath;//照片路径
		File f = creatfile(path, pathtri);
		try {
			FileOutputStream out = new FileOutputStream(f);
			bmp.compress(Bitmap.CompressFormat.JPEG, 100/*这里更改图片质量，0最差，100最好*/, out);
			out.flush();
			out.close();
			Log.i("T", "saved");
		} catch (FileNotFoundException e) {
			Log.i("error", "error");
			e.printStackTrace();
			;
		} catch (IOException e) {
			e.printStackTrace();
			Log.i("error1", "error");
		}
	}
	//加载SD卡原始图片，前提是文件夹内存有图片
	private void resize(String filename, String filename1, String cut_path, String cut_file) {
		String path = filepath + filename;
		Bitmap bmp = null;
		Bitmap bb = BitmapFactory.decodeFile(path);
		bmp = CutImage(bb);
		saveBitmap(bmp, cut_file, cut_path);
	}
	//判断图片是否存在
	private Boolean BMPIsExists(String filename1, String filename2, String filename3){
		String path1 = filepath + filename1;
		String path2 = filepath + filename2;
		String path3 = filepath + filename3;
		File file1 = new File(path1);
		File file2 = new File(path2);
		File file3 = new File(path3);
		if(!file1.exists()||!file2.exists()||!file3.exists()){
//			Toast.makeText(this,"冰箱图片不存在！",Toast.LENGTH_SHORT).show();
			return false;
		}
		// Log.e("存在","EXIST");
		return true;
	}
	//当SD卡中不存在图片时，加载app内部图片
	private void resize_in(String filename, String filename1, String cut_path, String cut_file) {
		Bitmap bmp = null;
		Bitmap bb = BitmapFactory.decodeResource(this.getResources(),R.drawable.a0);
		bmp = CutImage(bb);
		saveBitmap(bmp, cut_file, cut_path);
	}
	//浏览本地图片并进行裁剪操作
	private void showFile() {
		// TODO Auto-generated method stub
		String path = "/triple/";//此为图片存放的文件夹
		String path1 = path + "video0.jpg";
		String path2 = path + "video1.jpg";
		String path3 = path + "video2.jpg";
		//将裁剪后的图片放在另一目录，防止替换原图片
		long time = System.currentTimeMillis();
		Time time1 = new Time(time);
		Date date = new Date(time);
		// Log.e("TIME",time1.toString());
		//Log.e("DATE",date.toString());
		String cut_path1 = "/triple/cut/";
		String cut_path2 = "/triple/cut/";
		String cut_path3 = "/triple/cut/";
		File dir = new File(pathtri+cut_path1);
		DelFile(dir);
        /*focus0 = Long.toString((long) (time%1231+Math.random()*100))+".jpg";
        focus1 = Long.toString((long) (time%23415433+Math.random()*125))+".jpg";
        focus2 = Long.toString((long) (time%308635+Math.random()*176))+".jpg";*/
//        focus0 = Long.toString(time)+"a.jpg";
//        focus1 = Long.toString(time)+"b.jpg";
//        focus2 = Long.toString(time)+"c.jpg";
		String cut_file1 = path + "cut/"+focus0;
		String cut_file2 = path + "cut/"+focus1;
		String cut_file3 = path + "cut/"+focus2;
		Log.e("name",cut_file1+","+cut_file2+","+cut_file3);
		if(BMPIsExists(path1,path2,path3)) {
			resize(path1, path, cut_path1, cut_file1);
			resize(path2, path, cut_path2, cut_file2);
			resize(path3, path, cut_path3, cut_file3);
			File file1 = new File(filepath + cut_file1);
			File file2 = new File(filepath + cut_file2);
			File file3 = new File(filepath + cut_file3);
			file = new File[]{file1, file2, file3};
		}else{
			resize_in(path1, path, cut_path1, cut_file1);
			resize_in(path2, path, cut_path2, cut_file2);
			resize_in(path3, path, cut_path3, cut_file3);
			File file1 = new File(filepath + cut_file1);
			File file2 = new File(filepath + cut_file2);
			File file3 = new File(filepath + cut_file3);
			file = new File[]{file1, file2, file3};
			Log.e("LOAD_IN","SD卡图片不存在！，加载内部图片");
		}
	}
	private List<Map<String, Object>> getData(List<Food> foods) {
		List<Map<String,Object>> maps = new ArrayList<>();
		if(!isZH()) {
			for(Food f:foods){
				Map<String,Object> map = new HashMap<>();
				int index = getIndex(f.getFoodName());
				map.put("image",icon[index]);
				map.put("text",iconName_EN[index]);
				maps.add(map);
			}
		}
		else{
			for(Food f:foods){
				Map<String,Object> map = new HashMap<>();
				int index = getIndex(f.getFoodName());
				map.put("image",icon[index]);
				map.put("text",iconName_CN[index]);
				maps.add(map);
			}
		}
		return maps;
	}

	private int getIndex(String foodName){
		int tmp=-1;
		for(int i=0;i<iconName_CN.length;i++){
			if(iconName_CN[i].equals(foodName)){
				tmp = i;
				break;
			}
		}
		if(tmp==-1){
			return 0;
		}
		return  tmp;
	}

	/**
	 *<p>判断是否在中国地区</p>
	 * @return 布尔类型，true:是，false:否
	 */
	public boolean isZH() {
		Locale locale = getResources().getConfiguration().locale;
		String language = locale.getLanguage();
		if (language.endsWith("zh")) {
			return true;
		} else {
			return false;
		}
	}


//	private void addButton()
//	{
//
//		for(Food f:top){
////					Map<String,Object> map = new HashMap<>();
////					int index = getIndex(f.getFoodName());
//					// map.put("image",icon[index]);
//					// map.put("text",iconName_EN[index]);
//					/// maps.add(map);
////					tempbuttonButton = new Button();
//
//
////					tempbuttonButton.setText(f.getFoodName());
//					float x1=((Float.parseFloat(f.getX1())+(float)273))/(float)1.7;
//					float y1=Float.parseFloat(f.getY1())/(float)1.7+(float)159;
//					Log.e("T", "111");
//
////					tempbuttonButton = new Button(Main.this);
//
//
//					tempbuttonButton.setX(x1 );
//
//					tempbuttonButton.setY(y1);
//					tempbuttonButton.setWidth(44);
//					tempbuttonButton.setHeight(30);
//					tempbuttonButton.setText(f.getFoodName());
//					tempbuttonButton.setTextColor(Color.parseColor("#19F0C8"));
//					tempbuttonButton.setBackgroundColor(Color.parseColor("#304349"));
//					tempbuttonButton.getBackground().setAlpha(70);
//					Log.e("T", String.valueOf(x1));
//					Log.e("T", String.valueOf(y1));
////                    tempbuttonButton.setBackgroundColor(Color.TRANSPARENT);
//
//
//
////
//
//					llLayout.addView(tempbuttonButton);
////					llLayout.removeView(tempbuttonButton);
//
//				}
//	}

}
