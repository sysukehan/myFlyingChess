package com.test.androidgroup.flyingchess;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by kehan on 16-3-21.
 */
public class PersonalInformationActivity extends Activity {
    private ImageView image;
    private TextView title;
    @Override
    public void onCreate(Bundle savedInstancestate) {
        super.onCreate(savedInstancestate);
        setContentView(R.layout.information_layout);
        image = (ImageView) findViewById(R.id.user_image);
        title = (TextView) findViewById(R.id.title);
        title.setText("个人资料");
        image.setImageResource(R.mipmap.config);
    }
}
