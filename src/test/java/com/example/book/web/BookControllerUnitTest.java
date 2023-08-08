package com.example.book.web;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
// import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.http.MediaType;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

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
    // log.info("save_테스트() 시작 ======================================");
    // Book book = bookService.저장하기(new Book(null, "책제목", "책저자"));
    // System.out.println("book: " + book);
    // }

    // BDDMockito 패턴 given, when, then
    @Test
    public void save_테스트() throws Exception {
        // given (테스트를 하기 위한 준비)
        Book book = new Book(null, "책제목", "책저자");

        String content = new ObjectMapper().writeValueAsString(book);
        // 바꾸려면 ObjectMapper.readvalue로 바꾸면 된다.
        // new ObjectMapper().readValue(content, Book.class); object로 바꾼다. gson!! json을
        // string

        log.info(content);
        // 스텁?
        when(bookService.저장하기(book)).thenReturn(new Book(1L, "테스트", "테스트2"));

        // when (테스트 실행)
        ResultActions resultAction = mockMvc.perform(post("/book").contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .accept(MediaType.APPLICATION_JSON));

        // then (검증)
        resultAction
                .andExpect(status().isCreated()) // 201을 기대
                .andExpect(jsonPath("$.title").value("테스트"))
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    public void findAll_테스트() throws Exception {
        // given
        List<Book> books = new ArrayList<>();
        books.add(new Book(1L, "테스트1", "작가1"));
        books.add(new Book(2L, "테스트2", "작가2"));
        when(bookService.모두가져오기()).thenReturn(books);

        // when
        ResultActions resultAction = mockMvc.perform(get("/book")
                .accept(MediaType.APPLICATION_JSON));

        // then
        resultAction
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andExpect(jsonPath("$.[0].title").value("테스트1"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void findById_테스트() throws Exception {
        // given
        Long id = 1L;
        when(bookService.한건가져오기(id)).thenReturn(new Book(1L, "테스트1", "작가1"));

        // when
        ResultActions resultAction = mockMvc.perform(get("/book/{id}", id)
                .accept(MediaType.APPLICATION_JSON));

        // then
        resultAction
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("테스트1"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void update_테스트() throws Exception {
        // given

        Long id = 1L;
        Book book = new Book(null, "수정하기", "책저자");
        String content = new ObjectMapper().writeValueAsString(book);
        when(bookService.수정하기(id, book)).thenReturn(new Book(1L, "수정하기", "작가1"));

        // when (테스트 실행)
        ResultActions resultAction = mockMvc.perform(put("/book/{id}", id).contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .accept(MediaType.APPLICATION_JSON));

        // then
        resultAction
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("수정하기"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void delete_테스트() throws Exception {
        // given
        Long id = 1L;
        when(bookService.삭제하기(id)).thenReturn("ok");

        // when (테스트 실행)
        ResultActions resultAction = mockMvc.perform(delete("/book/{id}", id).accept(MediaType.TEXT_PLAIN));

        // then
        resultAction
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
            
        MvcResult requestResult = resultAction.andReturn();
        String result = requestResult.getResponse().getContentAsString();
        assertEquals("ok", result);
    }
}
