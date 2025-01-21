package com.training.restApi;

import com.training.restApi.domain.dto.AuthorDto;
import com.training.restApi.domain.dto.BookDto;
import com.training.restApi.domain.entities.AuthorEntity;
import com.training.restApi.domain.entities.BookEntity;

public class TestDataUtil {
    private TestDataUtil() {
    }


    public static AuthorEntity createTestAuthorA() {
        return AuthorEntity.builder()
                .id(1L)
                .name("Hugh Mungus")
                .age(69)
                .build();
    }

    public static AuthorEntity createTestAuthorB() {
        return AuthorEntity.builder()
                .id(2L)
                .name("IC Weiner")
                .age(85)
                .build();
    }

    public static AuthorDto createTestAuthorDto() {
        return AuthorDto.builder()
                .id(4L)
                .name("Author Dto")
                .age(99)
                .build();
    }

    public static BookEntity createTestBookA(final AuthorEntity author) {
        return BookEntity.builder()
                .isbn("123abc").title("Book 1").author(author)
                .build();
    }

    public static BookEntity createTestBookB(final AuthorEntity author) {
        return BookEntity.builder()
                .isbn("213abc").title("Book 2").author(author)
                .build();
    }

    public static BookDto createTestBookDtoA(final AuthorDto author) {
        return BookDto.builder()
                .isbn("123abc").title("Book Dto").author(author)
                .build();
    }
}
