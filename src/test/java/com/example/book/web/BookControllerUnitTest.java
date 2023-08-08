package com.example.book.web;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

// 단위테스트 컨트롤러만, 필터이런거는 뜬다. ControllerAdvice 메모리 뜬다.
// Junit5에서는 @Runwith 대신에 @ExtendWith를 쓴다.
@WebMvcTest
@ExtendWith(SpringExtension.class)//스프링환경으로 테스트하기 위해 필수
public class BookControllerUnitTest {
    
}
