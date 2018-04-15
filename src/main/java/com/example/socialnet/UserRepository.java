package com.example.socialnet;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findByNameAndSurname(String name, String surname);

    List<User> findByName(String name);

    List<User> findBySurname(String surname);

    Optional<User> findByUsername(String username);

}