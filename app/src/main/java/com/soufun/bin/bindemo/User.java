package com.soufun.bin.bindemo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @描述
 * @入口
 * @进入要传的值
 * @备注说明
 */

public class User implements Parcelable {

    public int userId;
    public String userName;
    public boolean isMale;

    public User(int userId , String userName , boolean isMale){
        this.userId = userId;
        this.userName = userName;
        this.isMale = isMale;
    }


    protected User(Parcel in) {
        userId = in.readInt();
        userName = in.readString();
        isMale = in.readByte() != 0;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(userId);
        dest.writeString(userName);
        dest.writeByte((byte) (isMale ? 1 : 0));
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("User{");
        sb.append("userId=").append(userId);
        sb.append(", userName='").append(userName).append('\'');
        sb.append(", isMale=").append(isMale);
        sb.append('}');
        return sb.toString();
    }
}
