package com.soufun.bin.bindemo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.soufun.bin.bindemo.R;
import com.soufun.bin.bindemo.adapter.HomeAdapter;
import com.soufun.bin.bindemo.utils.Utils;

public class MainActivity extends AppCompatActivity {


    private RecyclerView mRecyclerView;
    private TextView headerTitle;
    private HomeAdapter mAdapter;
    private LinearLayout ll_header;
    private int headerHeight;
    private LinearLayoutManager mManager;
    private int mCurrentPosition = -1;
    private static final int NORMAL_TYPLE = 0;
    private static final int TIME_TYPLE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        ll_header = (LinearLayout) findViewById(R.id.ll_header);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        mManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mManager);
        mRecyclerView.setAdapter(mAdapter = new HomeAdapter());
        mAdapter.setOnItemClickListener(new HomeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if(position == 1){
                    startActivity(new Intent(MainActivity.this , MessengerActivity.class));
                }
                if(position == 2){
                    startActivity(new Intent(MainActivity.this , BookManagerActivity.class));
                }
            }
        });
        headerTitle = (TextView) findViewById(R.id.tv_header_title);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                headerHeight = ll_header.getHeight();
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(mAdapter.getItemViewType(mCurrentPosition + 1) == TIME_TYPLE){
                    View view = mManager.findViewByPosition(mCurrentPosition + 1);
                    if (view != null) {
                        if (view.getTop() <= headerHeight) {
                            ll_header.setY(-(headerHeight - view.getTop()));
                        } else {
                            ll_header.setY(0);
                        }
                    }
                }
                if (mCurrentPosition != mManager.findFirstVisibleItemPosition()) {
                    mCurrentPosition = mManager.findFirstVisibleItemPosition();
                    ll_header.setY(0);
                    headerTitle.setText(Utils.getWeek(mCurrentPosition / 4));
                }
            }
        });
    }
}
