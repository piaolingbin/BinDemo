package com.soufun.bin.bindemo.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.soufun.bin.bindemo.BinderPool;

public class BinderPoolService extends Service {

    private static final String TAG = "BinderPool";
    private Binder mBinderPool = new BinderPool.BinderPoolImpl();

    public BinderPoolService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG , "onBind");
        return mBinderPool;
    }
}
