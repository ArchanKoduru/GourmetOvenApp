package com.gourmetoven.gourmetapp.repository;

import com.gourmetoven.gourmetapp.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Integer>, JpaSpecificationExecutor<Users> {
    Optional<Users> findByUsername(String username);

    @Query("SELECT MAX(u.id) FROM Users u")
    Long findMaxUsersId();
}
