package com.example.todo.config;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.example.todo.entity.Role;
import com.example.todo.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;



public class TodoUserDetails implements UserDetails {


    private User user ;



    public TodoUserDetails(User user) {
        this.user = user;
    }




    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<Role> roles =user.getRoles();

        List<SimpleGrantedAuthority> authories = new ArrayList<>();
        //add role to granted authority
        for(Role role : roles) {
            authories.add(new SimpleGrantedAuthority(role.getName()));
        }

        return authories;
    }

    @Override
    public String getPassword() {
        // return password
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        //return email
        return user.getEmail();
    }

    public String getFullname() {
        return this.user.getFirstName() + " " + this.user.getLastName();
    }


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.isEnabled();
    }

    public void setFirstName(String firstName) {
        this.user.setFirstName(firstName);
    }

    public void setLastName(String lastName) {
        this.user.setLastName(lastName);
    }

}