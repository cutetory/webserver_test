package com.example.book.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

//단위테스트 DB관련된 Bean이 IoC에 등록되면 됨.

@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY) //가짜 DB로 테스트, Replace.NONE 실제 DB로 테스트
@DataJpaTest //Repository들을 다 IoC 등록해줌.
public class BookRepositoryUnitTest {

    // @Mock 할 필요없고, @DataJpaTest 해주면 됨.
    @Autowired
    private BookRepository bookRepository;

    @Test
    public void save_테스트()
    {
        //given
        Book book = new Book(null, "책제목1", "책저자1");

        //when
        Book bookEntity = bookRepository.save(book);

        //then
        assertEquals("책제목1", bookEntity.getTitle());
    }

}
