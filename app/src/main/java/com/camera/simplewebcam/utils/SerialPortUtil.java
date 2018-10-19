package com.camera.simplewebcam.utils;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.camera.simplewebcam.Main;

import com.camera.simplewebcam.constants.IConstant;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android_serialport_api.SerialPort;

/**
 * Created by Administrator on 2017/6/6.
 */

public class SerialPortUtil {

    public static SerialPort serialPort = null;
    public static InputStream inputStream = null;
    public static OutputStream outputStream = null;

    public static Thread receiveThread = null;

    public static boolean flag = false;

    /**
     * 打开串口的方法
     */
    public static void openSrialPort(){
        Log.i("test","打开串口");
        try {
            serialPort = new SerialPort(new File("/dev/"+ IConstant.PORT),IConstant.BAUDRATE,0);
            //获取打开的串口中的输入输出流，以便于串口数据的收发
            inputStream = serialPort.getInputStream();
            outputStream = serialPort.getOutputStream();
            flag = true;
            receiveSerialPort();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *关闭串口的方法
     * 关闭串口中的输入输出流
     * 然后将flag的值设为flag，终止接收数据线程
     */
    public static void closeSerialPort(){
        Log.i("test","关闭串口");
        try {
            if(inputStream != null) {
                inputStream.close();
            }
            if(outputStream != null){
                outputStream.close();
            }
            flag = false;
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 发送串口数据的方法
     * @param data 要发送的数据
     */
    public static void sendSerialPort(String data){
        Log.i("test","发送串口数据");
        try {
            byte[] sendData = data.getBytes();
            Log.i("snake", "sendSerialPort sendData:" +  sendData[0]);
            outputStream.write(sendData);
            outputStream.flush();
            Log.i("test","串口数据发送成功");
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("test","串口数据发送失败");
        }
    }

    /**
     * 接收串口数据的方法
     */
    public static void receiveSerialPort(){
        Log.i("test","接收串口数据");
        if(receiveThread != null)
            return;
        /*定义一个handler对象要来接收子线程中接收到的数据
            并调用Activity中的刷新界面的方法更新UI界面
         */
        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                Main.refreshTextView(msg.getData().getString("data"));
            }
        };
        /*创建子线程接收串口数据
         */
        receiveThread = new Thread(){
            @Override
            public void run() {
                int door = 0;
                while (flag) {
                    try {
                        byte[] readData = new byte[32];
                        if (inputStream == null) {
                            return;
                        }
                        int size = inputStream.read(readData);

                        String HEX_CODE = "0123456789ABCDEF";

                        int _byteLen = readData.length;
                        StringBuilder _result = new StringBuilder(_byteLen * 2);
                        for (int i = 0; i < _byteLen; i++) {
                            int n = readData[i] & 0xFF;
                            _result.append(HEX_CODE.charAt(n >> 4));
                            _result.append(HEX_CODE.charAt(n & 0x0F));
                            if(i==5){
                                door= readData[i]&0x01;
                                String door1= String.valueOf(door);
//                                if (door1.equals("1")){
//                                    Main main=new Main();
//                                    main.threadGetPic();
//                                }
                            }

                        }



                        if (size>0 && flag) {


//                            String recinfo = new String (readData,"GB2312");
//                            Log.i("snake", "接收到串口数据ASK码:" + door);

//                            Log.i("snake", "接收到串口数据HEX:" +  result);
//                                if(recinfo.getBytes() == readData)
//                                Log.i("snake", "接收到串口数据size:" +  size);
//                            else
//                                Log.i("snake", "接收到串口数据recinfo.getBytes()[0] :" +  recinfo.getBytes()[0] );
//
//                            if (readData[0]==-12){
//                                Log.i("snake", "接收到串口数据HEX:0xf4");
//                            }else{
//                                Log.i("snake", "接收到串口数据HEX:issue");
//                            }
                            /*将接收到的数据封装进Message中，然后发送给主线程
                             */
                            Message message = new Message();
                            Bundle bundle = new Bundle();
                            bundle.putString("data", String.valueOf(door));
                            message.setData(bundle);
                            handler.sendMessage(message);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        //启动接收线程
        receiveThread.start();
    }

}
