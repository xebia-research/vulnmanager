package com.xebia.vulnmanager.repositories;

import com.xebia.vulnmanager.models.nmap.objects.NMapReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NMapRepository extends JpaRepository<NMapReport, Long> {

}
