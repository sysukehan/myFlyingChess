package com.test.androidgroup.flyingchess;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by kehan on 16-3-21.
 */
public class GameActivity extends Activity {

    private TextView title;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_layout);
        title = (TextView) findViewById(R.id.title);
        title.setText("游戏中");
    }
}
