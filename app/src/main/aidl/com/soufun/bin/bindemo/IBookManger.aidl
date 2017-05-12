// IBookManger.aidl
package com.soufun.bin.bindemo;

// Declare any non-default types here with import statements
import com.soufun.bin.bindemo.Book;
import com.soufun.bin.bindemo.IOnNewBookArrivedListener;
interface IBookManger {
    List<Book> getBookList();
    void addBook(in Book book);
    void registerOnNewBookArrivedListener(IOnNewBookArrivedListener listener);
    void unregisterOnNewBookArrivedListener(IOnNewBookArrivedListener listener);
}
