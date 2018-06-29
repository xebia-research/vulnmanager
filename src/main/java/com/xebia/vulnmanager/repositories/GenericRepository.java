package com.xebia.vulnmanager.repositories;

import com.xebia.vulnmanager.models.generic.GenericReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GenericRepository extends JpaRepository<GenericReport, Long> {

}
