package com.test.androidgroup.flyingchess;

/**
 * Created by asl on 16-3-24.
 */
public class MSGTool {
    static public int sequenceID;

    public MSGTool()
    {
        sequenceID = 0;
    }

    public static MSGS.MSG createRegisterRequest(String userID, String nickname, String password)
    {
        MSGS.RegisterReq.Builder msgL= MSGS.RegisterReq.newBuilder();
        msgL.setUserID(userID);
        msgL.setNickname(nickname);
        msgL.setPassword(password);

        MSGS.Request.Builder msgQ = MSGS.Request.newBuilder();
        msgQ.setRegisterReq(msgL);

        MSGS.MSG.Builder msg = MSGS.MSG.newBuilder();
        msg.setMessageType(MSGS.MSGType.register_Request);
        msg.setSequenceID(sequenceID);
        sequenceID ++;

        msg.setRequest(msgQ);
        return msg.build();
    }

    public static MSGS.MSG createLoginRequest(String userID, String password)
    {
        MSGS.LoginReq.Builder msgL= MSGS.LoginReq.newBuilder();
        msgL.setUserID(userID);
        msgL.setPassword(password);

        MSGS.Request.Builder msgQ = MSGS.Request.newBuilder();
        msgQ.setLoginReq(msgL);

        MSGS.MSG.Builder msg = MSGS.MSG.newBuilder();
        msg.setMessageType(MSGS.MSGType.login_Request);
        msg.setSequenceID(sequenceID);
        sequenceID ++;

        msg.setRequest(msgQ);
        return msg.build();
    }

    public static MSGS.MSG createRoomListRequest(int size)
    {
        MSGS.RoomListReq.Builder msgL= MSGS.RoomListReq.newBuilder();
        msgL.setSize(size);

        MSGS.Request.Builder msgQ = MSGS.Request.newBuilder();
        msgQ.setRoomListReq(msgL);

        MSGS.MSG.Builder msg = MSGS.MSG.newBuilder();
        msg.setMessageType(MSGS.MSGType.roomList_Request);
        msg.setSequenceID(sequenceID);
        sequenceID ++;

        msg.setRequest(msgQ);
        return msg.build();
    }

    public static MSGS.MSG createEnterRoomRequest(String userID, int roomID)
    {
        MSGS.EnterRoomReq.Builder msgL= MSGS.EnterRoomReq.newBuilder();
        msgL.setUserID(userID);
        msgL.setRoomID(roomID);

        MSGS.Request.Builder msgQ = MSGS.Request.newBuilder();
        msgQ.setEnterRoomReq(msgL);

        MSGS.MSG.Builder msg = MSGS.MSG.newBuilder();
        msg.setMessageType(MSGS.MSGType.enterRoom_Request);
        msg.setSequenceID(sequenceID);
        sequenceID ++;

        msg.setRequest(msgQ);
        return msg.build();
    }

    public static MSGS.MSG createCreateRoomRequest(String userID, int size)
    {
        MSGS.CreateRoomReq.Builder msgL= MSGS.CreateRoomReq.newBuilder();
        //msgL.setUserID(userID);
        msgL.setSize(size);

        MSGS.Request.Builder msgQ = MSGS.Request.newBuilder();
        msgQ.setCreateRoomReq(msgL);

        MSGS.MSG.Builder msg = MSGS.MSG.newBuilder();
        msg.setMessageType(MSGS.MSGType.createRoom_Request);
        msg.setSequenceID(sequenceID);
        sequenceID ++;

        msg.setRequest(msgQ);
        return msg.build();
    }

    public static MSGS.MSG createBeginGameRequest(int roomID)
    {
        MSGS.BeginGameReq.Builder msgL= MSGS.BeginGameReq.newBuilder();
        msgL.setRoomID(roomID);

        MSGS.Request.Builder msgQ = MSGS.Request.newBuilder();
        msgQ.setBeginGameReq(msgL);

        MSGS.MSG.Builder msg = MSGS.MSG.newBuilder();
        msg.setMessageType(MSGS.MSGType.beginGame_Request);
        msg.setSequenceID(sequenceID);
        sequenceID ++;

        msg.setRequest(msgQ);
        return msg.build();
    }

    public static MSGS.MSG createShakeDiceRequest(String userID, int roomID)
    {
        MSGS.ShakeDiceReq.Builder msgL= MSGS.ShakeDiceReq.newBuilder();
        msgL.setUserID(userID);
        msgL.setRoomID(roomID);

        MSGS.Request.Builder msgQ = MSGS.Request.newBuilder();
        msgQ.setShakeDiceReq(msgL);

        MSGS.MSG.Builder msg = MSGS.MSG.newBuilder();
        msg.setMessageType(MSGS.MSGType.shakeDice_Request);
        msg.setSequenceID(sequenceID);
        sequenceID ++;

        msg.setRequest(msgQ);
        return msg.build();
    }

    public static MSGS.MSG createChessMoveRequest(String userID, int roomID, MSGS.Color color, int chessID, int dest)
    {
        MSGS.ChessMoveReq.Builder msgL= MSGS.ChessMoveReq.newBuilder();
        msgL.setRoomID(roomID);
        msgL.setUserID(userID);
        MSGS.ChessMove.Builder c = MSGS.ChessMove.newBuilder();
        c.setDestination(dest);
        c.setColor(color);
        c.setChessID(chessID);

        MSGS.Request.Builder msgQ = MSGS.Request.newBuilder();
        msgQ.setChessMoveReq(msgL);

        MSGS.MSG.Builder msg = MSGS.MSG.newBuilder();
        msg.setMessageType(MSGS.MSGType.chessMove_Request);
        msg.setSequenceID(sequenceID);
        sequenceID ++;

        msg.setRequest(msgQ);
        return msg.build();
    }

    public static MSGS.MSG createWinRequest(String userID, int roomID)
    {
        MSGS.WinReq.Builder msgL= MSGS.WinReq.newBuilder();
        msgL.setUserID(userID);
        msgL.setRoomID(roomID);

        MSGS.Request.Builder msgQ = MSGS.Request.newBuilder();
        msgQ.setWinReq(msgL);

        MSGS.MSG.Builder msg = MSGS.MSG.newBuilder();
        msg.setMessageType(MSGS.MSGType.win_Request);
        msg.setSequenceID(sequenceID);
        sequenceID ++;

        msg.setRequest(msgQ);
        return msg.build();
    }
}
