// IBookManagerService.aidl
package com.howie.multiple_process;

import com.howie.multiple_process.bean.Book;
import com.howie.multiple_process.listener.IBookSizeChangeListener;

interface IBookManagerService {
    //获取book列表
    List<Book> getBookList();
    //增加书籍
    void addBook(inout Book book);

    //注册与反注册监听
    void registerListener(IBookSizeChangeListener listener);
    void unRegisterListener(IBookSizeChangeListener listener);
}