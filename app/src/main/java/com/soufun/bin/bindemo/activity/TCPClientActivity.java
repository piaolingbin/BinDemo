package com.soufun.bin.bindemo.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.soufun.bin.bindemo.R;
import com.soufun.bin.bindemo.service.TCPService;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;
import static android.os.Build.VERSION_CODES.M;

public class TCPClientActivity extends AppCompatActivity implements View.OnClickListener{
    private static final int MESSAGE_RECEIVE_NEW_MSG = 1;
    private static final int MESSAGE_SOCKET_CONNECTED = 2;
    private static final String TAG = "TCPService";

    private Button mSendButton;
    private TextView mMessageTextView;
    private EditText mMessageEditText;

    private PrintWriter mPirntWriter;
    private Socket mClientSocket;


    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MESSAGE_RECEIVE_NEW_MSG:
                    mMessageTextView.setText(mMessageTextView.getText() + (String)msg.obj);
                    break;
                case MESSAGE_SOCKET_CONNECTED:
                    mSendButton.setEnabled(true);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tcpclient);
        initViews();
        Intent intent = new Intent(this , TCPService.class);
        startService(intent);
        new Thread(){
            @Override
            public void run() {
                connectTCPServer();
            }
        }.start();
    }

    @Override
    protected void onDestroy() {
        if(mClientSocket != null){
            try {
                mClientSocket.shutdownInput();
                mClientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onDestroy();
    }

    private void connectTCPServer() {
        Socket socket = null;
        while (socket == null && !isDestroyed()) {
            try {
                socket = new Socket("localhost", 8088);
                mClientSocket = socket;
                mPirntWriter = new PrintWriter(new BufferedWriter(
                        new OutputStreamWriter(socket.getOutputStream())), true);
                mHandler.sendEmptyMessage(MESSAGE_SOCKET_CONNECTED);
                Log.d(TAG, "connect server success");
            } catch (IOException e) {
                SystemClock.sleep(1000);
                Log.d(TAG, "connect tcp server failed, retry...");
            }
        }
        if (isDestroyed())return;
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while (!TCPClientActivity.this.isFinishing()) {
                String msg = br.readLine();
                Log.d(TAG, "receive:" + msg);
                if (msg != null) {
                    String time = formatDateTime(System.currentTimeMillis());
                    String showedMsg = "server " + time + ":" + msg + "\n";
                    mHandler.obtainMessage(MESSAGE_RECEIVE_NEW_MSG, showedMsg).sendToTarget();
                }
            }
            Log.d(TAG, "quit...");
            mPirntWriter.close();
            br.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initViews() {
        mMessageTextView = (TextView) findViewById(R.id.tv_message);
        mMessageEditText = (EditText) findViewById(R.id.et_send_message);
        mSendButton = (Button) findViewById(R.id.btn_send);
        mSendButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == mSendButton){
            String msg = mMessageEditText.getText().toString();
            if(!TextUtils.isEmpty(msg) && mPirntWriter != null){
                mPirntWriter.println(msg);
                mMessageEditText.setText("");
                String time = formatDateTime(System.currentTimeMillis());
                String showedMsg = "self" + time + ":" + msg + "\n";
                mMessageTextView.setText(mMessageTextView.getText() + showedMsg);
            }
        }
    }

    private String formatDateTime(long time) {
        return new SimpleDateFormat("(HH:mm:ss)").format(new Date(time));
    }
}
