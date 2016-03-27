package com.test.androidgroup.flyingchess;

/**
 * Created by kehan on 16-3-27.
 */
import android.app.Activity;
import android.os.Bundle;

public class FlyingChessActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
}
