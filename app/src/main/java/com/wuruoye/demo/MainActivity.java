package com.wuruoye.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.wuruoye.widgets.ArrowView;

/**
 * @Created : wuruoye
 * @Date : 2018/6/3 09:10.
 * @Description :
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    ArrowView av;
    int direct = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        av = findViewById(R.id.av);
        av.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        av.change((direct = direct + 2) % 4);
    }
}
