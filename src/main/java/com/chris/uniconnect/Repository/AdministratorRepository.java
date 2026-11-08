package com.chris.uniconnect.Repository;

import com.chris.uniconnect.Model.Entity.Administrator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdministratorRepository extends JpaRepository<Administrator, Integer> {

    Administrator findByUserEntityUsername(String username);

    List<Administrator> findByInstitutionProfileIsNull();
}
