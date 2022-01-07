package com.example.todo.handleException;

public class CategoryNotFoundException  extends Exception{
    public CategoryNotFoundException (String message) {
        super(message);
    }
}
