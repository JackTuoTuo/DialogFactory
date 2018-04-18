package com.tuo.dialogfactory;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.tuo.customview.DialogFactory;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    public void onclick(View view) {
        DialogFactory dialogFactory = new DialogFactory.Builder(this)
                .setTitle("测试")
                .setMessage("我是最帅的")
                .create();


        dialogFactory.show();

    }
}
