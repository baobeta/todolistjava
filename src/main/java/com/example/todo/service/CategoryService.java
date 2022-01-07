package com.example.todo.service;

import com.example.todo.entity.Category;
import com.example.todo.handleException.CategoryNotFoundException;
import com.example.todo.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class CategoryService {

    public static final int CATEGORIES_PER_PAGE =3;

    @Autowired
    public CategoryRepository repo;
    
    public List<Category> findAll() {
        return (List<Category>) repo.findAll(Sort.by("title").ascending());
    }


    public Page<Category> listByPage(int pageNum, String sortField, String sortDir, String keyword ) {
        Sort sort = Sort.by(sortField);

        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

        Pageable pageable = PageRequest.of(pageNum-1,CATEGORIES_PER_PAGE, sort);

        if(keyword!=null) {
            return repo.findAll(keyword, pageable);
        }
        return repo.findAll(pageable);
    }

    public Category save(Category Category) {
        boolean isUpdatingCategory =(Category.getId() != null);
        return repo.save(Category);

    }


    public boolean isTitleUnique(Integer id, String title) {
        Category categoryByTitle = repo.getCategoryByTitle(title);

        if(categoryByTitle == null) return true;
        boolean isCreateNew= (id==null);
        if(isCreateNew) {
            if(categoryByTitle != null) return false;
        } else {
            if(categoryByTitle.getId()!=id) {
                return false;
            }
        }
        return true;
    }

    public Category get(Integer id) throws CategoryNotFoundException {
        try {
            return repo.findById(id).get();
        } catch(NoSuchElementException ex) {
            throw new CategoryNotFoundException("Could not find any category with ID "+ id);
        }

    }

    public void delete(Integer id) throws CategoryNotFoundException {
        Long countById = repo.countById(id);
        if(countById == null || countById == 0) {
            throw new CategoryNotFoundException("Could not find any category with ID "+ id);
        }
        repo.deleteById(id);
    }


}
