package com.camera.simplewebcam;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by zhangzhiming1 on 2017/7/18.
 */

public class CameraSurfaceView extends SurfaceView implements SurfaceHolder.Callback ,Runnable{
    static final String tag = "CameraPreview";
    static final String dwins = "dwins";
    private static final boolean DEBUG = true;
    protected Context context;
    private SurfaceHolder holder;
    Thread mainLoop = null;
    private Bitmap bmp=null;

    private boolean cameraExists=false;
    private boolean shouldStop=false;

    private int cameraId=2;
    private int cameraBase=0;

    static final int IMG_WIDTH=1280;
    static final int IMG_HEIGHT=720;

    private int winWidth=0;
    private int winHeight=0;
    private Rect rect;
    private int dw, dh;
    private float rate;
    //dwins
    private static final String UVC_NODE_ON = "on";
    private static final String UVC_NODE_OFF = "off";
    private static final String UVC_NODE_FILE = "/dev/UsbCamera";
    /**
     * camera buffer data
     */
    private byte[] metacamData;
    private byte[] mImgData;
    /**
     * root directory of usb camera image save path
     */
    public static final String JD_PIC_PATH = Environment
            .getExternalStorageDirectory().getPath() + "/DCIM/triple";

    public CameraSurfaceView(Context context) {
        this(context,null);
    }

    public CameraSurfaceView(Context context, AttributeSet attrs) {
        this(context,attrs,0);
    }

    public CameraSurfaceView(Context context, AttributeSet attrs, int defStyle) {
        super(context,attrs,defStyle);
        this.context = context;
        String tag = this.getTag().toString();
        if(tag!=null){
            cameraId =  Integer.valueOf(tag);
        }
        if(DEBUG) Log.d("WebCam","CameraPreview constructed");
        setFocusable(true);

        holder = getHolder();
        holder.addCallback(this);
        holder.setType(SurfaceHolder.SURFACE_TYPE_NORMAL);
    }

    public static void savePicData(byte[] data, int devid) {
        Log.i(dwins,"savePicData()");
        if (null == data) {
            Log.e(dwins,"savePicData(), data[] is null");
            return;
        }

        File fdir = new File(JD_PIC_PATH);
        if (!fdir.exists()) {
            Log.i(dwins,"fdir.exists()="+fdir.exists());
            fdir.mkdirs();
        }

        File f = new File(JD_PIC_PATH + "/" + Integer.toString(devid)
                + ".jpg");
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
            if (img.compress(Bitmap.CompressFormat.JPEG, 90, fOut)) {
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
    }
    int framecount=6;
    @Override
    public void run() {

        while (true && cameraExists) {
            Log.i(tag, "winWidth="+winWidth);
            Log.i(tag, "winHeight="+winHeight);
            if(winWidth==0){
                winWidth=this.getWidth();
                winHeight=this.getHeight();

                if(winWidth*3/4<=winHeight){
                    dw = 0;
                    dh = (winHeight-winWidth*3/4)/2;
                    rate = ((float)winWidth)/IMG_WIDTH;
                    rect = new Rect(dw,dh,dw+winWidth-1,dh+winWidth*3/4-1);
                }else{
                    dw = (winWidth-winHeight*4/3)/2;
                    dh = 0;
                    rate = ((float)winHeight)/IMG_HEIGHT;
                    rect = new Rect(dw,dh,dw+winHeight*4/3 -1,dh+winHeight-1);
                }
            }
            Log.i(tag, "rect.width()="+rect.width());
            Log.i(tag, "rect.height()="+rect.height());
/*dwins take picture*/
//			takepicture(metacamData);
            //if (null != metacamData) {
            //	if (framecount == 0) {
//					savePicData(metacamData, cameraId);
            //		framecount = 6;
            //	} else
            //	framecount--;
            //}
            //	else {
            //		//savePicData(metacamData, cameraId);
            //	Log.i(tag, "metacamData NUL");
            //	break;
            //	}

//*/

            // obtaining a camera image (pixel data are stored in an array in JNI).
            CameraPreview.getInstance().processCamera(metacamData);//获取一帧摄像头图像数据，比如320*240 YUV数据，然后保存在rgb
            // camera image to bmp
            Bitmap img = BitmapFactory.decodeByteArray(metacamData, 0, metacamData.length);
            //pixeltobmp(img);

            Canvas canvas = getHolder().lockCanvas();
            if (canvas != null)
            {
            	/*draw camera bmp on canvas
				*Rect src: 是对图片进行裁截，若是空null则显示整个图片
				* RectF dst：是图片在Canvas画布中显示的区域
				* 大于src则把src的裁截区放大，小于src则把src的裁截区缩小。
				*/
                canvas.drawBitmap(img,null,rect,null);
                getHolder().unlockCanvasAndPost(canvas);
            }
            //if(true)
            //	break;
            if(shouldStop){
                shouldStop = false;
                Log.i(tag, "break");
                break;
            }
        }
        Log.i(tag, "线程退出12");
    }
    //dwins
    private void otgPowerstateSet(boolean open) {
        Log.d("WebCam", "otgPowerstateSet1");

        String updateState;
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(UVC_NODE_FILE);
            if (open) {
                updateState = UVC_NODE_ON;
            } else {
                updateState = UVC_NODE_OFF;
            }
            out.write(updateState.getBytes());
        } catch (IOException e) {
            Log.d("WebCam", "IOException2");
        } finally {
            try {
                if (null != out) {
                    out.close();
                }
            } catch (IOException e) {
                Log.d("WebCam", "IOException3");
            }
        }
    }
    //dwins
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if(DEBUG) Log.d("WebCam", "surfaceCreated");
        if(bmp==null){
            bmp = Bitmap.createBitmap(IMG_WIDTH, IMG_HEIGHT, Bitmap.Config.ARGB_8888);
        }
        otgPowerstateSet(true);//open the uvc device
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int ret = CameraPreview.getInstance().prepareCameraWithBase(cameraId, cameraBase);
        Log.d(tag, "ret="+ret);
        if(ret!=-1) cameraExists = true;
        metacamData = new byte[IMG_WIDTH * IMG_HEIGHT * 2];
        mImgData = new byte[IMG_WIDTH * IMG_HEIGHT * 2];
        mainLoop = new Thread(this);
        mainLoop.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if(DEBUG) Log.d("WebCam", "surfaceChanged");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if(DEBUG) Log.d("WebCam", "surfaceDestroyed");
        //otgPowerstateSet(false);
        if(cameraExists){
            shouldStop = true;
            while(shouldStop){
                try{
                    Thread.sleep(100); // wait for thread stopping
                }catch(Exception e){}
            }
        }
        CameraPreview.getInstance().stopCamera();
    }

    public void savePic(){
        CameraPreview.getInstance().takepicture(metacamData);
        savePicData(metacamData, cameraId);
    }
}
