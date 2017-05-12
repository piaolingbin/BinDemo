package com.soufun.bin.bindemo.service;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import com.soufun.bin.bindemo.utils.MyConstants;

/**
 * @描述
 * @入口
 * @进入要传的值
 * @备注说明
 */

public class MessengerService extends Service {

    private static final String TAG = "MessengerService";

    private Messenger service = new Messenger(new MessengerHandler());

    private static class MessengerHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what){
                case MyConstants.MSG_FROM_CLIENT:
                    Log.i(TAG , "message from client : " + msg.getData().get(MyConstants.MSG));
                    Message replyMessage = Message.obtain(null , MyConstants.MSG_FROM_SERVICE);
                    Bundle bundle = new Bundle();
                    bundle.putString(MyConstants.MSG , "OK , I have got the message!");
                    replyMessage.setData(bundle);
                    try {
                        msg.replyTo.send(replyMessage);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return service.getBinder();
    }
}
