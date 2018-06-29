package com.xebia.vulnmanager.repositories;

import com.xebia.vulnmanager.models.openvas.objects.OvResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OpenvasResultRepository extends JpaRepository<OvResult, Long> {

}
