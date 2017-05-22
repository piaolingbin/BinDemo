package com.soufun.bin.bindemo.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.soufun.bin.bindemo.R;
import com.soufun.bin.bindemo.view.InScrollView;
import com.soufun.bin.bindemo.view.OutScrollView;

public class ScrollViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll_view);
        OutScrollView osl = (OutScrollView) findViewById(R.id.outScrollView);
        InScrollView isl = (InScrollView) findViewById(R.id.inScrollView);
        isl.setOutScrollView(osl);
    }
}
