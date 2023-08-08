package com.example.book.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.book.domain.Book;
import com.example.book.domain.BookRepository;

//단위 테스트 (Service와 관련된 것들만 메모리에 로딩하면 됨.)
/***
 * 단위 테스트
 * BookRepository => 가짜 객체로 만들 수 있음.
 * Mock은 가짜로 객체 만드는 거
 * InjectMocks 해당 파일에 @Mock로 등록된 모든 애들을 주입받는다.
 */

@ExtendWith(MockitoExtension.class) //스프링을 테스트 할때 JUnit에게 알려주는 어노테이션
public class BookServiceUnitTest {

    @InjectMocks
    private BookService bookService; //얘를 테스트 해보고 싶음

    @Mock
    private BookRepository bookRepository;

    @Test
    public void 저장하기_테스트()
    {
        //BOD Mocikto방식
        //given
        Book book = new Book();
        book.setTitle("책제목1");
        book.setAuthor("책저자1");

        //stub - 동작지정
        when(bookRepository.save(book)).thenReturn(book);

        //test execute
        Book bookEntity = bookService.저장하기(book);

        //then
        assertEquals(bookEntity, book);
    }
}
