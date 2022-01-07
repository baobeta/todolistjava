package com.example.todo.service;


import com.example.todo.entity.Role;
import com.example.todo.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class RoleService {

    @Autowired
    public RoleRepository repository;


    public Role findRoleUser() {
        return repository.findByName("USER");
    }
}
