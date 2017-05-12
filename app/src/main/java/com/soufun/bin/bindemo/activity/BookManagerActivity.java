package com.soufun.bin.bindemo.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.soufun.bin.bindemo.Book;
import com.soufun.bin.bindemo.IBookManger;
import com.soufun.bin.bindemo.IOnNewBookArrivedListener;
import com.soufun.bin.bindemo.R;
import com.soufun.bin.bindemo.service.BookManagerService;

import java.util.List;

public class BookManagerActivity extends AppCompatActivity {
    private static final String TAG = "BookManagerActivity";
    private static final int MSG_NEW_BOOK_ARRIVED = 1;
    private IBookManger mRemoteBooManager;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MSG_NEW_BOOK_ARRIVED:
                    Log.i(TAG , "新书 ： " + msg.obj + "已到");
                    break;
            }
        }
    };

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // 获取AIDL接口
            IBookManger mIBookManger = IBookManger.Stub.asInterface(service);
            // 通过AIDL接口调用服务中的方法
            try {
                mRemoteBooManager = mIBookManger;
                List<Book> bookList = mIBookManger.getBookList();
                Log.i(TAG , "List Type : " + bookList.getClass().getCanonicalName());
                Log.i(TAG , "Books : " + bookList.toString());
                mIBookManger.addBook(new Book(3 , "第一行代码"));
                List<Book> newBookList = mIBookManger.getBookList();
                Log.i(TAG , "List New Type : " + newBookList.getClass().getCanonicalName());
                Log.i(TAG , "New Books : " + newBookList.toString());
                // 有新书的时候能不能主动告诉我呢？ 注册监听
                mIBookManger.registerOnNewBookArrivedListener(mIOnNewBookArrivedListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    private IOnNewBookArrivedListener mIOnNewBookArrivedListener = new IOnNewBookArrivedListener.Stub() {
        @Override
        public void onNewBookArrived(Book newBook) throws RemoteException {
            // 因为这个方法的调用是在binder线程池中，如果要访问UI相关内容需要切换到UI线程操作
            // 如果不切换的话是可以做耗时操作的，但需要服务端做处理（将此方法运行在子线程），否则会造成服务端ANR
            mHandler.obtainMessage(MSG_NEW_BOOK_ARRIVED , newBook).sendToTarget();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_manager);
        // 绑定Service
        Intent intent = new Intent(this , BookManagerService.class);
        bindService(intent , mServiceConnection , Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        if(mRemoteBooManager != null && mRemoteBooManager.asBinder().isBinderAlive()){
            try {
                Log.i(TAG , "注销。");
                mRemoteBooManager.unregisterOnNewBookArrivedListener(mIOnNewBookArrivedListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        unbindService(mServiceConnection);
        super.onDestroy();
    }
}
