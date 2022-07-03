// IBookSizeChangeListener.aidl
package com.howie.multiple_process.listener;
import com.howie.multiple_process.bean.Book;
// Declare any non-default types here with import statements

interface IBookSizeChangeListener {
    void bookSizeChange(in List<Book> books);
}