package com.soufun.bin.bindemo.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.Log;

import com.soufun.bin.bindemo.Book;
import com.soufun.bin.bindemo.IBookManger;
import com.soufun.bin.bindemo.IOnNewBookArrivedListener;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;


public class BookManagerService extends Service {

    private static final String TAG = "BookManagerService";
    private CopyOnWriteArrayList<Book> mBookList = new CopyOnWriteArrayList<>();
    private AtomicBoolean mIsServiceDestroyed = new AtomicBoolean(false);
    // 监听列表 模拟多个用户
    //private CopyOnWriteArrayList<IOnNewBookArrivedListener> listeners = new CopyOnWriteArrayList<>();
    private RemoteCallbackList<IOnNewBookArrivedListener> mRemoteCallbackList = new RemoteCallbackList<>();

    // 创建binder 通过AIDL接口
    private Binder binder = new IBookManger.Stub() {
        @Override
        public List<Book> getBookList() throws RemoteException {
            return mBookList;
        }

        @Override
        public void addBook(Book book) throws RemoteException {
            mBookList.add(book);
        }

        @Override
        public void registerOnNewBookArrivedListener(IOnNewBookArrivedListener listener) throws RemoteException {
            /*if(!listeners.contains(listener)){
                listeners.add(listener);
            } else {
                Log.i(TAG , "注册失败，此用户已注册。");
            }
            Log.i(TAG , "注册-已注册用户数：" + listeners.size());*/
            mRemoteCallbackList.register(listener);
            Log.i(TAG , "注册，现有用户数：" + mRemoteCallbackList.getRegisteredCallbackCount());
        }

        @Override
        public void unregisterOnNewBookArrivedListener(IOnNewBookArrivedListener listener) throws RemoteException {
            /*if(listeners.contains(listener)){
                listeners.remove(listener);
            } else {
                Log.i(TAG , "该用户尚未注册。");
            }
            Log.i(TAG , "注销-剩余用户数：" + listeners.size());*/
            mRemoteCallbackList.unregister(listener);
            Log.i(TAG , "注销，现有用户数：" + mRemoteCallbackList.getRegisteredCallbackCount());
        }

        @Override
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            return super.onTransact(code, data, reply, flags);
        }
    };

    @Override
    public void onCreate() {
        // 初始化数据
        mBookList.add(new Book(1 , "艺术探索"));
        mBookList.add(new Book(2 , "群英会"));
        // 每5秒添加一本新书
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(!mIsServiceDestroyed.get()){
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    // 添加新书
                    int bookID = mBookList.size() + 1;
                    Book newBook = new Book(bookID , "NewBook" + bookID);
                    mBookList.add(newBook);
                    // 挨个通知用户，新书已到
                    /*for(int i = 0 ; i < listeners.size() ; i++){
                        try {
                            listeners.get(i).onNewBookArrived(newBook);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }*/
                    int N = mRemoteCallbackList.beginBroadcast();
                    for(int i = 0 ; i < N ; i++){
                        IOnNewBookArrivedListener l = mRemoteCallbackList.getBroadcastItem(i);
                        if(l != null){
                            try {
                                l.onNewBookArrived(newBook);
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    mRemoteCallbackList.finishBroadcast();
                }
            }
        }).start();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onDestroy() {
        mIsServiceDestroyed.set(true);
        super.onDestroy();
    }
}
