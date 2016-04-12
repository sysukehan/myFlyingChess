package com.test.androidgroup.flyingchess;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import com.test.androidgroup.flyingchess.AnotherPlayer.Player;
import com.test.androidgroup.flyingchess.Server.MSGS;
import com.test.androidgroup.flyingchess.Server.MessageProcessForUI;

public class RoomActivity extends FlyingChessActivity {
    private String previousView;
    private Context context;
    private RoomInfo room = new RoomInfo();
    private RoomHandler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);
        context = this;
        Intent intent = getIntent();
        room = (RoomInfo)intent.getParcelableExtra("Room");
        TextView title = (TextView) findViewById(R.id.show_room_id);
        title.setText(String.format("房间ID：%d", room.getRoomID()));
        SetView(room.getPlayerNumber());
        Button exitRoom = (Button) findViewById(R.id.exit_room);
        Button startGame = (Button) findViewById(R.id.start_game);
        if (GameInfo.getUser().getUser_Nick_Name().equals(room.getHost())) startGame.setVisibility(View.VISIBLE);
        exitRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // MessageProcessForUI.sendExitRoomReq(room.getRoomID(),user.getUser_id(),mp.ms.sendHandler);
            }
        });
        startGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MessageProcessForUI.sendBeginRoomReq(room.getRoomID(), GameInfo.mp.ms.sendHandler);
            }
        });

        handler = new RoomHandler(this);
        GameInfo.setHandler(handler);

    }
    public void SetView(int player_num) {


            LinearLayout size2 = (LinearLayout) findViewById(R.id.player2);
            LinearLayout size3 = (LinearLayout) findViewById(R.id.player_list2);
            LinearLayout size4 = (LinearLayout) findViewById(R.id.player4);

            //玩家一
            LinearLayout player1 = (LinearLayout) findViewById(R.id.player_item1);
            TextView player1_info = (TextView) player1.findViewById(R.id.player_id);
            player1_info.setText(room.players[0].getUser_Nick_Name());

            //玩家二
            LinearLayout player2 = (LinearLayout) findViewById(R.id.player_item2);
            TextView player2_info = (TextView) player2.findViewById(R.id.player_id);

            //玩家三
            LinearLayout player3 = (LinearLayout) findViewById(R.id.player_item3);
            TextView player3_info = (TextView) player3.findViewById(R.id.player_id);
            //玩家四
            LinearLayout player4 = (LinearLayout) findViewById(R.id.player_item4);
            TextView player4_info = (TextView) player4.findViewById(R.id.player_id);

            if(player_num >= 2) {
                size2.setVisibility(View.VISIBLE);
                player2_info.setText(room.players[1].getUser_Nick_Name());
            }
            if(player_num >= 3) {
                size3.setVisibility(View.VISIBLE);
                player3_info.setText(room.players[2].getUser_Nick_Name());
            }

            if(player_num == 4) {
                size4.setVisibility(View.VISIBLE);
                player4_info.setText(room.players[3].getUser_Nick_Name());
            }
    }

    private static class RoomHandler extends Handler {
        private final WeakReference<RoomActivity> mActivity;
        public RoomHandler(RoomActivity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            RoomActivity activity = mActivity.get();
            if (activity != null) {
                if (msg.what == 0x123)//是正常的消息
                {
                    try
                    {
                        MSGS.MSG m = MSGS.MSG.parseFrom((byte[])msg.obj);   //获取消息类
//                        if(m.getMessageType() == MSGS.MSGType.exitRoom_Response);
//                        {
//                            if(m.getResponse().getResult()) {
//                                Intent intent = new Intent();
//                                Bundle data = new Bundle();
//
//                                intent = new Intent(activity.context, ChooseModeActivity.class);
//                                data.putInt("playerNumber",activity.room.getRoomSize());
//                                intent.putExtras(data);
//
//                                activity.startActivity(intent);
//                            }
//                            else new AlertDialog.Builder(activity).setTitle("此时无法退出房间").setPositiveButton("OK", null).create().show();
//                        }
                        if(m.getMessageType() == MSGS.MSGType.beginGame_Response) {
                            if(m.getResponse().getResult()) {
                                Intent intent = new Intent(activity.context,GameActivity.class);
//                                Bundle data = new Bundle();
//                                data.putParcelable("game",activity.room);
//                                intent.putExtras(data);
                                activity.startActivity(intent);
                            }
                            else Toast.makeText(activity.getApplicationContext(),"Oops,开始游戏失败",Toast.LENGTH_LONG).show();
                        }

                        if(m.getMessageType() == MSGS.MSGType.roomChange_Notification) {
                            MSGS.RoomChangeNtf change = m.getNotification().getRoomChangeNtf();
                            Log.d("room enter", Boolean.toString(change.getInout()));
                            if(change.getInout() == true) { //进入房间通知
                                Log.d("room enter", change.getPlayer(0).getNickname()+"进入了房间");
                                Toast.makeText(activity.getApplicationContext(),change.getPlayer(0).getNickname()+"进入了房间",Toast.LENGTH_LONG).show();
                                activity.room.addPlayer(new Player(change.getPlayer(0).getUserID(), change.getPlayer(0).getNickname()));
                                activity.SetView(activity.room.getPlayerNumber());
                            }
                            else{ //退出房间通知
                                Toast.makeText(activity.getApplicationContext(),change.getPlayer(0).getNickname()+"退出了房间",Toast.LENGTH_LONG).show();
                                activity.room.removePlayer(new Player(change.getPlayer(0).getUserID(),change.getPlayer(0).getNickname()));
                                activity.SetView(activity.room.getPlayerNumber());
                            }
                        }
                        if(m.getMessageType() == MSGS.MSGType.beginGame_Notification) {
                            Toast.makeText(activity.getApplicationContext(), "准备开始游戏。。。", Toast.LENGTH_LONG).show();
                            MSGS.BeginGameNtf begin = m.getNotification().getBeginGameNtf();
                            Intent intent = new Intent(activity.context,GameActivity.class);
//                                Bundle data = new Bundle();
//                                data.putParcelable("game",activity.room);
//                                intent.putExtras(data);
                            activity.startActivity(intent);
                        }
                    }
                    catch (Exception e){Log.d("parse", "Oops,GG");}
                }
                if (msg.what == 0x130)//无法建立连接
                {
                    Toast.makeText(activity.getApplicationContext(), "建立连接失败", Toast.LENGTH_LONG).show();
                }
                if (msg.what == 0x131)//连接断开
                {
                    Toast.makeText(activity.getApplicationContext(), "失去连接", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}