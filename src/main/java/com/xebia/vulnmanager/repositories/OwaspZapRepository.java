package com.xebia.vulnmanager.repositories;

import com.xebia.vulnmanager.models.zap.objects.ZapReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OwaspZapRepository extends JpaRepository<ZapReport, Long> {

}
