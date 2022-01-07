package com.example.todo.entity;


import javax.persistence.*;

@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;

    @Column(length =300, nullable = false)
    public String title;

    @Override
    public String toString() {
        return "Category{" +
                "title='" + title + '\'' +
                '}';
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
