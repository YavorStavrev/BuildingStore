package com.tu.javabuildingstore.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "categories")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {

    @Id
    @SequenceGenerator(name = "category_sequence", allocationSize = 100)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "category_sequence")
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<Product> products = new HashSet<>();

    public Category(String name) {
        this.name = name;
    }
}
