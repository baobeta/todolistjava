package com.example.todo;


import com.example.todo.entity.Role;
import com.example.todo.entity.User;
import com.example.todo.repository.RoleRepository;
import com.example.todo.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class UserRepositoryTest {

    @Autowired
    public UserRepository userRepo;

    @Autowired
    public RoleRepository roleRepo;

    @Test
    public void testCreateUser() {
        User user1 = new User();
        user1.setEmail("baobeta@gmail.com");
        user1.setFirstName("Bao");
        user1.setLastName("Le");
        user1.setPassword("123456");
        Set<Role> role = new HashSet<Role>();
        role.add(roleRepo.findById(3).get());
        user1.setRoles(role);
        User result = userRepo.save(user1);
    }

    @Test
    public void testUpdateUser() {
        User user2 = userRepo.findById(1).get();
        user2.setEmail("baobeta2907@gmail.com");
        User result = userRepo.save(user2);
        assertThat(result.getId()).isGreaterThan(0);

    }

    @Test
    public void testDeleteUser(){
        userRepo.deleteById(1);

    }
}
