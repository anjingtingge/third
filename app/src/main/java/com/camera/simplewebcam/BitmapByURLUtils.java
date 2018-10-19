package com.camera.simplewebcam;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * 根据存储路径获取图片并压缩的工具类
 */
public class BitmapByURLUtils {
    public static int be = 1;//be=1表示不缩放
    /**
     *根据路径来获取图片
     * @param path
     * @return
     */
//    public static Bitmap getBitMBitmap(String path) {
//        Bitmap bitmap=null;
//        try {
//            URL url = new URL(path);
//            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
//            conn.setConnectTimeout(5000);
//            conn.setRequestMethod("GET");
//            if(conn.getResponseCode() == 200){
//                InputStream inputStream = conn.getInputStream();
//                bitmap = BitmapFactory.decodeStream(inputStream);
//                return bitmap;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return bitmap;
//    }


    /**
     * 根据存储路径来获取对拍图片并等比压缩
     * @param
     * @return
     */
    public static Bitmap getImage(InputStream in, String srcPath, float width, float height)  {
        Bitmap bitmap = null;
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        bitmap = BitmapFactory.decodeStream(in,null,newOpts);//此时返回bm为空
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        float ww = width;//这里设置宽度, 根据默认对拍图的宽高
        float hh = height;//这里设置高度, 根据默认对拍图的宽高
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (w / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (h / hh);
        }
        if (be <= 0) {
            be = 1;
        }
        newOpts.inSampleSize = be;//设置缩放比例
        newOpts.inJustDecodeBounds = false;
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        try {
            in = new FileInputStream(new File(srcPath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        bitmap = BitmapFactory.decodeStream(in, null, newOpts);
        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return compressImage(bitmap);//压缩好比例大小后再进行质量压缩
    }

//    public static Bitmap getimage1(InputStream in,String srcPath)  {
//        Bitmap bitmap = null;
//        BitmapFactory.Options newOpts = new BitmapFactory.Options();
//        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
//        newOpts.inJustDecodeBounds = true;
//        bitmap = BitmapFactory.decodeStream(in,null,newOpts);//此时返回bm为空
//        newOpts.inJustDecodeBounds = false;
//        try {
//            in.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        int w = newOpts.outWidth;
//        int h = newOpts.outHeight;
//        float ww = 430;//这里设置宽度为348
//        float hh = 558;//这里设置高度为450
//        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
//
//        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
//            be = (int) (w / ww);
//            ratio=w/ww;
//        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
//            be = (int) (h / hh);
//            ratio=h/hh;
//        }
//        if (be <= 0) {
//            be = 1;
//        }
//        newOpts.inSampleSize = be;//设置缩放比例
//        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
//        try {
//            bitmap = BitmapFactory.decodeStream(new URL(srcPath).openStream(), null, newOpts);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return compressImage(bitmap);//压缩好比例大小后再进行质量压缩
//    }
    /**
     * 对图片进行质量压缩
     * @param image
     * @return
     */
    public static Bitmap compressImage(Bitmap image) {
        if (image!=null){
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
            int options = 100;
            while ( baos.toByteArray().length / 1024>100) {    //循环判断如果压缩后图片是否大于100kb,大于继续压缩
                baos.reset();//重置baos即清空baos
                options -= 10;//每次都减少10
                image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            }
            ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
            Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
            return bitmap;
        }else{
            return null;
        }
    }
//   public  interface  GetBitmapListener{
//        void getBitmap(Bitmap bitmap);
//    }
    /**
     * 修改url来压缩图片
     * @param imgUrl
     * @param width
     * @param height
     * @return
     */
    public static String resize(String imgUrl, int width, int height) {
        String DOMAIN="http://img30.360buyimg.com/";
        String IMG_APP_CODE="smartcloud";
        if (TextUtils.isEmpty(imgUrl)) {
            return null;
        }
        if (!imgUrl.startsWith(DOMAIN + IMG_APP_CODE)) {
            throw new IllegalArgumentException("invalid image url, imgUrl=" + imgUrl);
        }
        return DOMAIN + IMG_APP_CODE + "/" + "s" + width + "x" + height + "_" + imgUrl.substring(imgUrl.indexOf("jfs/"));
    }

    /**
     *
     * 获取图片的旋转角度
     * @param imgPath
     * @return
     */
//    public static int geImageRorateAngle(String imgPath) {
//
//        int angle = 0;
//        ExifInterface exif = null;
//        try {
//            exif = new ExifInterface(imgPath);
//        } catch (IOException e) {
//            e.printStackTrace();
//            exif = null;
//        }
//        if (exif != null) {
//
//            int ori = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
//
//            switch (ori) {
//                case ExifInterface.ORIENTATION_ROTATE_90:
//                    angle = 90;
//                    break;
//                case ExifInterface.ORIENTATION_ROTATE_180:
//                    angle = 180;
//                    break;
//                case ExifInterface.ORIENTATION_ROTATE_270:
//                    angle = 270;
//                    break;
//                default:
//                    angle = 270;
//                    break;
//            }
//        }
//        return angle;
//    }

//    public static Bitmap decodeSampledBitmapFromResource(String path, int reqWidth, int reqHeight, int angle) {
//        BitmapFactory.Options newOpts = new BitmapFactory.Options();
//        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
//        newOpts.inJustDecodeBounds = true;
//        Bitmap bitmap = BitmapFactory.decodeFile(path, newOpts);// 此时返回bm为空
//        JLog.d("srcPath========================", path);
//        newOpts.inJustDecodeBounds = false;
//        int w = newOpts.outWidth;
//        int h = newOpts.outHeight;
//        // 高和宽我们设置为
//        float hh = 960f;
//        float ww = 1280f;
//        // 缩放比，只用高或者宽其中一个数据进行计算即可
//        int be = 1;// be=1表示不缩放
//        if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
//            be = (int) (newOpts.outWidth / ww + 0.5f);
//        } else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
//            be = (int) (newOpts.outHeight / hh + 0.5f);
//        }
//        if (be <= 0)
//            be = 1;
//        newOpts.inSampleSize = be;// 设置缩放比例
//        bitmap = BitmapFactory.decodeFile(path, newOpts);
//        JLog.d("map=================", "" + bitmap);
//        return bitmap;
//    }

}
