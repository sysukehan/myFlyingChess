package com.test.androidgroup.flyingchess.Resource;

import com.test.androidgroup.flyingchess.Server.*;

import java.util.Random;

import android.os.Handler;
import android.util.Log;

public class Dice {
	static boolean num = false;
	private Random random;
    public int rollresult = 6;
    private Thread waitToSendRoll;
    private Handler _handler;

	public Dice(int seed) {
        random = new Random(seed);
	}

	public int roll(Handler handler){
		//等待1秒后发送消息

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

        return  rollresult;
	}



}
