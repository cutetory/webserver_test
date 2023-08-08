package com.example.book.web;

// import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
// import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import com.example.book.domain.Book;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

//인테그리 통합테스트 (모든 Bean들을 똑같이 IoC에 올리고 테스트)
//bean 메모리 저장된 싱글톤방식 저장된 객체들
//WebEnvironment.MOCK = 실제 톰켓을 올리는게 아니라, 다른 톰켓으로 테스트
//WebEnvironment.RANDOM_PORT = 실제 톰켓으로 테스트
@Slf4j
@AutoConfigureMockMvc // 이걸 써야 모키토가 IoC에 등록된다.
@Transactional //각각의 테스트함수가 종료될때마다 트랜잭션을 rollback 해주는 어노테이션
@SpringBootTest(webEnvironment = WebEnvironment.MOCK) 
public class BookControllerIntegreTest {

    //MockMvc 컨트롤러 주소 확인?
    @Autowired
    private MockMvc mockMvc;


    //BDDMockito 패턴 given, when, then
    @Test
    public void save_테스트() throws Exception {
        //given (테스트를 하기 위한 준비)
        Book book = new Book(null, "책제목", "책저자");

        String content = new ObjectMapper().writeValueAsString(book);
        // 바꾸려면 ObjectMapper.readvalue로 바꾸면 된다.
        // new ObjectMapper().readValue(content, Book.class); object로 바꾼다. gson!! json을 string
        
        log.info(content);
        //스텁? 통합이니깐 실제 service가 실행됨.
        // when(bookService.저장하기(book)).thenReturn(new Book(1L, "테스트", "테스트2"));

        //when (테스트 실행)
        ResultActions resultAction = mockMvc.perform(post("/book").contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .accept(MediaType.APPLICATION_JSON));
        
        //then (검증)
        resultAction
            .andExpect(status().isCreated()) // 201을 기대
            .andExpect(jsonPath("$.title").value("책제목"))
            .andDo(MockMvcResultHandlers.print());

    }


}
