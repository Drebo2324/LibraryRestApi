package com.training.restApi.repositories;

import com.training.restApi.domain.entities.BookEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends CrudRepository <BookEntity, String>, PagingAndSortingRepository<BookEntity, String> {
}

//PagingAndSortingRepository<T, ID> gives methods for pagination
//pages are like lists but with metadata. Allows for better query manipulation
