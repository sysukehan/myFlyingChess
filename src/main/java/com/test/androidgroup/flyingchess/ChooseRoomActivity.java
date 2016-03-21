package com.test.androidgroup.flyingchess;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by kehan on 16-3-21.
 */
public class ChooseRoomActivity extends Activity {

    private TextView title;//标题栏上的字
    private ImageView userImage;//用户头像
    private int playerNumber;//玩家人数

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_room_layout);

        Intent intent = getIntent();
        intent.getIntExtra("playerNumber", 0);//获取玩家人数，缺省为0

        title = (TextView) findViewById(R.id.title);
        userImage = (ImageView) findViewById(R.id.user_image);
        title.setText("房间");
    }
}
