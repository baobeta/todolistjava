package com.example.todo.controller.task;


import com.example.todo.entity.Category;
import com.example.todo.entity.Role;
import com.example.todo.entity.Task;
import com.example.todo.entity.User;
import com.example.todo.handleException.CategoryNotFoundException;
import com.example.todo.handleException.TaskNotFoundException;
import com.example.todo.service.CategoryService;
import com.example.todo.service.TaskService;
import com.example.todo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
public class TaskController {
    @Autowired
    public TaskService service;

    @Autowired
    public CategoryService categoryService;

    @Autowired
    public UserService userService;

    @GetMapping("/tasks")
    public String listAll(Model model) {
        return listByPage(1,model,"title","asc",null);
    }

    @GetMapping("/tasks/page/{pageNum}")
    public String listByPage(
            @PathVariable(name="pageNum") int pageNum, Model model,
            @Param("sortField") String sortField,
            @Param("sortDir") String sortDir,
            @Param("keyword") String keyword) {

        // pagination
        Page<Task> page = service.listByPage(pageNum, sortField, sortDir,keyword);
        List<Task> listTasks = page.getContent();


        // setup number in button
        long startCount = (pageNum-1) * TaskService.TASKS_PER_PAGE +1;
        long endCount= startCount + TaskService.TASKS_PER_PAGE -1;
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
        model.addAttribute("listTasks",listTasks);

        return "tasks/tasks";
    }

    @GetMapping("/tasks/new")
    public String newTask(Model model) {
        List<Category> listCategories = categoryService.findAll();
        List<User> listUsers = userService.listAll();
        // create new task
        Task task = new Task();

        // set some attribute
        model.addAttribute("task",task);
        model.addAttribute("listCategories",listCategories);
        model.addAttribute("listUsers",listUsers);
        model.addAttribute("pageTitle","Create New Task");
        return "tasks/task_form";
    }

    @PostMapping("/tasks/save")
    public String saveTask(Task task, RedirectAttributes redirectAttributes) throws IOException {

        service.save(task);
        redirectAttributes.addFlashAttribute("message","The user has been saved successfully.");
        return getRedirectURLtoAffectedTask(task);
    }

    private String getRedirectURLtoAffectedTask(Task task) {
        return "redirect:/tasks/page/1?sortField=id&sortDir=asc&keyword="+task.getId();
    }

    @GetMapping("/tasks/edit/{id}")
    public String editTask(@PathVariable(name="id") Integer id,
                               Model model,
                               RedirectAttributes redirectAttributes) {
        try {
            List<Category> listCategories = categoryService.findAll();
            List<User> listUsers = userService.listAll();
            Task task = service.get(id);
            model.addAttribute("task", task);
            model.addAttribute("listCategories",listCategories);
            model.addAttribute("listUsers",listUsers);
            model.addAttribute("pageTitle","Edit task (ID: " + id+ ")");
            return "tasks/task_form";

        } catch ( TaskNotFoundException ex) {
            redirectAttributes.addFlashAttribute("message",ex.getMessage());
            return "redirect:/tasks";
        }



    }

    @GetMapping("/tasks/delete/{id}")
    public String deleteCategory(@PathVariable(name="id") Integer id,
                                 Model model,
                                 RedirectAttributes redirectAttributes) {
        try {
            service.delete(id);
            redirectAttributes.addFlashAttribute("message","The task ID "+ id + " has been deleted successfully");

        } catch (TaskNotFoundException ex) {
            redirectAttributes.addFlashAttribute("message",ex.getMessage());
        }
        return "redirect:/tasks";

    }


}
