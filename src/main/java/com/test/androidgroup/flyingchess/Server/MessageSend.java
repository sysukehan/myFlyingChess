package com.test.androidgroup.flyingchess.Server;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by asl on 16-3-24.
 */
public class MessageSend extends Thread{
    public Handler sendHandler;
    public Handler mainHandler;
    private OutputStream out;

    MessageSend(OutputStream _out, Handler _mainHandler)
    {
        this.out = _out;
        this.mainHandler = _mainHandler;
    }

    public void run()
    {
        while(true)
        {
            Looper.prepare();
            this.sendHandler = new Handler()
            {
                public void handleMessage(Message msg)
                {
                    switch(msg.what)
                    {
                        case 0x100:
                            sendMSG(msg);
                            break;
                    }

                }
            };
            Looper.loop();
        }
    }

    void sendMSG(Message message)
    {
        try
        {
            out.write((byte[])message.obj);
        }
        catch (IOException e)
        {

        }
    }
}