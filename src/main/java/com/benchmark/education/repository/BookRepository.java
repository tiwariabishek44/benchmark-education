package com.benchmark.education.repository;

import com.benchmark.education.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {

    List<Book> findBySubjectId(int id);

    List<Book> findByBookType(Book.BookType bookType);

    List<Book> findByFileLocation(String s);
}
