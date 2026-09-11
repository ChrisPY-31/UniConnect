package com.chris.uniconnect.Repository;

import com.chris.uniconnect.Model.Entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student , Integer> {

    List<Student> findAllByName(String name);

    Student findByUserEntityUsername(String username);

    Integer countAllByCareer_CareerName(String careerName);

    long countByCareer_CareerName(String careerName);

    //se deben de filtar por nombre carrera


}
