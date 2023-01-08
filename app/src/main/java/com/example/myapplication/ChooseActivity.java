package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class ChooseActivity extends MainActivity{
    static String TAG = "JoyWan";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);

        Button subscribeButton = findViewById(R.id.DisplayButton);
        subscribeButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClickSub");
                Intent intent = new Intent();
                intent.setClass(ChooseActivity.this, SubscribeActivity.class);
                startActivity(intent);
            }
        });
    }
}