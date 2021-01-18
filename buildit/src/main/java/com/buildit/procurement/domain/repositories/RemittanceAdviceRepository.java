package com.buildit.procurement.domain.repositories;

import com.buildit.procurement.domain.model.RemittanceAdvice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RemittanceAdviceRepository extends JpaRepository<RemittanceAdvice, Long> {
}
