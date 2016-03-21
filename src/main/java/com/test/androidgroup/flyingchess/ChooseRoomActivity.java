package com.test.androidgroup.flyingchess;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by kehan on 16-3-21.
 */
public class ChooseRoomActivity extends Activity {

    private TextView title;
    private ImageView userImage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_room_layout);
        title = (TextView) findViewById(R.id.title);
        userImage = (ImageView) findViewById(R.id.user_image);
        title.setText("房间");
    }
}
