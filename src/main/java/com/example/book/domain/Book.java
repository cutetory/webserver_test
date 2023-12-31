package com.example.book.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity //서버 실행시에 Object Relation Mapping이 됨. 테이블이 h2 생성됨
public class Book {
    @Id //PK를 해당 변수로 하겠다.
    @GeneratedValue(strategy = GenerationType.IDENTITY) //해당 데이터베이스 번호증가 전략을 따라간다.
    private Long id;
    private String title;
    private String author;

    // 이렇게 추후에 만들면 @Data 안쓰고 세터만 쓸 수 있다.
    // public static setBook(Dto dto)
    // {
    //     title = dto.getTitle();
    //     author = dto.getAuthor();
    // }
    
}
