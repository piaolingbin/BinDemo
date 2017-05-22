package com.soufun.bin.bindemo.activity;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.soufun.bin.bindemo.Book;
import com.soufun.bin.bindemo.IBookManger;
import com.soufun.bin.bindemo.IOnNewBookArrivedListener;
import com.soufun.bin.bindemo.R;
import com.soufun.bin.bindemo.utils.Utils;
import com.soufun.bin.bindemo.view.HorizontalScrollViewEx;
import com.soufun.bin.bindemo.view.HorizontalScrollViewEx2;
import com.soufun.bin.bindemo.view.ListViewEx;

import java.util.ArrayList;
import java.util.List;

public class BookManagerActivity extends AppCompatActivity {
    private static final String TAG = "BookManagerActivity";
    private static final int MSG_NEW_BOOK_ARRIVED = 1;
    private IBookManger mRemoteBooManager;
    private HorizontalScrollViewEx2 mListContainer;

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
        initView();
        // 绑定Service
        //Intent intent = new Intent(this , BookManagerService.class);
        //bindService(intent , mServiceConnection , Context.BIND_AUTO_CREATE);
    }

    private void initView() {
        LayoutInflater inflater = getLayoutInflater();
        mListContainer = (HorizontalScrollViewEx2) findViewById(R.id.container);
        final int screenWidth = Utils.getScreenMetrics(this).widthPixels;
        final int screenHeight = Utils.getScreenMetrics(this).heightPixels;
        for (int i = 0; i < 3; i++) {
            ViewGroup layout = (ViewGroup) inflater.inflate(
                    R.layout.content_layout2, mListContainer, false);
            layout.getLayoutParams().width = screenWidth;
            TextView textView = (TextView) layout.findViewById(R.id.title);
            textView.setText("page " + (i + 1));
            layout.setBackgroundColor(Color
                    .rgb(255 / (i + 1), 255 / (i + 1), 0));
            createList(layout);
            mListContainer.addView(layout);
        }
    }

    private void createList(ViewGroup layout) {
        ListViewEx listView = (ListViewEx) layout.findViewById(R.id.list);
        ArrayList<String> datas = new ArrayList<String>();
        for (int i = 0; i < 50; i++) {
            datas.add("name " + i);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.content_list_item, R.id.name, datas);
        listView.setAdapter(adapter);
        listView.setHorizontalScrollViewEx2(mListContainer);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(BookManagerActivity.this, "click item",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        /*if(mRemoteBooManager != null && mRemoteBooManager.asBinder().isBinderAlive()){
            try {
                Log.i(TAG , "注销。");
                mRemoteBooManager.unregisterOnNewBookArrivedListener(mIOnNewBookArrivedListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        unbindService(mServiceConnection);*/
        super.onDestroy();
    }
}
