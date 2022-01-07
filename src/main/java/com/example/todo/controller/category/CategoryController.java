package com.example.todo.controller.category;

import com.example.todo.entity.Category;
import com.example.todo.handleException.CategoryNotFoundException;
import com.example.todo.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
public class CategoryController {
    @Autowired
    public CategoryService service;

    @GetMapping("/categories")
    public String listAll(Model model) {
        return listByPage(1,model,"title","asc",null);
    }

    @GetMapping("/categories/page/{pageNum}")
    public String listByPage(
            @PathVariable(name="pageNum") int pageNum, Model model,
            @Param("sortField") String sortField,
            @Param("sortDir") String sortDir,
            @Param("keyword") String keyword) {

        // pagination
        Page<Category> page = service.listByPage(pageNum, sortField, sortDir,keyword);
        List<Category> listcategories = page.getContent();


        // setup number in button
        long startCount = (pageNum-1) * service.CATEGORIES_PER_PAGE +1;
        long endCount= startCount + service.CATEGORIES_PER_PAGE -1;
        if(endCount>page.getTotalElements()) {
            endCount = page.getTotalElements();
        }
        // sort
        String reserveSortDir = sortDir.equals("asc") ? "desc" : "asc";

        model.addAttribute("currentPage",pageNum);
        model.addAttribute("totalPages",page.getTotalPages());
        model.addAttribute("startCount",startCount);
        model.addAttribute("endCount",endCount);
        model.addAttribute("totalItems",page.getTotalElements());
        model.addAttribute("sortField",sortField);
        model.addAttribute("sortDir",sortDir);
        model.addAttribute("reserveSortDir",reserveSortDir);
        model.addAttribute("keyword",keyword);
        model.addAttribute("listcategories",listcategories);

        return "categories/categories";
    }

    @GetMapping("/categories/delete/{id}")
    public String deleteCategory(@PathVariable(name="id") Integer id,
                             Model model,
                             RedirectAttributes redirectAttributes) {
        try {
            service.delete(id);
            redirectAttributes.addFlashAttribute("message","The category ID "+ id + "has been deleted successfully");

        } catch (CategoryNotFoundException ex) {
            redirectAttributes.addFlashAttribute("message",ex.getMessage());
        }
        return "redirect:/categories";

    }

    @GetMapping("/categories/new")
    public String newCategory(Model model) {

        Category category = new Category();

        // set some attribute
        model.addAttribute("category",category);
        model.addAttribute("pageTitle","Create New Category");
        return "categories/category_form";
    }

    @PostMapping("/categories/save")
    public String saveCategory(Category category , RedirectAttributes redirectAttributes) throws IOException {

        service.save(category);
        redirectAttributes.addFlashAttribute("message","The category has been saved successfully.");
        return getRedirectURLtoAffectedCategory(category);
    }

    private String getRedirectURLtoAffectedCategory(Category category) {
        return "redirect:/categories/page/1?sortField=id&sortDir=asc&keyword="+category.getTitle();
    }

    @GetMapping("/categories/edit/{id}")
    public String editCategory(@PathVariable(name="id") Integer id,
                           Model model,
                           RedirectAttributes redirectAttributes) {
        try {
            Category category = service.get(id);
            model.addAttribute("category", category);
            model.addAttribute("pageTitle","Edit Category (ID: " + id+ ")");
            return "categories/category_form";

        } catch (CategoryNotFoundException ex) {
            redirectAttributes.addFlashAttribute("message",ex.getMessage());
            return "redirect:/categories";
        }



    }



}
