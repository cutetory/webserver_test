package com.example.book.web;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

// 단위테스트 컨트롤러만, 필터이런거는 뜬다. ControllerAdvice 메모리 뜬다.
// Junit5에서는 @WebMvcTest에 @ExtendWith 등이 들어있다.
@WebMvcTest
public class BookControllerUnitTest {
    
}
