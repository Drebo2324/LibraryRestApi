package com.training.restApi.services.impl;

import com.training.restApi.domain.entities.BookEntity;
import com.training.restApi.repositories.BookRepository;
import com.training.restApi.services.BookService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class BookServiceImpl implements BookService {
    private BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public BookEntity save(String isbn, BookEntity bookEntity) {
        //ensure that entity isbn is the same as the one passed in method
        bookEntity.setIsbn(isbn);
        return bookRepository.save(bookEntity);
    }

    @Override
    public List<BookEntity> findAll() {
        List<BookEntity> listOfBooks = StreamSupport.stream(
                        bookRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());

        return listOfBooks;
    }

    @Override
    public Page<BookEntity> findAll(Pageable pageable) {
        return bookRepository.findAll(pageable);
    }

    @Override
    public Optional<BookEntity> findOne(String isbn) {
        return bookRepository.findById(isbn);
    }

    @Override
    public boolean ifExist(String isbn) {
        return bookRepository.existsById(isbn);
    }

    @Override
    public BookEntity partialUpdate(String isbn, BookEntity bookEntity) {
        bookEntity.setIsbn(isbn);

        return bookRepository.findById(isbn).map(retrievedBook -> {
            Optional.ofNullable(bookEntity.getTitle()).ifPresent(retrievedBook::setTitle);
            Optional.ofNullable(bookEntity.getAuthor()).ifPresent(retrievedBook::setAuthor);

            return bookRepository.save(retrievedBook);
        }).orElseThrow(() -> new RuntimeException("Book does not exist"));
    }

    @Override
    public void delete(String isbn) {
        bookRepository.deleteById(isbn);
    }

    @Override
    public void deleteAll() {
        bookRepository.deleteAll();
    }
}
