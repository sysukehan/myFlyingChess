package com.test.androidgroup.flyingchess;

import android.os.Parcel;
import android.os.Parcelable;

import com.test.androidgroup.flyingchess.AnotherPlayer.Player;

import java.io.Serializable;
/**
 * Created by 伍 on 2016/4/4.
 */
public class RoomInfo implements Parcelable {
    private int RoomID;
    private String Host;
    private int PlayerNumber;
    private int RoomSize;
    public Player[] players = new Player[4];

    RoomInfo() {}
    RoomInfo(int roomID, String host,int playerNumber,int roomSize, Player player[]) {
        this.RoomID = roomID;
        this.Host = host;
        this.PlayerNumber = playerNumber;
        this.RoomSize = roomSize;
        this.players = player;
    }
    RoomInfo(Parcel in) {
        this.RoomID = in.readInt();
        this.Host = in.readString();
        this.PlayerNumber = in.readInt();
        this.RoomSize = in.readInt();
        for (int i = 0; i < PlayerNumber; i++) players[i] = (Player) in.readValue(Player.class.getClassLoader());
    }

    void addPlayer(Player player) {
        players[PlayerNumber] = player;
        PlayerNumber++;
    }

    void removePlayer(Player player) {
        int i = 0;
        while(player != players[i]) i++;
        for(; i < PlayerNumber; i++) {
            players[i] = players[i+1];
        }
        players[PlayerNumber-1] = null;
        PlayerNumber--;
    }

    void setRoomID(int roomID) { RoomID = roomID; }
    void setHost(String host) { Host = host; }
    void setPlayerNumber(int playerNumber) { PlayerNumber = playerNumber; }
    void setRoomSize(int roomSize) {RoomSize = roomSize;}
    int getRoomID() {return RoomID;}
    String getHost() {return Host;}
    int getPlayerNumber() {return PlayerNumber;}
    int getRoomSize() {return RoomSize;}
    Player[] getPlayers() {return players;}
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(RoomID);
        dest.writeString(Host);
        dest.writeInt(PlayerNumber);
        dest.writeInt(RoomSize);
        for (int i = 0; i < PlayerNumber; i++) dest.writeValue(players[i]);
    }

    public static final Creator<RoomInfo> CREATOR = new Creator<RoomInfo>() {

        @Override
        public RoomInfo createFromParcel(Parcel source) {
            return new RoomInfo(source);
        }

        @Override
        public RoomInfo[] newArray(int size) {
            return new RoomInfo[size];
        }
    };
}
