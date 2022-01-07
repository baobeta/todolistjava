package com.example.todo.repository;

import com.example.todo.entity.Category;
import com.example.todo.entity.Task;
import com.example.todo.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface TaskRepository extends PagingAndSortingRepository<Task, Integer> {


    public Long countById(Integer id);

    @Query("SELECT COUNT(t) FROM Task t WHERE t.user.id =?1 AND t.isComplete = false")
    public Integer countTaskByUserAndCompleteFalse(Integer id);

    public Task getTaskByTitle(String title);

    public List<Task> getTaskByUserEmail(String email);

    @Query("UPDATE Task t SET t.isComplete = ?2 WHERE t.id = ?1")
    @Modifying
    public void updateCompletedStatus(Integer id, boolean completed);

    @Query("SELECT c FROM Task c WHERE CONCAT(c.id,' ',c.title) LIKE %?1%" )
    public Page<Task> findAll(String keyword, Pageable pageable);
}
