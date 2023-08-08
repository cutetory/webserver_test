package com.example.book.web;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

//인테그리 통합테스트 (모든 Bean들을 똑같이 IoC에 올리고 테스트)
//bean 메모리 저장된 싱글톤방식 저장된 객체들
@SpringBootTest(webEnvironment = WebEnvironment.MOCK) //실제 톰켓이 아니라 다른 톰캣으로 테스트
public class BookControllerIntegreTest {
    
}
