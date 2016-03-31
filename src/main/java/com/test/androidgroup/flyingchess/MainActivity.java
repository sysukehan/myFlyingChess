package com.test.androidgroup.flyingchess;

import android.app.AlertDialog;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
/*
    public Handler handler;
    MessageProcess mp;  //处理与服务器的连接，创建新Activity的时候要把这个句柄传下去
    Button b;
    TextView h;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        b = (Button)findViewById(R.id.button);
        h = (TextView)findViewById(R.id.textView);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mp.ms.sendHandler == null)
                    new AlertDialog.Builder(MainActivity.this).setTitle("无法发送信息").setPositiveButton("OK", null).create().show();
                MessageProcessForUI.sendRegisterReq("kkkk", "1234", "1234", mp.ms.sendHandler);
                MessageProcessForUI.sendRegisterReq()
                mp.flag = true;//打开应用后设置，只需要设置一次
            }
        });
        //使用这个handler接收来自服务器的信息
        handler = new Handler()//接收来自服务器消息的句柄，创建新Activity的时候要把这个句柄传下去
        {
            public void handleMessage(Message msg) {
                if (msg.what == 0x123)
                {
                    try
                    {
                        MSGS.MSG m = MSGS.MSG.parseFrom((byte[])msg.obj);   //获取消息类
                        //获取非数组元素：m.getSequenceID()
                        //获取数组元素：m.getResponse().getRoomListRes().getRoomList(i);[即获得第i个房间的对象]
                        h.setText(Integer.toString(m.getSequenceID())+Boolean.toString(m.getResponse().getResult())+m.getResponse().getError()); //doSomething（）；
                    }
                    catch (Exception e){}
                }
                if (msg.what == 0x130)//若无法建立连接
                {
                    new AlertDialog.Builder(MainActivity.this).setTitle("建立连接失败").setPositiveButton("OK", null).create().show();
                }
                if (msg.what == 0x131)//若失去连接
                {
                    new AlertDialog.Builder(MainActivity.this).setTitle("失去连接").setPositiveButton("OK", null).create().show();
                }
            }
        };
        mp = new MessageProcess();
        mp.start();
		mp.sendHandler = handler;
    }
*/
}
