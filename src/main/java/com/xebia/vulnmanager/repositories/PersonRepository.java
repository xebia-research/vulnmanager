package com.xebia.vulnmanager.repositories;


import com.xebia.vulnmanager.models.company.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Long> {
    Person findByUsername(String username);
    Person findByApiKey(String apiKey);
}
