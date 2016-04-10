package com.test.androidgroup.flyingchess.Resource;

import com.test.androidgroup.flyingchess.Server.*;

import java.util.Random;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class Dice {
	static boolean num = false;
	private Random random;
    private Thread waitToSendRoll;
    public int rollresult;
    private Handler _handler;

	public Dice(int seed) {
        random = new Random();
	}

    public int rollOffline(Handler handler){
        Log.i("roll","Offline");
        _handler = handler;
        waitToSendRoll = new Thread(new Runnable() {
            public void run(){
                try{
                    Thread.sleep(1000);
                    Message msg = new Message();
                    msg.what = 0x101;
                    Bundle bundle = new Bundle();
                    bundle.putInt("rollResult",random.nextInt(6) + 1);
                    msg.setData(bundle);
                    _handler.sendMessage(msg);

                }
                catch (Exception e){}
            }
        });
        waitToSendRoll.start();
        return 0;
    }

	public int roll(Handler handler){
		//等待1秒后发送消息
        Log.i("roll","Online");
        _handler = handler;
        waitToSendRoll = new Thread(new Runnable() {
            public void run(){
                try{
                    Thread.sleep(1000);
                    MessageProcessForUI.sendShakeDiceReq("123456", 1, _handler);

                }
                catch (Exception e){}
            }
        });
        waitToSendRoll.start();

        return  0;
	}



}
