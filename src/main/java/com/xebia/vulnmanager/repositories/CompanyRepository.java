package com.xebia.vulnmanager.repositories;

import com.xebia.vulnmanager.models.company.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {

}
