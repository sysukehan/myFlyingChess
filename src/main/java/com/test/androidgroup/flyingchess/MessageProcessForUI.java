package com.test.androidgroup.flyingchess;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * Created by asl on 16-3-24.
 */
public class MessageProcessForUI {

    static private int sendMessage(MSGS.MSG m, Handler handler)
    {
        Message msg = new Message();
        msg.what = 0x100;
        msg.obj = m.toByteArray();
        handler.sendMessage(msg);
        return 1;
    }

    static public int sendRegisterReq(String userID, String password, String nickname, Handler handler)
    {
        MSGS.MSG m = MSGTool.createRegisterRequest(userID, password, nickname);
        sendMessage(m, handler);
        return 1;
    }

    static public int sendLoginReq(String userID, String password, Handler handler)
    {
        MSGS.MSG m = MSGTool.createLoginRequest(userID, password);
        sendMessage(m, handler);
        return 1;
    }

    static public int sendRoomListReq(int size, Handler handler)
    {
        MSGS.MSG m = MSGTool.createRoomListRequest(size);
        sendMessage(m, handler);
        return 1;
    }

    static public int sendEnterRoomReq(String userID, int roomID, Handler handler)
    {
        MSGS.MSG m = MSGTool.createEnterRoomRequest(userID, roomID);
        sendMessage(m, handler);
        return 1;
    }

    static public int sendCreateRoomReq(String userID, int size, Handler handler)
    {
        MSGS.MSG m = MSGTool.createCreateRoomRequest(userID, size);
        sendMessage(m, handler);
        return 1;
    }

    static public int sendBeginRoomReq(int roomID, Handler handler)
    {
        MSGS.MSG m = MSGTool.createBeginGameRequest(roomID);
        sendMessage(m, handler);
        return 1;
    }

    static public int sendShakeDiceReq(String userID, int roomID, Handler handler)
    {
        MSGS.MSG m = MSGTool.createShakeDiceRequest(userID, roomID);
        sendMessage(m, handler);
        return 1;
    }

    static public int sendChessMoveReq(String userID, int roomID, MSGS.Color color, int chessID, int dest, Handler handler)
    {
        MSGS.MSG m = MSGTool.createChessMoveRequest(userID, roomID, color, chessID, dest);
        sendMessage(m, handler);
        return 1;
    }

    static public int sendWinRequestReq(String userID, int roomID, Handler handler) {
        MSGS.MSG m = MSGTool.createWinRequest(userID, roomID);
        sendMessage(m, handler);
        return 1;
    }
}
