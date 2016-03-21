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

    private TextView title;
    private ImageView userImage;
    private int playerNumber;
    private int time = 0;
    private Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searching_layout);
        context = this;

        Intent intent = getIntent();
        playerNumber = intent.getIntExtra("playerNumber", 0);

        title = (TextView) findViewById(R.id.title);
        userImage = (ImageView) findViewById(R.id.user_image);
        title.setText("搜索中.");


        //以下只是一个模拟搜索的过程
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
