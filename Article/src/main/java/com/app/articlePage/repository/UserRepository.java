package com.app.articlePage.repository;

import com.app.articlePage.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
        Optional<User> findByName(String name); //optional for gérer errors exceptions like nullPointer..
        boolean existsByName(String name);
        boolean existsByEmail(String email);

}

