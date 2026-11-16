package com.chris.uniconnect.Repository;

import com.chris.uniconnect.Model.Entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student , Integer>, JpaSpecificationExecutor<Student> {

    //GET /students/summary: [idCareer, careerName, alumnos] de todas las carreras, aunque tengan 0
    @Query("select c.idCareer, c.careerName, count(s.id) from Career c left join c.students s group by c.idCareer, c.careerName order by c.careerName")
    List<Object[]> countStudentsByCareer();

    long countByIdCareerIsNull();

    List<Student> findAllByName(String name);

    Student findByUserEntityUsername(String username);

    Integer countAllByCareer_CareerName(String careerName);

    long countByCareer_CareerName(String careerName);

    //se deben de filtar por nombre carrera


}
