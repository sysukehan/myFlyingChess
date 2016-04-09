package com.test.androidgroup.flyingchess;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;

import com.test.androidgroup.flyingchess.AnotherPlayer.Player;

/**
 * Created by kehan on 16-3-21.
 */
public class ChooseRoomActivity extends FlyingChessActivity {

    private TextView title;//标题栏上的字
    private ImageView userImage;//用户头像
    private ImageView back;//返回键

    private ListView Room_List; //房间列表
    private int RoomSize;//房间大小
    private List<HashMap<String, Object>> Room_Data;
    private RoomInfo[] rooms = new RoomInfo[20];
    private RoomInfo room_enter = new RoomInfo();
    private FloatingActionButton add_room; //创建房间按钮
    public Context context;
    public Room_Adapter adapter;
    private int count = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_room_layout);

        //初始化返回键，为返回键绑定监听器
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        GameInfo.mp = RunningInformation.mp;
        GameInfo.setUser(new Player(RunningInformation.playerId, RunningInformation.playerName));

        Intent intent = getIntent();
        RoomSize = intent.getIntExtra("playerNumber", 0);
        Log.v("RoomSize",Integer.toString(RoomSize));
        room_enter.setRoomSize(RoomSize);
        context = this;


        //建立从服务器接收消息的句柄
        ChooseRoomHandler handler = new ChooseRoomHandler(this);

        GameInfo.setHandler(handler);

        MessageProcessForUI.sendRoomListReq(RoomSize, GameInfo.mp.ms.sendHandler);

        add_room = (FloatingActionButton) findViewById(R.id.add_room_button);
        add_room.setVisibility(View.VISIBLE);
        add_room.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MessageProcessForUI.sendCreateRoomReq(GameInfo.getUser().getUser_id(), RoomSize, GameInfo.mp.ms.sendHandler);
            }
        });

        title = (TextView) findViewById(R.id.title);
        userImage = (ImageView) findViewById(R.id.user_image);
        title.setText("房间");
        //Room_Data = new List<HashMap<String, Object>>();

        adapter = new Room_Adapter(this);
        Room_List = (ListView) findViewById(R.id.room_list);
        Room_List.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               MessageProcessForUI.sendEnterRoomReq(GameInfo.getUser().getUser_id(),rooms[position].getRoomID(),GameInfo.mp.ms.sendHandler);
            }
        });
    }

    private static class ChooseRoomHandler extends Handler {
        private final WeakReference<ChooseRoomActivity> mActivity;

        public ChooseRoomHandler(ChooseRoomActivity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            ChooseRoomActivity activity = mActivity.get();
            if (activity != null) {
                Log.d("message", Integer.toHexString(msg.what));
                if (msg.what == 0x123)//是正常的消息
                {
                    try {
                        Log.d("message2", Integer.toHexString(msg.what));
                        MSGS.MSG m = MSGS.MSG.parseFrom((byte[]) msg.obj);   //获取消息类
                        if (m.getMessageType() == MSGS.MSGType.roomList_Response) {
                            activity.count = m.getResponse().getRoomListRes().getRoomsCount();
                            Log.d("message", Integer.toHexString(activity.count));
                            for (int i = 0; i < activity.count; i++) {
                                Log.d("message", Integer.toHexString(m.getResponse().getRoomListRes().getRoomsCount()));
                                MSGS.RoomListRes room_list = m.getResponse().getRoomListRes();
                                activity.rooms[i] = new RoomInfo();
                                activity.rooms[i].setRoomID(room_list.getRooms(i).getRoomID());
                                activity.rooms[i].setHost(room_list.getRooms(i).getHost());
                                activity.rooms[i].setPlayerNumber(room_list.getRooms(i).getPlayersCount());
                                activity.rooms[i].setRoomSize(activity.RoomSize);
                                Log.d("message player num", Integer.toString(activity.rooms[i].getPlayerNumber()));
                                for (int j = 0; j < activity.rooms[i].getPlayerNumber(); j++) {
                                    activity.rooms[i].players[j] = new Player(room_list.getRooms(i).getPlayers(j).getUserID(), room_list.getRooms(i).getPlayers(j).getNickname());
                                }
                            }
                            activity.Room_Data = activity.getData();
                            activity.Room_List.setAdapter(activity.adapter);
                        }
                        else {
                            if (m.getMessageType() == MSGS.MSGType.createRoom_Response) {
                                MSGS.CreateRoomRes create = m.getResponse().getCreateRoomRes();
                                if (m.getResponse().getResult()) {
                                    activity.room_enter.setRoomID(create.getRoomID()); //得到请求建立的房间的ID
                                    activity.room_enter.setHost(GameInfo.getUser().getUser_id());
                                    activity.room_enter.setPlayerNumber(1);
                                    activity.room_enter.players[0] = GameInfo.getUser();
                                }
                                else new AlertDialog.Builder(activity).setTitle("创建房间失败").setPositiveButton("OK", null).create().show();
                            }
                            if (m.getMessageType() == MSGS.MSGType.enterRoom_Response) {
                                MSGS.EnterRoomRes enter = m.getResponse().getEnterRoomRes();
                                if (m.getResponse().getResult()) {
                                    activity.room_enter.setRoomID(enter.getRoomInfo().getRoomID()); //得到进入建立的房间的ID
                                    activity.room_enter.setHost(enter.getRoomInfo().getHost()); //得到进入建立的房间的ID
                                    activity.room_enter.setPlayerNumber(enter.getRoomInfo().getPlayersCount()); //得到进入建立的房间的ID
                                    for (int j = 0; j < activity.room_enter.getPlayerNumber(); j++) {
                                        activity.room_enter.players[j] = new Player(enter.getRoomInfo().getPlayers(j).getUserID(), enter.getRoomInfo().getPlayers(j).getNickname());
                                    }
                                } else
                                    new AlertDialog.Builder(activity).setTitle("请求进入房间失败，可能该房间人数已满").setPositiveButton("OK", null).create().show();
                            }
                            if(m.getResponse().getResult()) {
                                Intent intent = new Intent(activity, RoomActivity.class);
                                Bundle data = new Bundle();
                                data.putParcelable("Room", activity.room_enter);
                                intent.putExtras(data);
                                activity.startActivity(intent);
                            }
                        }
                    } catch (Exception e) {
                        Log.d("message", e.toString());
                    }
                }
                if (msg.what == 0x130)//无法建立连接
                {
                    new AlertDialog.Builder(activity).setTitle("建立连接失败").setPositiveButton("OK", null).create().show();
                }
                if (msg.what == 0x131)//连接断开
                {
                    new AlertDialog.Builder(activity).setTitle("失去连接").setPositiveButton("OK", null).create().show();
                }
            }
        }
    }

    public static class ViewHolder {
        TextView room_id;
        TextView host;
        TextView percentage;
    }

    public class Room_Adapter extends BaseAdapter

    {
        private LayoutInflater mInflater;// 动态布局映射

        public Room_Adapter(Context context) {
            this.mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return Room_Data.size();
        }

        @Override
        public Object getItem(int position) {
            return Room_Data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.room_item, null);
                holder.room_id = (TextView) convertView.findViewById(R.id.room_id);
                holder.host = (TextView) convertView.findViewById(R.id.host_id);
                holder.percentage = (TextView) convertView.findViewById(R.id.percentage);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.room_id.setText(Room_Data.get(position).get("room_id").toString());
            holder.host.setText(Room_Data.get(position).get("host").toString());
            holder.percentage.setText(Room_Data.get(position).get("percentage").toString());
            return convertView;
        }
    }

    private List<HashMap<String, Object>> getData() {
        ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> map = null;
        Log.d("list", Integer.toString(count)+" "+Integer.toString(rooms.length));
        for (int i = 0; i < count; i++) {
            map = new HashMap<String, Object>();
            map.put("room_id", "房间：" + rooms[i].getRoomID());
            map.put("host", "房主：" + rooms[i].getHost());
            map.put("percentage", rooms[i].getPlayerNumber() + "/" + RoomSize);
            list.add(map);
        }
        return list;
    }
}