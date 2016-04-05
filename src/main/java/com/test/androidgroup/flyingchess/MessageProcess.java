package com.test.androidgroup.flyingchess;

import android.os.Looper;
import android.os.Message;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import android.os.Handler;
import android.util.Log;

import com.google.protobuf.ByteString;

/**
 * Created by asl on 16-3-23.
 */
public class MessageProcess extends Thread{
    private Socket socket;
    private String ip;
    private int port;

    static public Handler sendHandler;

    private InputStream in;
    private OutputStream out;
    
    public MessageSend ms;

    //public MessageProcessForUI mpi;
    public boolean flag;

    public MessageProcess() {
        this.ip = "115.28.168.58";
        //this.ip = "172.18.201.128";
        //this.ip = "192.168.11.195";
        //this.ip = "192.168.43.48";
        this.port = 9099;
        this.socket = new Socket();
        this.in = null;
        this.out = null;
        //this.sendHandler = _handler;
        flag = false;
    }

    @Override
    public void run()
    {
        Looper.prepare();
        try
        {
            socket.connect(new InetSocketAddress(ip, port), 100000);
            in = socket.getInputStream();
            out = socket.getOutputStream();

            this.ms = new MessageSend(out, this.sendHandler);
            //mpi = new MessageProcessForUI(ms.sendHandler);
            ms.start();
            receiveMessage();
        }
        catch (IOException e)
        {
            //Log.d("FlyingChess", e.toString()+ip+Integer.toString(port));
            Message emsg = new Message();
            emsg.what = 0x130;
            emsg.obj = "网络连接超时";
            this.sendHandler.sendMessage(emsg);
            e.printStackTrace();
        }

        Looper.loop();
    }

    void receiveMessage()
    {
        try
        {
            int count = 0;
            while(true)  //服务器关闭socket或flag为false时终止循环
            {
                byte len[] = new byte[1024];
                count = in.read(len);   //接收来自服务器的信息
                Log.d("---count-----", Integer.toString(count) + Boolean.toString(flag));
                if(count < 0)
                {
                    Message emsg = new Message();
                    emsg.what = 0x131;
                    emsg.obj = "失去连接";
                    this.sendHandler.sendMessage(emsg);
                    socket.close();
                    return;
                }
                if(flag)
                {
                    byte[] temp = new byte[count];  //复制到适合大小的数组中
                    for (int i = 0; i < count; i++)
                    {
                        temp[i] = len[i];
                    }
                    Message msg = new Message();
                    msg.what = 0x123;
                    msg.obj = temp;
                    this.sendHandler.sendMessage(msg);
                    //Log.d("FlyingChess", "send" + sendHandler.toString());
                }
            }
        }
        catch (Exception e)
        {
            Message emsg = new Message();
            emsg.what = 0x130;
            emsg.obj = "网络连接超时";
            this.sendHandler.sendMessage(emsg);
            e.printStackTrace();
        }
    }
}

