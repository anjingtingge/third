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

//dwins
//dwins

class CameraPreview  {

	//dwins
	private static final String UVC_NODE_ON = "on";
	private static final String UVC_NODE_OFF = "off";
	private static final String UVC_NODE_FILE = "/dev/UsbCamera";

	private static CameraPreview instance;

	static {
		System.loadLibrary("ImageProc");
	}

	public static synchronized CameraPreview getInstance() {
		if (instance == null) {
			instance = new CameraPreview();
		}
		return instance;
	}

	private CameraPreview() {
	}
	//dwins
    // JNI functions
    public native int prepareCamera(int videoid);
    public native int prepareCameraWithBase(int videoid, int camerabase);
    public native void processCamera(byte[] image);
    public native void stopCamera();
    public native void pixeltobmp(Bitmap bitmap);
	public native void takepicture( byte[] image);


	public void otgPowerstateSet(boolean open) {
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
}
