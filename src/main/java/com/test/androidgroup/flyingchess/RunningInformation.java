package com.test.androidgroup.flyingchess;

import com.test.androidgroup.flyingchess.Server.MessageProcess;

/**
 * Created by kehan on 16-3-27.
 */
public class RunningInformation {

    public static boolean isAnonymous;//是否匿名
    public static String playerId;//用户ID
    public static String playerName;//用户昵称
    public static String md5Password;//用户经过MD5加密后的密码
    public static int sumMatches;//线上游戏总局数
    public static double exceedSumMatches;//线上游戏总局数超过玩家百分比
    public static int winMatches;//线上游戏胜利总数
    public static double exceedWinMatches;//线上游戏胜利总数超过玩家百分比
    public static double percent;//线上游戏胜率
    public static double exceedPercent;//线上游戏胜率超过玩家百分比
    public static MessageProcess mp;//处理与服务器的连接，创建新Activity的时候要把这个句柄传下去

    //初始化
    public static void initial() {
        isAnonymous = true;//是否匿名
        playerId = "000000";//用户ID
        playerName = "NULL";//用户昵称
        md5Password = "000000";//用户经过MD5加密后的密码
        sumMatches = 0;//线上游戏总局数
        exceedSumMatches = 0;//线上游戏总局数超过玩家百分比
        winMatches = 0;//线上游戏胜利总数
        exceedWinMatches = 0;//线上游戏胜利总数超过玩家百分比
        percent = 0;//线上游戏胜率
        exceedPercent = 0;//线上游戏胜率超过玩家百分比
    }

}
