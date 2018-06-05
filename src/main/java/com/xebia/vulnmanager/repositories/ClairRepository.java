package com.xebia.vulnmanager.repositories;

import com.xebia.vulnmanager.models.clair.objects.ClairReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClairRepository extends JpaRepository<ClairReport, Long> {

}
