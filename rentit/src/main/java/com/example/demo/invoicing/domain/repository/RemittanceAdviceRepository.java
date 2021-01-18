package com.example.demo.invoicing.domain.repository;

import com.example.demo.invoicing.domain.model.RemittanceAdvice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RemittanceAdviceRepository extends JpaRepository<RemittanceAdvice, Long> {
}
