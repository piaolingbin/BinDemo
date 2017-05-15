package com.soufun.bin.bindemo.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

/**
 * @描述
 * @入口
 * @进入要传的值
 * @备注说明
 */

public class TCPService extends Service {
    private static final String TAG = "TCPService";
    private boolean mIsServiceDestoryed = false;
    private String[] mDefinedMessages = new String[]{
      "hello , boy!",
            "今天马刺输了！",
            "卡哇伊被渣渣垫脚",
            "马刺被25分翻盘了",
            "卡哇伊第三节被垫脚扭伤脚踝后，两罚全中，马刺还领先23分！",
            "然后小德伤退，马刺被翻盘！"
    };

    @Override
    public void onCreate() {
        new Thread(new TcpServer()).start();
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        mIsServiceDestoryed = true;
        super.onDestroy();
    }

    private class TcpServer implements Runnable{

        @SuppressWarnings("resource")
        @Override
        public void run() {
            ServerSocket serverSocket = null;
            try {
                serverSocket = new ServerSocket(8088);
            } catch (IOException e) {
                Log.d(TAG , "establish tcp server failed, port:8088");
                e.printStackTrace();
                return;
            }
            while(!mIsServiceDestoryed){
                try {
                    final Socket client = serverSocket.accept();
                    Log.d(TAG , "accept");
                    new Thread(){
                        @Override
                        public void run() {
                            try {
                                responseClient(client);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void responseClient(Socket client) throws IOException{
        // 用户接收客户端消息
        BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        // 用于向客户端发送消息
        PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(client.getOutputStream())) , true);
        out.println("欢迎来到NBA聊天室");
        while(!mIsServiceDestoryed){
            String str = in.readLine();
            Log.d(TAG , "msg from client :" + str);
            if(str == null){
                // 客户端断开连接
                break;
            }
            int i = new Random().nextInt(mDefinedMessages.length);
            String msg = mDefinedMessages[i];
            out.println(msg);
            Log.d(TAG , "服务端返回的消息：" + msg);
        }
        Log.d(TAG , "client quit");
        // 关闭流
        in.close();
        out.close();
        client.close();
    }
}
