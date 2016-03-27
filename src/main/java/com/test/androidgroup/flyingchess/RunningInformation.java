package com.test.androidgroup.flyingchess;

/**
 * Created by kehan on 16-3-27.
 */
public class RunningInformation {

    public static boolean isAnonymous = true;//是否匿名
    public static String playerId = "000000";//用户ID
    public static String playerName = "NULL";//用户昵称
    public static String md5Password = "000000";//用户经过MD5加密后的密码
    public static int sumMatches = 0;//线上游戏总局数
    public static double exceedSumMatches = 0;//线上游戏总局数超过玩家百分比
    public static int winMatches = 0;//线上游戏胜利总数
    public static double exceedWinMatches = 0;//线上游戏胜利总数超过玩家百分比
    public static double percent = 0;//线上游戏胜率
    public static double exceedPercent = 0;//线上游戏胜率超过玩家百分比
}
