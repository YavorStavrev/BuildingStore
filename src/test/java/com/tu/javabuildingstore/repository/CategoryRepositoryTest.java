package com.tu.javabuildingstore.repository;

import com.tu.javabuildingstore.model.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository repository;

    @Test
    void findByName_returnsSavedEntity() {
        Category cat = new Category("Tools");
        repository.save(cat);

        Optional<Category> found = repository.findByName("Tools");

        assertTrue(found.isPresent());
        assertEquals("Tools", found.get().getName());
    }

    @Test
    void existsByName_trueWhenPresent() {
        repository.save(new Category("Paint"));
        assertTrue(repository.existsByName("Paint"));
        assertFalse(repository.existsByName("Missing"));
    }
}