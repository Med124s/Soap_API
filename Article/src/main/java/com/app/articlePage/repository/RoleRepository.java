package com.app.articlePage.repository;

import com.app.articlePage.Model.ERoles;
import com.app.articlePage.Model.URoles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<URoles,Long> {
    Optional<URoles>findByName(ERoles type);

}