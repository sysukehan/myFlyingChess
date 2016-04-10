package com.test.androidgroup.flyingchess;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

/**
 * Created by kehan on 16-4-9.
 */
public class StartActivity extends FlyingChessActivity {

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_layout);
        context = this;
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(context, LoginAndRegisterActivity.class);
                startActivity(intent);
                finish();
            }
        }, 4000);
    }
}
