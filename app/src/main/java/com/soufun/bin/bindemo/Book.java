package com.soufun.bin.bindemo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @描述
 * @入口
 * @进入要传的值
 * @备注说明
 */

public class Book implements Parcelable {
    public int booid;
    public String bookName;

    public Book(int booid , String bookName){
        this.booid = booid;
        this.bookName = bookName;
    }

    private Book(Parcel in) {
        booid = in.readInt();
        bookName = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(booid);
        dest.writeString(bookName);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Book{");
        sb.append("booid=").append(booid);
        sb.append(", bookName='").append(bookName).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
