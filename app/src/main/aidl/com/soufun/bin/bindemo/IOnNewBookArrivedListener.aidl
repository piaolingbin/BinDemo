// IOnNewBookArrivedListener.aidl
package com.soufun.bin.bindemo;

// Declare any non-default types here with import statements
import com.soufun.bin.bindemo.Book;
interface IOnNewBookArrivedListener {
    void onNewBookArrived(in Book newBook);
}
