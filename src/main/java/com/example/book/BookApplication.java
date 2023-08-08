package com.example.book;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
// import org.springframework.context.ConfigurableApplicationContext;


// import com.example.book.domain.Book;
// import com.example.book.domain.BookRepository;

@SpringBootApplication
public class BookApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookApplication.class, args);


		// ConfigurableApplicationContext context = SpringApplication.run(BookApplication.class, args);
		// BookRepository bookRepository = context.getBean(BookRepository.class);

		// // 테스트 데이터 추가
		// Book book1 = new Book();
		// book1.setContent("첫 번째 메모");
		// bookRepository.save(book1);

		// Book book2 = new Book();
		// book2.setContent("두 번째 메모");
		// bookRepository.save(book2);

        // // 모든 메모 조회
        // bookRepository.findAll().forEach(book -> System.out.println(book.getId() + ": " + book.getContent()));
	}

}
