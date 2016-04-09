package com.test.androidgroup.flyingchess;

import android.os.Handler;
import com.test.androidgroup.flyingchess.AnotherPlayer.Player;

/**
 * Created by 伍 on 2016/4/6.
 */
public class GameInfo {
    private static Player user;
    public static MessageProcess mp;

    static void setUser(Player player) { user = player;}
    static Player getUser() {return user;}

    static void setHandler(Handler handler) {
        mp.sendHandler = handler;
    }

}
