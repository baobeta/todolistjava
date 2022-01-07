package com.example.todo.service;
import com.example.todo.entity.Task;
import com.example.todo.entity.User;
import com.example.todo.handleException.TaskNotFoundException;
import com.example.todo.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class TaskService {
    @Autowired
    public TaskRepository repo ;

    public static final int TASKS_PER_PAGE =4;

    public List<Task> listAll() {
        return (List<Task>) repo.findAll(Sort.by("title").ascending());
    }

    public Page<Task> listByPage(int pageNum, String sortField, String sortDir, String keyword ) {
        Sort sort = Sort.by(sortField);

        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

        Pageable pageable = PageRequest.of(pageNum-1,TASKS_PER_PAGE, sort);

        if(keyword!=null) {
            return repo.findAll(keyword, pageable);
        }
        return repo.findAll(pageable);
    }


    public Task save(Task task) {
        return repo.save(task);

    }

    public boolean isTitleUnique(Integer id, String title) {
        Task taskByTitle = repo.getTaskByTitle(title);

        if(taskByTitle == null) return true;
        boolean isCreateNew= (id==null);
        if(isCreateNew) {
            if(taskByTitle != null) return false;
        } else {
            if(taskByTitle.getId()!=id) {
                return false;
            }
        }
        return true;
    }

    public Task get(Integer id) throws TaskNotFoundException {
        try {
            return repo.findById(id).get();
        } catch(NoSuchElementException ex) {
            throw new TaskNotFoundException("Could not find any task with ID "+ id);
        }

    }

    public void delete(Integer id) throws TaskNotFoundException {
        Long countById = repo.countById(id);
        if(countById == null || countById == 0) {
            throw new TaskNotFoundException("Could not find any task with ID "+ id);
        }
        repo.deleteById(id);
    }

    public List<Task> getTaskByEmail(String email) {
        return repo.getTaskByUserEmail(email);
    }

    public void updateCompletedTask (Integer id, boolean completed) {
        repo.updateCompletedStatus(id, completed);
    }

    public Integer countTaskUncompleted (User user) {
        return repo.countTaskByUserAndCompleteFalse(user.getId());
    }
}
