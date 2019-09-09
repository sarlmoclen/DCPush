package com.example.dcpush.demo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.dcpush.R;
import com.sarlmoclen.dcpush.DCWebSocketManager;

public class MainActivity extends AppCompatActivity {

    private TextView tv_content;
    private MyBroadcastReceiver myBroadcastReceiver;

    public class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String content = intent.getStringExtra("content");
            String c = tv_content.getText().toString() + "\n" + content;
            tv_content.setText(c);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        tv_content = findViewById(R.id.tv_content);

        findViewById(R.id.bt_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DCWebSocketManager.getInstance().startSocket();
            }
        });
        findViewById(R.id.bt_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DCWebSocketManager.getInstance().sendMessage("test");
            }
        });
        findViewById(R.id.bt_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DCWebSocketManager.getInstance().closeSocket();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver();
    }

    private void registerReceiver(){
        myBroadcastReceiver = new MyBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("test.action");
        registerReceiver(myBroadcastReceiver, intentFilter);
    }

    private void unregisterReceiver(){
        if(myBroadcastReceiver != null)
            unregisterReceiver(myBroadcastReceiver);
    }

}
