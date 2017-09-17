package com.tochka.newsparser.repository;

import com.tochka.newsparser.domain.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority, String> {
}
