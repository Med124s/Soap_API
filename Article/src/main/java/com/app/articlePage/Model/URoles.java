package com.app.articlePage.Model;


import javax.persistence.*;

@Entity
@Table(name = "roles")

public class URoles {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private ERoles name;

    public URoles(ERoles name) {
        this.name = name;
    }

    public URoles() {

    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ERoles getName() {
        return name;
    }

    public void setName(ERoles name) {
        this.name = name;
    }
}
