package com.letmeclean.domain.cleaner;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CleanerRepository extends JpaRepository<Cleaner, Long> {

    boolean existsByEmail(String email);
}
