package com.test.androidgroup.flyingchess;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by kehan on 16-3-21.
 */

public class PersonalInformationActivity extends FlyingChessActivity {

    private ImageView back;
    private ImageView image;
    private TextView title;
    private Context context;
    private TextView userId;//用户ID
    private TextView username;//用户昵称
    private TextView sum;//线上对局总数
    private TextView exceedSum;//对局总数超越人数百分比
    private TextView winSum;//线上对局胜利局数
    private TextView exceedWinSum;//向上对局胜利局数超越人数百分比
    private TextView winPercent;//线上对局胜率
    private TextView exceedWinPercent;//线上对局胜率超越人数百分比

    private Button logout;

    @Override
    public void onCreate(Bundle savedInstancestate) {
        super.onCreate(savedInstancestate);
        setContentView(R.layout.information_layout);
        context = this;
        image = (ImageView) findViewById(R.id.user_image);
        title = (TextView) findViewById(R.id.title);
        back = (ImageView) findViewById(R.id.back);
        title.setText("个人资料");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        image.setImageResource(R.mipmap.config);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ChangeInformationActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        userId = (TextView) findViewById(R.id.info_userid);
        username = (TextView) findViewById(R.id.info_username);
        sum = (TextView) findViewById(R.id.sum_matches);
        exceedSum = (TextView) findViewById(R.id.sum_win_players);
        winSum = (TextView) findViewById(R.id.win_matches);
        exceedWinSum = (TextView) findViewById(R.id.win_matches_player);
        winPercent = (TextView) findViewById(R.id.win_percent);
        exceedWinPercent = (TextView) findViewById(R.id.win_percent_player);
        userId.setText(RunningInformation.playerId);
        username.setText(RunningInformation.playerName);
        sum.setText(RunningInformation.sumMatches + "局");
        exceedSum.setText("共超过了" + RunningInformation.exceedSumMatches + "%的玩家");
        winSum.setText(RunningInformation.winMatches + "局");
        exceedWinSum.setText("共超过了" + RunningInformation.exceedWinMatches + "%的玩家");
        winPercent.setText(RunningInformation.percent + "%");
        exceedWinPercent.setText("共超过了" + RunningInformation.exceedPercent + "%的玩家");

        logout = (Button) findViewById(R.id.logout);
        //注销的逻辑在这里实现
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                    注销的时候可能需要跟服务器说一声
                */
                //发送广播，退出所有Activity，开启新的LoginAndRegisterActivity
                Intent newIntent = new Intent("com.test.androidgroup.flyingchess.BACK_TO_LOGIN_REGISTER");
                sendBroadcast(newIntent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    username.setText(data.getStringExtra("newName"));
                }
                break;
            default:
                break;
        }
    }
/*
    @Override
    public void onStart() {
        super.onStart();
        Log.d("FlyingChess", "PersonalInformationActivity onStart()");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("FlyingChess", "PersonalInformationActivity onResume()");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("FlyingChess", "PersonalInformationActivity onPause()");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("FlyingChess", "PersonalInformationActivity onStop()");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("FlyingChess", "PersonalInformationActivity onDestroy()");
    }
*/
}
