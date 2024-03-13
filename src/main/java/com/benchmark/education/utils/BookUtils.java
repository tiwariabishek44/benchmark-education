package com.benchmark.education.utils;

import com.benchmark.education.entity.Book;

public class BookUtils {

    public static Book.BookType getBookType(String bookType){
        if("MCQ".equals(bookType)){
            return Book.BookType.MCQ;
        } else if ("FREE".equals(bookType)) {
            return Book.BookType.FREE;
        } else if ("PAID".equals(bookType)) {
            return Book.BookType.PAID;
        } else if ("PUBLICATIONS".equals(bookType)) {
            return Book.BookType.PUBLICATIONS;
        }

        return null;
    }
}

