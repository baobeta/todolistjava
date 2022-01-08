package com.example.todo.controller.login;

import com.example.todo.entity.Mail;
import com.example.todo.entity.Role;
import com.example.todo.entity.User;
import com.example.todo.repository.SendMailService;
import com.example.todo.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashSet;
import java.util.Set;

@Controller
public class LoginController {



    @Autowired
    public CategoryService categoryService;

    @Autowired
    public UserService userService;

    @Autowired
    public RoleService roleService;

    @Autowired
    public SendMailService sendMailService;


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

    @GetMapping("/forgetpassword")
    public String getPassword(Model model) {
        User user = new User();
        user.setEnabled(true);
        model.addAttribute("user",user);
        return "forgot";
    }

    @PostMapping("/forgetpassword")
    public String rePassword(User user,Model model, RedirectAttributes redirectAttributes) {
        if(userService.checkUserExists(user)) {
            String password = GeneratePassword.generatePassword(6);
            User userReset = userService.getUserByEmail(user.getEmail());
            userReset.setPassword(password);
            userService.save(userReset);
            Mail mail = new Mail();
            mail.setRecipient(user.getEmail());
            mail.setSubject("Reset your password");
            mail.setMessage(" This is new password for you: "+password);
            sendMailService.sendMail(mail);
            redirectAttributes.addFlashAttribute("message","Please check your email for notifications!");
            return "redirect:/login";
        } else {
            redirectAttributes.addFlashAttribute("message","Account does not exist, please register!");
            return "redirect:/login";
        }

    }

}
