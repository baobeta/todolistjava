package com.example.todo.controller.category;

import com.example.todo.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CategoryRestController {
    @Autowired
    public CategoryService service;

    @PostMapping("/categories/check_title")
    public String checkDuplicateEmail(@Param("id") Integer id, @Param("title") String title) {
        return service.isTitleUnique(id, title) ? "OK" : "Duplicated";
    }

}
