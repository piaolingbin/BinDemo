package com.soufun.bin.bindemo.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.soufun.bin.bindemo.service.MessengerService;
import com.soufun.bin.bindemo.utils.MyConstants;
import com.soufun.bin.bindemo.R;


public class MessengerActivity extends AppCompatActivity {

    private static final String TAG = "MessengerActivity";

    private Messenger replyMessenger = new Messenger(new MessengerHandler());

    private ServiceConnection serviceConnection = new ServiceConnection(){

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Messenger serviceMessenger = new android.os.Messenger(service);
            Message msg = Message.obtain(null , MyConstants.MSG_FROM_CLIENT);
            Bundle bundle = new Bundle();
            bundle.putString(MyConstants.MSG , "hello , this is client!");
            msg.setData(bundle);
            msg.replyTo = replyMessenger;
            try {
                serviceMessenger.send(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    private static class MessengerHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MyConstants.MSG_FROM_SERVICE:
                    Log.i(TAG , "message from service : " + msg.getData().getString(MyConstants.MSG));
                     break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messenger);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        Intent intent = new Intent(this , MessengerService.class);
        bindService(intent , serviceConnection , Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
