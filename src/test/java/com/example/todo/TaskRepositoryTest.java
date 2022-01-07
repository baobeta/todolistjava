package com.example.todo;

import com.example.todo.entity.Task;
import com.example.todo.entity.User;
import com.example.todo.repository.TaskRepository;
import com.example.todo.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class TaskRepositoryTest {

    @Autowired
    public TaskRepository repository;

    @Autowired
    public UserRepository repositoryUser;

    @Test
    public void testGetListTaskByEmail() {
        List<Task> tasks = repository.getTaskByUserEmail("baobeta@gmail.com");
        tasks.forEach(c->System.out.println(c.getTitle()));
        assertThat(tasks.size()).isGreaterThan(0);
    }

    @Test
    public void testCountTaskUncompled() {
        User user =  repositoryUser.findById(7).get();
        Integer count = repository.countTaskByUserAndCompleteFalse(7);
        System.out.println(count);
        assertThat(count).isGreaterThan(0);
    }
}
