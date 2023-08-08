package com.example.book.web;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;

// import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

// import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.assertEquals;
// import static org.mockito.Mockito.when;
// import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
// import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import com.example.book.domain.Book;
import com.example.book.domain.BookRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

//인테그리 통합테스트 (모든 Bean들을 똑같이 IoC에 올리고 테스트)
//bean 메모리 저장된 싱글톤방식 저장된 객체들
//WebEnvironment.MOCK = 실제 톰켓을 올리는게 아니라, 다른 톰켓으로 테스트
//WebEnvironment.RANDOM_PORT = 실제 톰켓으로 테스트
@Slf4j
@AutoConfigureMockMvc // 이걸 써야 모키토가 IoC에 등록된다.
@Transactional // 각각의 테스트함수가 종료될때마다 트랜잭션을 rollback 해주는 어노테이션
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
public class BookControllerIntegreTest {

    // MockMvc 컨트롤러 주소 확인?
    @Autowired
    private MockMvc mockMvc;

    // findAll 테스트할 때 데이터 아무것도 없어서
    @Autowired
    private BookRepository bookRepository;

    // 전체 실행할 때 오토인크레멘트 처리때문에
    @Autowired
    private EntityManager entityManager;

    // 전체 실행할 때 오토인크레멘트 처리때문에
    @BeforeEach
    public void init() {
        entityManager.createNativeQuery("ALTER TABLE book ALTER COLUMN id RESTART WITH 1").executeUpdate();// h2 sql 쿼리
    }
    // BeforeEach와 AfterEach 만들어서 진행!!

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
        // 스텁? 통합이니깐 실제 service가 실행됨.
        // when(bookService.저장하기(book)).thenReturn(new Book(1L, "테스트", "테스트2"));

        // when (테스트 실행)
        ResultActions resultAction = mockMvc.perform(post("/book").contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .accept(MediaType.APPLICATION_JSON));

        // then (검증)
        resultAction
                .andExpect(status().isCreated()) // 201을 기대
                .andExpect(jsonPath("$.title").value("책제목"))
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    public void findAll_테스트() throws Exception {
        // given
        List<Book> books = new ArrayList<>();
        books.add(new Book(null, "테스트1", "작가1"));
        books.add(new Book(null, "테스트2", "작가2"));
        books.add(new Book(null, "테스트3", "작가3"));
        // when(bookService.모두가져오기()).thenReturn(books);
        bookRepository.saveAll(books);

        // when
        ResultActions resultAction = mockMvc.perform(get("/book")
                .accept(MediaType.APPLICATION_JSON));

        // then
        resultAction
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(3)))
                .andExpect(jsonPath("$.[0].title").value("테스트1"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void findById_테스트() throws Exception {
        // given
        List<Book> books = new ArrayList<>();
        books.add(new Book(null, "테스트1", "작가1"));
        books.add(new Book(null, "테스트2", "작가2"));
        books.add(new Book(null, "테스트3", "작가3"));
        // when(bookService.모두가져오기()).thenReturn(books);
        bookRepository.saveAll(books);

        Long id = 1L;
        // when(bookService.한건가져오기(id)).thenReturn(new Book(1L, "테스트1", "작가1"));

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
        List<Book> books = new ArrayList<>();
        books.add(new Book(null, "테스트1", "작가1"));
        books.add(new Book(null, "테스트2", "작가2"));
        books.add(new Book(null, "테스트3", "작가3"));
        // when(bookService.모두가져오기()).thenReturn(books);
        bookRepository.saveAll(books);

        Long id = 1L;
        Book book = new Book(null, "수정하기", "책저자");
        String content = new ObjectMapper().writeValueAsString(book);
        // when(bookService.수정하기(id, book)).thenReturn(new Book(1L, "수정하기", "작가1"));

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
        List<Book> books = new ArrayList<>();
        books.add(new Book(null, "테스트1", "작가1"));
        books.add(new Book(null, "테스트2", "작가2"));
        books.add(new Book(null, "테스트3", "작가3"));
        // when(bookService.모두가져오기()).thenReturn(books);
        bookRepository.saveAll(books);
        // when(bookService.삭제하기(id)).thenReturn("ok");

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
