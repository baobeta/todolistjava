package com.example.todo.controller;

import com.example.todo.config.TodoUserDetails;
import com.example.todo.entity.Category;
import com.example.todo.entity.Role;
import com.example.todo.entity.Task;
import com.example.todo.entity.User;
import com.example.todo.handleException.TaskNotFoundException;
import com.example.todo.service.CategoryService;
import com.example.todo.service.RoleService;
import com.example.todo.service.TaskService;
import com.example.todo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class MainController {
    @Autowired
    private TaskService taskService;

    @Autowired
    public CategoryService categoryService;

    @Autowired
    public UserService userService;

    @Autowired
    public RoleService roleService;

    @GetMapping("")
    public String viewPageHome(@AuthenticationPrincipal TodoUserDetails loggeduser, Model model) {
        String email = loggeduser.getUsername();
        Task task = new Task();
        User user =userService.getUserByEmail(email);
        Integer countTaskUncompleted = taskService.countTaskUncompleted(user);

        List<Task> listTasks = taskService.getTaskByEmail(email);
        List<Category> listCategories = categoryService.findAll();


        model.addAttribute("task",task);
        model.addAttribute("listTasks",listTasks);
        model.addAttribute("listCategories",listCategories);
        model.addAttribute("countTaskUncompleted",countTaskUncompleted);
        return "index";
    }
    @GetMapping("/login")
    public String viewPageLogin() {
        return "login";
    }

    @GetMapping("/register")
    public String viewPageRegister(Model model) {
        User user = new User();
        user.setEnabled(true);
        model.addAttribute("user",user);
        return "register";
    }

    @PostMapping("/register")
    public String saveUser(User user,Model model, RedirectAttributes redirectAttributes) {
        Set<Role> role = new HashSet<Role>();
        role.add(roleService.findRoleUser());
        user.setRoles(role);
        user.setEnabled(true);
        if(userService.isEmailUnique(user.getId(),user.getEmail())) {
            userService.save(user);
            redirectAttributes.addFlashAttribute("message","The user has been saved successfully.");
            return "redirect:/login";
        }
        else {
            redirectAttributes.addFlashAttribute("message","Email already exists, please login or register for a new account ");
            return "redirect:/register";
        }

    }


    @PostMapping("/save")
    public String saveTask(Task task, RedirectAttributes redirectAttributes, @AuthenticationPrincipal TodoUserDetails loggeduser) throws IOException {

        String email = loggeduser.getUsername();

        User user =userService.getUserByEmail(email);
        task.setUser(user);
        task.setComplete(false);
        taskService.save(task);
        redirectAttributes.addFlashAttribute("message","The task has been saved successfully.");
        return "redirect:/";
    }

    @GetMapping("/{id}/completed/{status}")
    public String updateTaskStatus(@PathVariable("id") Integer id,
                                          @PathVariable("status") boolean enabled, RedirectAttributes redirectAttributes) {
        taskService.updateCompletedTask(id, enabled);;
        return "redirect:/";
    }

    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable(name="id") Integer id,
                                 Model model,
                                 RedirectAttributes redirectAttributes) {
        try {
            taskService.delete(id);
            redirectAttributes.addFlashAttribute("message","The task has been deleted successfully");

        } catch (TaskNotFoundException ex) {
            redirectAttributes.addFlashAttribute("message",ex.getMessage());
        }
        return "redirect:/";

    }

}
