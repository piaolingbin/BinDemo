package com.soufun.bin.bindemo.activity;

import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.soufun.bin.bindemo.BinderPool;
import com.soufun.bin.bindemo.ComputeImpl;
import com.soufun.bin.bindemo.ICompute;
import com.soufun.bin.bindemo.ISecurityCenter;
import com.soufun.bin.bindemo.R;
import com.soufun.bin.bindemo.SecurityCenterImpl;

public class BinderPoolActivity extends AppCompatActivity {
    private static final String TAG = "BinderPool";
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_binder_pool);
        btn = (Button) findViewById(R.id.btn_encrypt);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        doWork();
                    }
                }).start();
            }
        });
    }

    private void doWork() {
        // 创建Binder连接池
        BinderPool binderPool = BinderPool.getInstance(BinderPoolActivity.this);
        // 根据业务码获取相应的Binder
        IBinder securityBinder = binderPool.queryBinder(BinderPool.BINDER_SECURITY_CENTER);
        Log.d(TAG , "Activity-got securityBinder");
        ISecurityCenter mSecurityCenter = SecurityCenterImpl.asInterface(securityBinder);
        Log.d(TAG , "visit ISecurityBinder");
        String msg = "helloworld-安卓";
        Log.d(TAG , "content:" + msg);
        try {
            String password = mSecurityCenter.encrypt(msg);
            Log.d(TAG , "encrypt:" + password);
            Log.d(TAG , "decrypt:" + mSecurityCenter.decrypt(password));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        Log.d(TAG , "visit ICompute");
        IBinder computeBinder = binderPool.queryBinder(BinderPool.BINDER_COMPUTE);
        ICompute mCompute = ComputeImpl.asInterface(computeBinder);
        try {
            Log.d(TAG , "3+5=" + mCompute.add(3, 5));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
