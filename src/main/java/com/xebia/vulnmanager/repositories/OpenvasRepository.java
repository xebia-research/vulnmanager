package com.xebia.vulnmanager.repositories;

import com.xebia.vulnmanager.models.openvas.objects.OpenvasReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OpenvasRepository extends JpaRepository<OpenvasReport, Long> {

}
