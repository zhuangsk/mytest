package com.zhuang.lucene.dao;

import com.zhuang.lucene.pojo.Book;

import java.util.List;

public interface BookDao {
    /**
     * 查询全部
     */
    List<Book> findAll();
}
