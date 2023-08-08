package com.example.book.domain;

import org.springframework.data.jpa.repository.JpaRepository;

//@Repository //생략가능하다. JpaRepository를 상속받았기 때문에
public interface BookRepository extends JpaRepository<Book, Long>{ //ID의 타입은 Long이다.
    
}
