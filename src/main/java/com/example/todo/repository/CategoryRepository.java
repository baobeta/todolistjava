package com.example.todo.repository;

import com.example.todo.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface CategoryRepository extends PagingAndSortingRepository<Category, Integer> {

    public Category getCategoryByTitle(String title);
    public Optional<Category> findById(Integer id);

    public Long countById(Integer id);

    @Query("SELECT c FROM Category c WHERE c.title LIKE %?1%" )
    public Page<Category> findAll(String keyword, Pageable pageable);
}
