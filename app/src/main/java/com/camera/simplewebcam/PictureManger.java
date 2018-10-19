package com.camera.simplewebcam;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;

/**
 * Created by zhangzhiming1 on 2017/7/27.
 */

public class PictureManger {

    static final String dwins = "dwins";
    public static final String JD_PIC_PATH = Environment
            .getExternalStorageDirectory().getPath() + "/DCIM/triple";
    private byte[] data;
    private int devid;
    private Handler mhandler = null;
    private static String namePath1,namePath2,namePath3;

    public PictureManger(Handler mhandler,byte[] data,int devid){
        this.mhandler = mhandler;
        this.data = data;
        this.devid = devid;
    }

    public void done(final ImageView imageView){
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String name = savePicData(data,devid);
                saveName(name);

                mhandler.post(new Runnable() {
                    @Override
                    public void run() {
//                        showPic(name,imageView);
                    }
                });
            }
        }).start();
    }

    private void saveName(String name){
        name = JD_PIC_PATH +"/"+ name;
        switch (devid){
            case 0:
                namePath1 = name;
                break;
            case 1:
                namePath2 = name;
                break;
            case 2:
                namePath3 = name;
                break;
        }
    }

    private String savePicData(byte[] data, int devid) {
        Log.i(dwins,"savePicData()");
        if (null == data) {
            Log.e(dwins,"savePicData(), data[] is null");
            return "";
        }

        File fdir = new File(JD_PIC_PATH);
        if (!fdir.exists()) {
            Log.i(dwins,"fdir.exists()="+fdir.exists());
            fdir.mkdirs();
        }
        String name = "video"+Integer.toString(devid)+ ".jpg";
        String path = JD_PIC_PATH + "/"+name;
        File f = new File(path);
        if (f.exists()) {
            Log.i(dwins,"f.exists()="+fdir.exists());
            f.delete();
        }

        Bitmap img = BitmapFactory.decodeByteArray(data, 0, data.length);

        //	Log.i(dwins,"img.getWidth()="+img.getWidth());
        Log.i(dwins,"data.length="+data.length);
        Log.i(dwins,"JD_PIC_PATH="+JD_PIC_PATH);
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(f);
            Log.i(dwins,"fOut="+fOut);
            if(img == null){
                Log.i(dwins,"img==null");
                return "";
            }
            if (img.compress(Bitmap.CompressFormat.JPEG, 95, fOut)) {
                Log.i(dwins,"line121 90");
                fOut.flush();
            }
            //bm.compress(Bitmap.CompressFormat.JPEG, 30, baos);//30 是压缩率，表示压缩70%; 如果不压缩是100，表示压缩率为0
        } catch (FileNotFoundException e) {
            Log.e(dwins,"savePicData(), FileNotFoundException : ", e);
        } catch (IOException e) {
            Log.e(dwins,"savePicData(), IOException : ", e);
        } finally {
            try {
                if (null != fOut) {
                    fOut.close();
                }
            } catch (IOException e) {
                Log.e(dwins,"savePicData(), finally IOException : ", e);
            }
        }

        Log.i(dwins,"savePicData(), done");
        return name;
    }

    private void showPic(String name, ImageView imageView){
        if("".equals(name))return;
        Log.i(dwins,"name:"+name);
        WeakReference p0SoftRef = null;
        try {
            p0SoftRef = new WeakReference(BitmapByURLUtils.getImage(new FileInputStream(new File(JD_PIC_PATH, name)), JD_PIC_PATH +"/"+ name,432,243));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.e(dwins,e.toString());
        }
        Bitmap p0= (Bitmap)p0SoftRef.get();
        Log.v(dwins,"bitmap :"+p0);
        if(p0!=null){
            imageView.setImageBitmap(p0);
            Log.v(dwins,"imageView index :"+devid);
        }
    }

    public static void deleteAllPictures(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                deleteDirectory(JD_PIC_PATH);
            }
        }).start();
    }

    public static void deleteCurrentPictures(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(namePath1!=null)
                    deleteFile(namePath1);
                if(namePath2!=null) {
                    deleteFile(namePath2);
                }
                if(namePath3!=null) {
                    deleteFile(namePath3);
                }
            }
        }).start();
    }

    /**
     * 删除目录下的文件
     * @param   filePath 被删除目录的文件路径
     * @return  目录删除成功返回true，否则返回false
     */
    private static boolean deleteDirectory(String filePath) {
        boolean flag = false;
        //如果filePath不以文件分隔符结尾，自动添加文件分隔符
        if (!filePath.endsWith(File.separator)) {
            filePath = filePath + File.separator;
        }
        File dirFile = new File(filePath);
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }
        flag = true;
        File[] files = dirFile.listFiles();
        //遍历删除文件夹下的所有文件(包括子目录)
        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()) {
                //删除子文件
                flag = deleteFile(files[i].getAbsolutePath());
                if (!flag) break;
            } else {
                //删除子目录
                flag = deleteDirectory(files[i].getAbsolutePath());
                if (!flag) break;
            }
        }
        if (!flag) return false;
        //删除当前空目录
//        return dirFile.delete();
        return true;
    }


    /**
     * 删除单个文件
     * @param   filePath    被删除文件的文件名
     * @return 文件删除成功返回true，否则返回false
     */
    private static boolean deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.isFile() && file.exists()) {
            return file.delete();
        }
        return false;
    }
}
