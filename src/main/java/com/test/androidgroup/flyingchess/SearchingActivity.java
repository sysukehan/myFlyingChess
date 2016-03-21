package com.test.androidgroup.flyingchess;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by kehan on 16-3-21.
 */
public class SearchingActivity extends Activity {

    private TextView title;//标题上的字
    private ImageView userImage;//用户头像
    private int playerNumber;//玩游戏的人的数量
    private int time = 0;//模拟的时候算时间用的
    private Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searching_layout);
        context = this;

        Intent intent = getIntent();
        //获取上一级选择的游戏人数，缺省为0
        playerNumber = intent.getIntExtra("playerNumber", 0);

        title = (TextView) findViewById(R.id.title);
        userImage = (ImageView) findViewById(R.id.user_image);
        title.setText("搜索中.");


        //以下只是一个模拟搜索的过程，6s后会跳到游戏界面，实现的时候把这个删掉就行
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (time >= 6000) {
                    handler.removeCallbacks(this);
                    Intent intent = new Intent(context, GameActivity.class);
                    startActivity(intent);
                    return;
                } else {
                    if (title.getText().toString().equals("搜索中...")) {
                        title.setText("搜索中.");
                    } else {
                        title.setText(title.getText().toString() + ".");
                    }
                    handler.postDelayed(this, 1000);
                    time = time + 1000;
                }
            }
        }, 1000);
    }
}
