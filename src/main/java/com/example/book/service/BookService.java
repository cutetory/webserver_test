package com.example.book.service;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; //자바 아님 주의!!
import com.example.book.domain.Book;
import com.example.book.domain.BookRepository;
import lombok.RequiredArgsConstructor;

//기능 정의, 트랜잭션관리 전체롤백 등...
@RequiredArgsConstructor //final이 붙은 필드를 인자값으로 하는 생성자를 만들어줌
@Service
public class BookService {
    private final BookRepository bookRepository;    
    
    @Transactional //함수가 종료될 때 commit할지 rollback할지 트랜잭션 관리하겠다.
    public Book 저장하기(Book book){
        return bookRepository.save(book);
    }

    //람다는 타입을 몰라도 되고, 인터페이스에 메서드 하나뿐이면 람다로 쓸 수 있다.
    @Transactional(readOnly = true) //JPA 변경감지라는 내부 기능 활성화 X, update시의 정합성을 유지해줌. insert의 유령데이터 현상(팬텀현상) 못막음
    public Book 한건가져오기(Long id){
        return bookRepository.findById(id).orElseThrow(()->new IllegalArgumentException("id를 확인해주세요"));
    }

    @Transactional(readOnly = true)
    public List<Book> 모두가져오기(){
        return bookRepository.findAll();
    }

    @Transactional
    public Book 수정하기(Long id, Book book){
        //더티체킹 update치기
        Book bookEntity = bookRepository.findById(id).orElseThrow(()->new IllegalArgumentException("id를 확인해주세요")); //영속화(book오브젝트) -> 영속성 컨텍스트 보관
        bookEntity.setTitle(book.getTitle());
        bookEntity.setAuthor(book.getAuthor());
        return bookEntity;
    }//함수 종료=>트랜잭션 종료=>영속화 되어있는 데이터를 DB로 갱신(flush)=> commit => 더티체킹

    @Transactional
    public String 삭제하기(Long id){
        bookRepository.deleteById(id); //오류가 터지면 익셉션을 타니까 신경쓰지말기
        return "ok";
    }

}
