package com.test.androidgroup.flyingchess.AnotherPlayer;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by 伍 on 2016/4/4.
 */
public class Player implements Parcelable {
    public String user_id;
    public String user_Nick_Name;
    //private userImage;
    public Player() {}
    public Player(String user_id, String user_Nick_Name) {this.user_id = user_id; this.user_Nick_Name = user_Nick_Name;}
    public Player(Parcel in) {
        user_id = in.readString();
        user_Nick_Name = in.readString();
    }
    public void setUser_id(String user_id) {this.user_id = user_id;}
    public void setUser_Nick_Name(String user_Nick_Name) {this.user_Nick_Name = user_Nick_Name;}
    public String getUser_id() {return user_id;}
    public String getUser_Nick_Name() {return user_Nick_Name;}
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest,int flags) {
        dest.writeString(user_id);
        dest.writeString(user_Nick_Name);
    }
    public static final Creator<Player> CREATOR = new Creator<Player>() {

        @Override
        public Player createFromParcel(Parcel source) {
            return new Player(source);
        }

        @Override
        public Player[] newArray(int size) {
            return new Player[size];
        }
    };
}