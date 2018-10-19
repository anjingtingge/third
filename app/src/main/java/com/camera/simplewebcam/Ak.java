package com.camera.simplewebcam;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

//public class Ak extends Activity {
//    private RelativeLayout llLayout;
//    private Button tempbuttonButton;
//    private  ImageView back;
//    private String pathtri = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath();
//private ImageView imageView0,imageView1,imageView2;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//        setContentView(R.layout.activity_ak);
//        llLayout = (RelativeLayout) findViewById(R.id.RelativeLayout1);
//        tempbuttonButton = new Button(Ak.this);
//        tempbuttonButton.setText("添加");
//        tempbuttonButton.setX(200);
//        tempbuttonButton.setY(600);
//        tempbuttonButton.setWidth(60);
//        tempbuttonButton.setHeight(30);
//        tempbuttonButton.setText("苹果");
//        tempbuttonButton.setBackgroundColor(Color.parseColor("#F8F8FF"));
//        tempbuttonButton.getBackground().setAlpha(100);
//        llLayout.addView(tempbuttonButton);
//
//
//        imageView0=findViewById(R.id.cut0);
//        imageView1=findViewById(R.id.cut1);
//        imageView2=findViewById(R.id.cut2);
//        back=findViewById(R.id.back);
//        back.setOnClickListener(  new View.OnClickListener() {
//			@Override
//			public void onClick(View view) {
//				finish();
//			}
//		});
//
//
//        Bitmap bmp0 = BitmapFactory.decodeFile(pathtri+"/triple/cut/0.jpg");
//        Bitmap bmp1 = BitmapFactory.decodeFile(pathtri+"/triple/cut/1.jpg");
//        Bitmap bmp2 = BitmapFactory.decodeFile(pathtri+"/triple/cut/2.jpg");
//        imageView0.setImageBitmap(bmp0);
//        imageView1.setImageBitmap(bmp1);
//        imageView2.setImageBitmap(bmp2);
//
//
//    }
//}



import android.content.res.TypedArray;

import android.util.DisplayMetrics;
import android.util.Log;

import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import util.MultiPartStack;
import util.MultiPartStringRequest;

public class Ak extends Activity {
    private RelativeLayout llLayout;
    private Button tempbuttonButton;
    private List<Food> top,middle,bottom;
    private List<Map<String, Object>> data_list;
    private List<Map<String, Object>> data_list1;
    private List<Map<String, Object>> data_list2;
    //    private TextView textdisplay;
    public ImageView imageView0,imageView1,imageView2,caipu0,fanhui;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_ak);

        llLayout = (RelativeLayout) findViewById(R.id.RelativeLayout1);

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
        imageView0 = (ImageView)findViewById(R.id.cut0);
        imageView1 = (ImageView)findViewById(R.id.cut1);
        imageView2 = (ImageView)findViewById(R.id.cut2);
        fanhui = (ImageView)findViewById(R.id.back);

//        textdisplay=findViewById(R.id.textview);
        fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();

                //  Toast.makeText(Ak.this,"识别开始1",Toast.LENGTH_SHORT).show();
                //   finish();
            }
        });
        url = this.getResources().getString(R.string.url);
        mSingleQueue = Volley.newRequestQueue(this, new MultiPartStack());

        Toast.makeText(Ak.this,"识别开始-onCreat",Toast.LENGTH_SHORT).show();

        GetResponse();

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
        url = GetURL();
//        if(url.equals("")||url==null){
//            showInputDialog();
//        }
        showFile();
        Map<String, File> files = new HashMap<String, File>();
        files.put("image", file[0]);
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
            Toast.makeText(Ak.this,"识别开始-返回第一张图片识别结果",Toast.LENGTH_SHORT).show();
            try {
                if(top==null){
                    top = new ArrayList<>();
                }
                if(top.size()>0){
                    top.clear();
                }
                top = parseStrJson(response);
//                textdisplay.setText(response.toString());
                for(Food f:top){
                    Map<String,Object> map = new HashMap<>();
                    int index = getIndex(f.getFoodName());
                    // map.put("image",icon[index]);
                    // map.put("text",iconName_EN[index]);
                    /// maps.add(map);
                    tempbuttonButton = new Button(Ak.this);
                    tempbuttonButton.setText(f.getFoodName());
                    float x1=((Float.parseFloat(f.getX1())+(float)273))/(float)1.7;
                    float y1=Float.parseFloat(f.getY1())/(float)1.7+(float)159;
                    tempbuttonButton.setX(x1 );

                    tempbuttonButton.setY(y1);
                    tempbuttonButton.setWidth(44);
                    tempbuttonButton.setHeight(30);
                    tempbuttonButton.setText(f.getFoodName());
                    tempbuttonButton.setTextColor(Color.parseColor("#19F0C8"));
                    tempbuttonButton.setBackgroundColor(Color.parseColor("#304349"));
                    tempbuttonButton.getBackground().setAlpha(70);
//                    tempbuttonButton.setBackgroundColor(Color.TRANSPARENT);
                    llLayout.addView(tempbuttonButton);
                }

                /*上传第二张图片到服务器*/
                Map<String, File> files1 = new HashMap<String, File>();
                files1.put("image", file[1]);
                Map<String, String> params = new HashMap<String, String>();
                addPutUploadFileRequest(files1, params, mResonseListenerString1,
                        mErrorListener);
            } catch (Exception e) {
                System.out.println("Something wrong...");
                e.printStackTrace();
            }
        }
    };
    Response.Listener<String> mResonseListenerString1 = new Response.Listener<String>() {

        @Override
        public void onResponse(String response) {

            Toast.makeText(Ak.this,"识别开始-返回第二张图片识别结果",Toast.LENGTH_SHORT).show();
            Log.e("fanhui", response);
            try {
                if(middle==null){
                    middle = new ArrayList<>();
                }
                if(middle.size()>0){
                    middle.clear();
                }
                middle = parseStrJson(response);

                for(Food f:middle){
                    Map<String,Object> map = new HashMap<>();
                    int index = getIndex(f.getFoodName());
                    // map.put("image",icon[index]);
                    // map.put("text",iconName_EN[index]);
                    /// maps.add(map);
                    tempbuttonButton = new Button(Ak.this);
                    tempbuttonButton.setText(f.getFoodName());
                    float x1=((Float.parseFloat(f.getX1())+(float)273))/(float)1.7;
                    float y1=Float.parseFloat(f.getY1())/(float)1.7+(float)400;
                    tempbuttonButton.setX( x1);

                    tempbuttonButton.setY(y1);
                    tempbuttonButton.setWidth(44);
                    tempbuttonButton.setHeight(30);
                    tempbuttonButton.setText(f.getFoodName());
                    tempbuttonButton.setTextColor(Color.parseColor("#19F0C8"));
                    tempbuttonButton.setBackgroundColor(Color.parseColor("#304349"));
                    tempbuttonButton.getBackground().setAlpha(70);
//                    tempbuttonButton.setBackgroundColor(Color.TRANSPARENT);
                    llLayout.addView(tempbuttonButton);
                }
//                textdisplay.setText(response.toString());
                Map<String, File> files2 = new HashMap<String, File>();
                files2.put("image", file[2]);
                Map<String, String> params = new HashMap<String, String>();
                addPutUploadFileRequest(files2, params, mResonseListenerString2,
                        mErrorListener);
            } catch (Exception e) {
                System.out.println("Something wrong...");
                e.printStackTrace();
            }
            // Log.i(TAG + "2", response.toString());
        }
    };
    Response.Listener<String> mResonseListenerString2 = new Response.Listener<String>() {

        @Override
        public void onResponse(String response) {
            //  Log.e("fanhui", response);
            Toast.makeText(Ak.this,"识别开始-返回第三张图片识别结果",Toast.LENGTH_SHORT).show();
            try {
                if(bottom==null){
                    bottom = new ArrayList<>();
                }
                if(bottom.size()>0){
                    bottom.clear();
                }
                bottom = parseStrJson(response);
//                textdisplay.setText(response.toString());
                for(Food f:bottom){
                    Map<String,Object> map = new HashMap<>();
                    int index = getIndex(f.getFoodName());
                    // map.put("image",icon[index]);
                    // map.put("text",iconName_EN[index]);
                    /// maps.add(map);
                    tempbuttonButton = new Button(Ak.this);
                    tempbuttonButton.setText(f.getFoodName());
                    float x1=((Float.parseFloat(f.getX1())+(float)273))/(float)1.7;
                    float y1=Float.parseFloat(f.getY1())/(float)1.7+(float)650;
                    tempbuttonButton.setX( x1);
//                    tempbuttonButton.setY(Float.parseFloat(f.getY1())+(float)760);
                    tempbuttonButton.setY(y1);
                    tempbuttonButton.setWidth(44);
                    tempbuttonButton.setHeight(30);
                    tempbuttonButton.setText(f.getFoodName());
                    tempbuttonButton.setTextColor(Color.parseColor("#19F0C8"));
                    tempbuttonButton.setBackgroundColor(Color.parseColor("#304349"));
                    tempbuttonButton.getBackground().setAlpha(70);
//                    tempbuttonButton.setBackgroundColor(Color.TRANSPARENT);
                    llLayout.addView(tempbuttonButton);
                }
            } catch (Exception e) {
                System.out.println("Something wrong...");
                e.printStackTrace();
            }
//            String url_txt = pathtri+"/triple/"+PIC_INFO_NAME;
//            File file = new File(url_txt);
//            if(!file.exists()){
//                try{
//                    file.createNewFile();
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
//            }
            Toast.makeText(Ak.this,"识别完成！",Toast.LENGTH_SHORT).show();
            // toast.setGravity(Gravity.CENTER_HORIZONTAL,0,0);
            //toast.show();
            Bitmap bmp0 = BitmapFactory.decodeFile(pathtri+"/triple/cut/0.jpg");
            Bitmap bmp1 = BitmapFactory.decodeFile(pathtri+"/triple/cut/1.jpg");
            Bitmap bmp2 = BitmapFactory.decodeFile(pathtri+"/triple/cut/2.jpg");
            imageView0.setImageBitmap(bmp0);
            imageView1.setImageBitmap(bmp1);
            imageView2.setImageBitmap(bmp2);
//            caipu0.setImageBitmap(bmp2);
            // IsFinished = true;
            // DispBtn();

        }
    };
    Response.ErrorListener mErrorListener = new Response.ErrorListener() {

        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("erer", "sdsdsd"+error);
            Toast.makeText(Ak.this,"识别故障",Toast.LENGTH_SHORT).show();
            Toast.makeText(Ak.this,error.toString(),Toast.LENGTH_SHORT).show();

            if (error != null) {
                if (error.networkResponse != null) {
                    Toast.makeText(Ak.this,"Recognize error response",Toast.LENGTH_SHORT).show();
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
            Toast.makeText(this,"冰箱图片不存在！",Toast.LENGTH_SHORT).show();
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
}
