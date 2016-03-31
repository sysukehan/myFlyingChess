package com.test.androidgroup.flyingchess;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by kehan on 16-3-30.
 */
public class BackToLoginAndRegisterReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        ActivityCollector.finishAll();
        Intent newIntent = new Intent(context, LoginAndRegisterActivity.class);
        newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(newIntent);
    }
}
