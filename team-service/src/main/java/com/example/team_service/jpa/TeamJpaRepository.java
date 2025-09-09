package com.example.team_service.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TeamJpaRepository extends JpaRepository<Team, Long> {
    Optional<Team> findByName(String name);
}
