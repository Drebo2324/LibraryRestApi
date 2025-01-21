package com.training.restApi.services.impl;

import com.training.restApi.domain.entities.AuthorEntity;
import com.training.restApi.repositories.AuthorRepository;
import com.training.restApi.services.AuthorService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class AuthorServiceImpl implements AuthorService {

    //Need for CRUD methods
    private AuthorRepository authorRepository;

    public AuthorServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public AuthorEntity save(AuthorEntity authorEntity) {
        return authorRepository.save(authorEntity);
    }

    @Override
    public List<AuthorEntity> findAll() {
        //findAll() returns Iterable -> convert to Spliterator -> create stream -> collect stream into List
        List<AuthorEntity> listOfAuthors = StreamSupport.stream(authorRepository
                                .findAll()
                                .spliterator(),
                        false)
                .collect(Collectors.toList());

        return listOfAuthors;
    }

    @Override
    public Page<AuthorEntity> findAll(Pageable pageable) {
        return authorRepository.findAll(pageable);
    }

    @Override
    public Optional<AuthorEntity> findOne(Long id) {
        return authorRepository.findById(id);
    }

    @Override
    public boolean ifExist(Long id) {
        return authorRepository.existsById(id);
    }

    @Override
    public AuthorEntity partialUpdate(Long id, AuthorEntity authorEntity) {
        authorEntity.setId(id);

        //findById returns Optional(author)
        //map used to extract and transform the value inside the Optional if it is present
        return authorRepository.findById(id).map(retrievedAuthor -> {
            //Optional.ofNullable wraps potentially null values
            //ifPresent(existingAuthor::???) only used if Optional has a value
            //Optional.ofNullable handle partial updates, update only fields that are not null.
            //The Optional.ofNullable handle cases where individual fields within authorEntity might be null.
            Optional.ofNullable(authorEntity.getName()).ifPresent(retrievedAuthor::setName);
            Optional.ofNullable(authorEntity.getAge()).ifPresent(retrievedAuthor::setAge);
            //save updated author back into database
            return authorRepository.save(retrievedAuthor);
        }).orElseThrow(() -> new RuntimeException("Author does not exist"));
    }

    @Override
    public void delete(Long id) {
        authorRepository.deleteById(id);
    }

    @Override
    public void deleteAll() {
        authorRepository.deleteAll();
    }

}
