package com.example.todo;

import com.example.todo.entity.Role;
import com.example.todo.repository.RoleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class RoleRepositoryTest {
    @Autowired
    private RoleRepository repo;


    @Test
     public void createFirstRole() {
         Role roleAdmin = new Role("ADMIN","Manage Everything");
         Role result = repo.save(roleAdmin);
         assertThat(result.getId()).isGreaterThan(0);

     }

     @Test
    public void createSecondRole() {
        Role roleAdmin = new Role("MOD","Manage Category, task");
        Role result = repo.save(roleAdmin);
        assertThat(result.getId()).isGreaterThan(0);

    }
    @Test
    public void createThirdRole() {
        Role roleAdmin = new Role("User","Manage task");
        Role result = repo.save(roleAdmin);
        assertThat(result.getId()).isGreaterThan(0);

    }
}
