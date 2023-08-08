package com.example.book.web;

import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
// import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.http.MediaType;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
// import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import com.example.book.domain.Book;
import com.example.book.service.BookService;
// import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
// import com.fasterxml.jackson.databind.ObjectReader;

import lombok.extern.slf4j.Slf4j;

// 단위테스트 컨트롤러만, 필터이런거는 뜬다. ControllerAdvice 메모리 뜬다.
// Junit5에서는 @WebMvcTest에 @ExtendWith 등이 들어있다.
@Slf4j
@WebMvcTest
public class BookControllerUnitTest {
    
    @Autowired
    private MockMvc mockMvc;

    @MockBean // IoC에 등록된다.
    private BookService bookService;

    // @Test
    // public void save_테스트() throws Exception {
    //     log.info("save_테스트() 시작 ======================================");
    //     Book book = bookService.저장하기(new Book(null, "책제목", "책저자"));
    //     System.out.println("book: " + book);
    // }

    //BDDMockito 패턴 given, when, then
    @Test
    public void save_테스트() throws Exception {
        //given (테스트를 하기 위한 준비)
        Book book = new Book(null, "책제목", "책저자");

        String content = new ObjectMapper().writeValueAsString(book);
        // 바꾸려면 ObjectMapper.readvalue로 바꾸면 된다.
        // new ObjectMapper().readValue(content, Book.class); object로 바꾼다. gson!! json을 string
        
        log.info(content);
        //스텁?
        when(bookService.저장하기(book)).thenReturn(new Book(1L, "테스트", "테스트2"));

        //when (테스트 실행)
        ResultActions resultAction = mockMvc.perform(post("/book").contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .accept(MediaType.APPLICATION_JSON));
        
        //then (검증)
        resultAction
            .andExpect(status().isCreated()) // 201을 기대
            .andExpect(jsonPath("$.title").value("테스트"))
            .andDo(MockMvcResultHandlers.print());

    }
}
