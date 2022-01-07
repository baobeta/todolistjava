package com.example.todo.repository;

import com.example.todo.entity.Category;
import com.example.todo.entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface TaskRepository extends PagingAndSortingRepository<Task, Integer> {


    public Long countById(Integer id);

    public Task getTaskByTitle(String title);

    @Query("SELECT c FROM Task c WHERE CONCAT(c.id,' ',c.title) LIKE %?1%" )
    public Page<Task> findAll(String keyword, Pageable pageable);
}
